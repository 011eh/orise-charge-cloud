package org.dromara.omind.userplat.api.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 余额流水请求/响应DTO
 */
@Data
@NoArgsConstructor
public class OmindBalanceFlowDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 流水ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 流水类型：1-充值，2-消费，3-退款
     */
    private Integer flowType;

    /**
     * 流水类型名称
     */
    private String flowTypeName;

    /**
     * 金额
     */
    private BigDecimal amount;

    /**
     * 变动后余额
     */
    private BigDecimal balance;

    /**
     * 交易单号
     */
    private String tradeNo;

    /**
     * 外部交易单号
     */
    private String outTradeNo;

    /**
     * 关联ID，如订单ID、充电ID等
     */
    private String relatedId;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 查询开始时间
     */
    private Date beginTime;

    /**
     * 查询结束时间
     */
    private Date endTime;
} 