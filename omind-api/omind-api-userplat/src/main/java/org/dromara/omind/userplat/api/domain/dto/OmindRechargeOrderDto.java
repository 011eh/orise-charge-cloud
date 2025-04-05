package org.dromara.omind.userplat.api.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值订单请求/响应DTO
 */
@Data
@NoArgsConstructor
public class OmindRechargeOrderDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 订单ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 套餐ID
     */
    private Long packageId;

    /**
     * 套餐名称
     */
    private String packageName;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 充值金额
     */
    private BigDecimal rechargeMoney;

    /**
     * 赠送金额
     */
    private BigDecimal giftMoney;

    /**
     * 支付渠道：WXPAY-微信支付，ALIPAY-支付宝
     */
    private String payChannel;

    /**
     * 交易类型：JSAPI,APP等
     */
    private String tradeType;

    /**
     * 订单状态：0-待支付，1-支付成功，2-支付失败，3-已取消
     */
    private Integer status;

    /**
     * 支付平台交易号
     */
    private String transactionId;

    /**
     * 支付时间
     */
    private Date payTime;

    /**
     * 创建时间
     */
    private Date createTime;
} 