package org.dromara.omind.userplat.service.impl;

import com.wechat.pay.java.service.payments.jsapi.JsapiService;
import com.wechat.pay.java.service.payments.jsapi.model.Amount;
import com.wechat.pay.java.service.payments.jsapi.model.Payer;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayRequest;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayResponse;
import lombok.RequiredArgsConstructor;
import org.dromara.omind.userplat.api.config.WxProperties;
import org.dromara.omind.userplat.api.domain.dto.PaymentResponseDto;
import org.dromara.omind.userplat.service.PaymentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 微信JSAPI支付服务实现类
 */
@Service("weChatJsapiPaymentService")
@RequiredArgsConstructor
public class WeChatJsapiPaymentServiceImpl implements PaymentService {

    private final JsapiService jsapiService;
    private final WxProperties wxProperties;

    @Override
    public String createPayment(Long userId, BigDecimal amount, String description, String outTradeNo) {
        PrepayRequest request = new PrepayRequest();

        Amount amountObj = new Amount();
        BigDecimal bigDecimal = amount.multiply(new BigDecimal("100"));
        amountObj.setTotal(bigDecimal.intValue());
        request.setAmount(amountObj);

        request.setAppid(wxProperties.getPayAppId());
        request.setMchid(wxProperties.getMerchantId());
        request.setNotifyUrl(wxProperties.getNotifyUrl());
        request.setDescription(description);
        request.setOutTradeNo(outTradeNo);

        Payer payer = new Payer();
        payer.setOpenid(""); // 使用用户ID作为OpenID，实际使用中应从用户体系获取真实OpenID
        request.setPayer(payer);

        // 创建预支付订单
        PrepayResponse response = jsapiService.prepay(request);
        return response.getPrepayId();
    }

    @Override
    public PaymentResponseDto createPaymentWithResponse(Long userId, BigDecimal amount, String description, String outTradeNo) {
        String prepayId = createPayment(userId, amount, description, outTradeNo);
        return PaymentResponseDto.of(prepayId, outTradeNo);
    }

    @Override
    public boolean supports(String payChannel, String tradeType) {
        return "WXPAY".equals(payChannel) && "JSAPI".equals(tradeType);
    }
} 
