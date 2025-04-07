package org.dromara.omind.userplat.service.impl;

import com.wechat.pay.java.service.payments.nativepay.NativePayService;
import com.wechat.pay.java.service.payments.nativepay.model.Amount;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayRequest;
import com.wechat.pay.java.service.payments.nativepay.model.PrepayResponse;
import lombok.RequiredArgsConstructor;
import org.dromara.omind.userplat.api.config.WxProperties;
import org.dromara.omind.userplat.api.domain.dto.PaymentResponseDto;
import org.dromara.omind.userplat.service.PaymentService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("weChatPaymentService")
@RequiredArgsConstructor
public class WeChatPaymentServiceImpl implements PaymentService {

    private final NativePayService nativePayService;
    private final WxProperties wxProperties;

    @Override
    public String createPayment(Long userId, BigDecimal amount, String description, String outTradeNo) {
        PrepayRequest request = new PrepayRequest();

        Amount amountObj = new Amount();
        amountObj.setTotal(amount.multiply(new BigDecimal("100")).intValue());
        request.setAmount(amountObj);

        request.setAppid(wxProperties.getPayAppId());
        request.setMchid(wxProperties.getMerchantId());
        request.setNotifyUrl(wxProperties.getNotifyUrl());
        request.setDescription(description);
        request.setOutTradeNo(outTradeNo);

        // Create prepay order
        PrepayResponse response = nativePayService.prepay(request);
        return response.getCodeUrl();
    }

    @Override
    public PaymentResponseDto createPaymentWithResponse(Long userId, BigDecimal amount, String description, String outTradeNo) {
        String paymentUrl = createPayment(userId, amount, description, outTradeNo);
        return PaymentResponseDto.of(paymentUrl, outTradeNo);
    }

    @Override
    public boolean supports(String payChannel, String tradeType) {
        return "WXPAY".equals(payChannel) && "NATIVE".equals(tradeType);
    }
} 
