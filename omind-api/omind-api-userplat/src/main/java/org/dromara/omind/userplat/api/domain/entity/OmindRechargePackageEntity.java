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
 * 充值套餐表
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@TableName("omind_recharge_package")
public class OmindRechargePackageEntity extends TenantEntity {

    /**
     * 套餐ID
     */
    @TableId(value = "package_id")
    private Long packageId;

    /**
     * 套餐名称
     */
    private String packageName;

    /**
     * 充值金额
     */
    private BigDecimal rechargeMoney;

    /**
     * 赠送金额
     */
    private BigDecimal giftMoney;

    /**
     * 状态：0-禁用，1-启用
     */
    private Integer enableState;

    /**
     * 是否删除：0-正常，1-删除
     */
    private Integer isDel;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;
} 