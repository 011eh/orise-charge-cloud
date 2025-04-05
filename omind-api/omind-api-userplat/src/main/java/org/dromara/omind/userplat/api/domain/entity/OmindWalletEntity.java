package org.dromara.omind.userplat.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.dromara.common.mybatis.core.domain.BaseEntity;
import org.dromara.common.tenant.core.TenantEntity;

import java.math.BigDecimal;

/**
 * 用户钱包表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("omind_wallet")
public class OmindWalletEntity extends TenantEntity {

    /**
     * 主键ID
     */
    @TableId
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 钱包余额
     */
    private BigDecimal balance;

    /**
     * 冻结金额
     */
    private BigDecimal frozenAmount;

    /**
     * 累计充值金额
     */
    private BigDecimal totalRecharge;

    /**
     * a累计消费金额
     */
    private BigDecimal totalConsume;

    /**
     * 钱包状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableField("del_flag")
    private Integer delFlag;
} 