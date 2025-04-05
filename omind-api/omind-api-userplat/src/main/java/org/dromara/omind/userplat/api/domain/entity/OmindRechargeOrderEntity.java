package org.dromara.omind.userplat.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.tenant.core.TenantEntity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 充值订单表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("omind_recharge_order")
public class OmindRechargeOrderEntity extends TenantEntity {

    /**
     * 订单ID
     */
    @TableId
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
     * 退款状态：0-未退款，1-部分退款，2-全额退款
     */
    private Integer refundStatus;

    /**
     * 已退款金额
     */
    private BigDecimal refundAmount;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableField("del_flag")
    private Integer delFlag;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
} 