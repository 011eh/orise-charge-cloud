package org.dromara.omind.userplat.service;

import org.dromara.omind.userplat.api.domain.dto.PaymentResponseDto;

import java.math.BigDecimal;

public interface PaymentService {
    /**
     * Create a payment order and return the payment URL
     * @param userId User ID
     * @param amount Payment amount
     * @param description Payment description
     * @param outTradeNo Merchant order number
     * @return Payment URL
     */
    String createPayment(Long userId, BigDecimal amount, String description, String outTradeNo);
    
    /**
     * Create a payment with response containing both payment URL and order number
     * @param userId User ID
     * @param amount Payment amount
     * @param description Payment description
     * @param outTradeNo Merchant order number (if null, a new one will be generated)
     * @return PaymentResponseDto containing payment URL and order number
     */
    PaymentResponseDto createPaymentWithResponse(Long userId, BigDecimal amount, String description, String outTradeNo);
    
    /**
     * Check if this payment service supports the given payment channel and trade type
     * @param payChannel Payment channel (e.g., "WECHAT", "ALIPAY")
     * @param tradeType Trade type (e.g., "JSAPI", "NATIVE", "APP")
     * @return true if supported, false otherwise
     */
    boolean supports(String payChannel, String tradeType);
} 