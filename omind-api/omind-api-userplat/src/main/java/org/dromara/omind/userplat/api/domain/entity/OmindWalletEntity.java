package org.dromara.omind.userplat.api.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.Version;
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
     * 可用余额(不含赠送金额)
     */
    private BigDecimal availableBalance;

    /**
     * 赠送金额
     */
    private BigDecimal giftBalance;

    /**
     * 冻结金额
     */
    private BigDecimal frozenAmount;

    /**
     * 累计充值金额
     */
    private BigDecimal totalRecharge;

    /**
     * 累计获得赠送金额
     */
    private BigDecimal totalGiftReceived;

    /**
     * 累计消费赠送金额
     */
    private BigDecimal totalGiftConsumed;

    /**
     * a累计消费金额
     */
    private BigDecimal totalConsume;

    /**
     * 钱包状态：0-禁用，1-启用
     */
    private Integer status;

    /**
     * 乐观锁版本号
     */
    @Version
    private Integer version;

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
