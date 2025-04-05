package org.dromara.omind.userplat.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.omind.userplat.api.domain.dto.OmindBalanceFlowDto;
import org.dromara.omind.userplat.api.domain.dto.OmindWalletDto;
import org.dromara.omind.userplat.api.domain.entity.OmindWalletEntity;

import java.math.BigDecimal;

/**
 * 钱包服务接口
 */
public interface OmindWalletService {

    /**
     * 获取用户钱包信息
     *
     * @param userId 用户ID
     * @return 钱包信息
     */
    OmindWalletDto getUserWallet(Long userId);

    /**
     * 获取用户余额流水
     *
     * @param balanceFlowDto 查询条件
     * @param pageQuery      分页查询
     * @return 流水列表
     */
    TableDataInfo<OmindBalanceFlowDto> getBalanceFlowList(OmindBalanceFlowDto balanceFlowDto, PageQuery pageQuery);

    /**
     * 充值
     *
     * @param userId       用户ID
     * @param packageId    套餐ID
     * @param amount       充值金额
     * @param giftAmount   赠送金额
     * @param payChannel   支付渠道：WXPAY-微信支付，ALIPAY-支付宝
     * @param tradeType    交易类型：JSAPI,APP等
     * @return 商户订单号
     */
    String recharge(Long userId, Long packageId, BigDecimal amount, BigDecimal giftAmount, String payChannel, String tradeType);

    /**
     * 充值成功回调处理
     *
     * @param outTradeNo   商户订单号
     * @param transactionId 交易号
     * @return 处理结果
     */
    Boolean handleRechargeSuccess(String outTradeNo, String transactionId);

    /**
     * 消费
     *
     * @param userId       用户ID
     * @param amount       消费金额
     * @param relatedId    关联ID，如订单ID、充电ID等
     * @param remark       备注
     * @return 消费结果
     */
    Boolean consume(Long userId, BigDecimal amount, String relatedId, String remark);
} 