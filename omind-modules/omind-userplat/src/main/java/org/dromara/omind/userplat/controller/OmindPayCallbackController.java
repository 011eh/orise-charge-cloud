package org.dromara.omind.userplat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.domain.R;
import org.dromara.common.web.core.BaseController;
import org.dromara.omind.userplat.service.OmindWalletService;
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

    /**
     * 充值支付成功回调
     */
    @Operation(summary = "充值支付成功回调")
    @PostMapping("/recharge/success")
    public R<Boolean> handleRechargeSuccess(@Parameter(description = "商户订单号") @RequestParam String outTradeNo,
                                          @Parameter(description = "交易号") @RequestParam String transactionId) {
        log.info("收到充值支付成功回调，商户订单号：{}，交易号：{}", outTradeNo, transactionId);
        return R.ok(walletService.handleRechargeSuccess(outTradeNo, transactionId));
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