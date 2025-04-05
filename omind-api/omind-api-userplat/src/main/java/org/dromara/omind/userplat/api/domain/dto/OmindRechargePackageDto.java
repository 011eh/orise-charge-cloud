package org.dromara.omind.userplat.api.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 充值套餐请求/响应DTO
 */
@Data
@NoArgsConstructor
public class OmindRechargePackageDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 套餐ID
     */
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
} 