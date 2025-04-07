package org.dromara.omind.userplat.api.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponseDto {
    private String paymentInfo;
    private String outTradeNo;
    
    public static PaymentResponseDto of(String paymentInfo, String outTradeNo) {
        PaymentResponseDto dto = new PaymentResponseDto();
        dto.setPaymentInfo(paymentInfo);
        dto.setOutTradeNo(outTradeNo);
        return dto;
    }
} 
