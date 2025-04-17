package org.dromara.omind.userplat.service;

import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.omind.userplat.domain.entity.OmindWithdrawalRecordEntity;

import java.math.BigDecimal;

/**
 * 提现服务接口
 */
public interface OmindWithdrawalService {

    /**
     * 申请提现
     *
     * @param userId 用户ID
     * @param amount 提现金额
     * @param reason 提现原因
     * @return 提现记录ID
     */
    Long applyWithdrawal(Long userId, BigDecimal amount, String reason);

    /**
     * 处理提现回调
     *
     * @param outRefundNo 商户退款单号
     * @param refundId    微信退款单号
     * @param status      退款状态
     * @return 处理结果
     */
    boolean handleWithdrawalCallback(String outRefundNo, String refundId, String status);

    /**
     * 获取用户提现记录
     *
     * @param userId    用户ID
     * @param pageQuery 分页查询
     * @return 提现记录列表
     */
    TableDataInfo<OmindWithdrawalRecordEntity> getWithdrawalRecords(Long userId, PageQuery pageQuery);
} 
