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
 * 余额流水表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("omind_balance_flow")
public class OmindBalanceFlowEntity extends TenantEntity {

    /**
     * 流水ID
     */
    @TableId
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