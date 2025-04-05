package org.dromara.omind.userplat.api.domain.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 用户钱包请求/响应DTO
 */
@Data
@NoArgsConstructor
public class OmindWalletDto implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名称
     */
    private String userName;

    /**
     * 用户手机号
     */
    private String mobile;

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
     * 累计消费金额
     */
    private BigDecimal totalConsume;

    /**
     * 钱包状态：0-禁用，1-启用
     */
    private Integer status;
} 