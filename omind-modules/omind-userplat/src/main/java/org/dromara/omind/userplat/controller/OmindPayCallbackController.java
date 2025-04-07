package org.dromara.omind.userplat.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.binarywang.wxpay.v3.util.AesUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.omind.userplat.service.OmindWalletService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

/**
 * 支付回调Controller
 */
@Slf4j
@Tag(name = "支付回调接口")
@RequiredArgsConstructor
@RestController
@RequestMapping("/userplat/pay/callback")
public class OmindPayCallbackController extends BaseController {

    private final OmindWalletService walletService;
    private final ObjectMapper objectMapper;

    @Value("${wx.pay.apiV3Key}")
    private String apiV3Key;

    /**
     * 充值支付成功回调
     */
    @Operation(summary = "充值支付成功回调")
    @PostMapping("/recharge/success")
    public R<Boolean> handleRechargeSuccess(
            @RequestBody String requestBody,
            @RequestHeader("Wechatpay-Timestamp") String timestamp,
            @RequestHeader("Wechatpay-Nonce") String nonce,
            @RequestHeader("Wechatpay-Signature") String signature,
            @RequestHeader("Wechatpay-Serial") String serial) {

        log.info("Received WeChat Pay callback notification");
        try {
            // 记录签名相关信息
            log.info("WeChat Pay Headers - \nTimestamp: {}\nNonce: {}\nSignature: {}\nSerial: {}",
                    timestamp, nonce, signature, serial);

            // 解析回调通知JSON
            JsonNode callbackData = objectMapper.readTree(requestBody);
            log.info("Callback data: {}", callbackData);

            // 验证回调数据类型
            String resourceType = callbackData.get("resource_type").asText();
            String eventType = callbackData.get("event_type").asText();

            log.info("Resource type: {}, Event type: {}", resourceType, eventType);

            if (!"encrypt-resource".equals(resourceType) || !"TRANSACTION.SUCCESS".equals(eventType)) {
                log.warn("Unexpected resource or event type. ResourceType: {}, EventType: {}",
                        resourceType, eventType);
                return R.fail();
            }

            // 获取加密数据
            JsonNode resource = callbackData.get("resource");
            String ciphertext = resource.get("ciphertext").asText();
            String nonceCipher = resource.get("nonce").asText();
            String associatedData = resource.has("associated_data") ?
                    resource.get("associated_data").asText() : "";

            // 解密数据

            AesUtils aesUtils = new AesUtils(apiV3Key.getBytes());
            String decryptedData = aesUtils.decryptToString(associatedData.getBytes(), nonceCipher.getBytes(), ciphertext);
            JsonNode decryptedJson = objectMapper.readTree(decryptedData);

            // 打印解密后的支付通知数据
            log.info("Decrypted payment data: {}", decryptedJson);

            // 处理支付结果
            String tradeState = decryptedJson.get("trade_state").asText();
            String transactionId = decryptedJson.get("transaction_id").asText();
            String outTradeNo = decryptedJson.get("out_trade_no").asText();

            log.info("Payment result - TradeState: {}, TransactionId: {}, OutTradeNo: {}",
                    tradeState, transactionId, outTradeNo);

            // 如果支付成功，业务处理
            if ("SUCCESS".equals(tradeState) && walletService.handleRechargeSuccess(outTradeNo, transactionId)) {
                return R.ok();
            } else {
                return R.fail();
            }

        } catch (Exception e) {
            log.error("Error processing WeChat Pay notification", e);
            return R.fail();
        }
    }

    /**
     * 模拟支付成功（仅用于测试）
     */
    @Operation(summary = "模拟支付成功（仅用于测试）")
    @PostMapping("/recharge/mock/success")
    public R<Boolean> mockRechargeSuccess(@Parameter(description = "商户订单号") @RequestParam String outTradeNo) {
        log.info("模拟充值支付成功，商户订单号：{}", outTradeNo);
        return R.ok(walletService.handleRechargeSuccess(outTradeNo, "mock_transaction_" + outTradeNo));
    }
} 
