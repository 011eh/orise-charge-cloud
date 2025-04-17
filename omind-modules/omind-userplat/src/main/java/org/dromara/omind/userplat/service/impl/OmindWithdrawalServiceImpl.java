package org.dromara.omind.userplat.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.binarywang.wxpay.service.WxPayService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.omind.userplat.api.domain.entity.OmindBalanceFlowEntity;
import org.dromara.omind.userplat.api.domain.entity.OmindRechargeOrderEntity;
import org.dromara.omind.userplat.api.domain.entity.OmindWalletEntity;
import org.dromara.omind.userplat.domain.entity.OmindWithdrawalRecordEntity;
import org.dromara.omind.userplat.domain.service.OmindBalanceFlowEntityIService;
import org.dromara.omind.userplat.domain.service.OmindRechargeOrderEntityIService;
import org.dromara.omind.userplat.domain.service.OmindWalletEntityIService;
import org.dromara.omind.userplat.domain.service.OmindWithdrawalRecordEntityIService;
import org.dromara.omind.userplat.service.OmindWithdrawalService;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 提现服务实现类
 * <p>
 * 提现业务逻辑说明：
 * 1. 用户申请提现：
 * - 只能提现累计充值的金额，不能提现赠送金额
 * - 申请提现时，检查用户钱包余额和累计充值金额是否足够
 * - 冻结用户钱包中相应的金额
 * - 创建提现记录
 * - 根据用户的充值订单历史记录，找到可用于退款的订单
 * - 在处理退款时，只考虑实际充值金额，不包括赠送金额
 * - 更新每个涉及的充值订单的退款状态和退款金额
 * <p>
 * 2. 提现回调处理：
 * - 提现成功时，更新钱包的冻结金额和总充值金额
 * - 同时，根据退款比例，计算并扣除相应的赠送金额
 * - 赠送金额扣除计算方式：根据每个订单的退款金额占比，计算需要扣减的赠送金额
 * - 记录相应的余额流水，包括提现退款和赠送金额扣减
 * <p>
 * 3. 提现失败处理：
 * - 将冻结金额返回到用户钱包余额
 * - 不扣除赠送金额
 * - 更新提现记录状态
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OmindWithdrawalServiceImpl implements OmindWithdrawalService {

    private final OmindWithdrawalRecordEntityIService withdrawalRecordService;
    private final OmindWalletEntityIService walletEntityIService;
    private final OmindBalanceFlowEntityIService balanceFlowEntityIService;
    private final OmindRechargeOrderEntityIService rechargeOrderEntityIService;
    private final WxPayService wxPayService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long applyWithdrawal(Long userId, BigDecimal amount, String reason) {
        // 1. 检查用户钱包余额
        OmindWalletEntity wallet = getWalletByUserId(userId);
        if (wallet == null) {
            throw new ServiceException("用户钱包不存在");
        }

        // 2. 检查累计充值是否足够提现
        if (wallet.getTotalRecharge().compareTo(amount) < 0) {
            throw new ServiceException("钱包累计充值余额不足");
        }

        // 3. 检查当前余额是否足够提现
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new ServiceException("钱包余额不足");
        }

        // 4. 冻结提现金额
        wallet.setBalance(wallet.getBalance().subtract(amount));
        wallet.setFrozenAmount(wallet.getFrozenAmount().add(amount));

        boolean updated = walletEntityIService.updateById(wallet);
        if (!updated) {
            throw new ServiceException("冻结提现金额失败");
        }

        // 5. 生成退款单号
        String outRefundNo = "WD" + DateUtil.format(LocalDateTime.now(), "yyyyMMddHHmmss") + IdUtil.fastSimpleUUID().substring(0, 8);

        // 6. 创建提现记录
        OmindWithdrawalRecordEntity withdrawalRecord = new OmindWithdrawalRecordEntity();
        withdrawalRecord.setUserId(userId);
        withdrawalRecord.setOutRefundNo(outRefundNo);
        withdrawalRecord.setAmount(amount);
        withdrawalRecord.setCurrency("CNY");
        withdrawalRecord.setStatus(0); // 处理中
        withdrawalRecord.setChannel("ORIGINAL"); // 原路退款
        withdrawalRecord.setReason(reason);
        withdrawalRecord.setDelFlag(0);

        boolean saved = withdrawalRecordService.save(withdrawalRecord);
        if (!saved) {
            throw new ServiceException("创建提现记录失败");
        }

        // 7. 查询用户最近的充值记录，用于原路退款
        LambdaQueryWrapper<OmindRechargeOrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmindRechargeOrderEntity::getUserId, userId)
                .eq(OmindRechargeOrderEntity::getStatus, 1) // 支付成功的订单
                .eq(OmindRechargeOrderEntity::getDelFlag, 0)
                .orderByDesc(OmindRechargeOrderEntity::getCreateTime);

        List<OmindRechargeOrderEntity> rechargeOrderList = rechargeOrderEntityIService.list(queryWrapper);
        if (rechargeOrderList.isEmpty()) {
            throw new ServiceException("未找到可用于退款的充值订单");
        }

        // 8. 处理退款逻辑，可能需要涉及多个充值订单
        BigDecimal remainingAmount = amount;
        String primaryTransactionId = null;

        // 记录将要涉及的所有订单ID
        StringBuilder involvedOrderIds = new StringBuilder();

        for (OmindRechargeOrderEntity order : rechargeOrderList) {
            // 只考虑充值金额部分，不包括赠送金额
            BigDecimal rechargeMoney = order.getRechargeMoney();
            // 已退款金额
            BigDecimal alreadyRefunded = order.getRefundAmount() != null ? order.getRefundAmount() : BigDecimal.ZERO;
            // 可退款金额 = 充值金额 - 已退款金额（不考虑赠送金额）
            BigDecimal availableRefund = rechargeMoney.subtract(alreadyRefunded);

            if (availableRefund.compareTo(BigDecimal.ZERO) <= 0) {
                // 该订单已无可退款金额，跳过
                continue;
            }

            // 确定本次从该订单退款的金额
            BigDecimal currentRefundAmount;
            if (remainingAmount.compareTo(availableRefund) >= 0) {
                // 需要退款的金额大于或等于该订单可退款金额，全部退
                currentRefundAmount = availableRefund;
            } else {
                // 只需要退一部分
                currentRefundAmount = remainingAmount;
            }

            // 更新订单退款状态和退款金额
            order.setRefundAmount(alreadyRefunded.add(currentRefundAmount));

            // 更新退款状态
            if (order.getRefundAmount().compareTo(rechargeMoney) >= 0) {
                // 如果退款金额等于或超过充值金额，设为全额退款（注意这里只比较充值金额，不包括赠送金额）
                order.setRefundStatus(2); // 全额退款
            } else {
                // 否则是部分退款
                order.setRefundStatus(1); // 部分退款
            }

            // 记录涉及的订单ID
            if (!involvedOrderIds.isEmpty()) {
                involvedOrderIds.append(",");
            }
            involvedOrderIds.append(order.getId());

            // 更新订单信息
            boolean orderUpdated = rechargeOrderEntityIService.updateById(order);
            if (!orderUpdated) {
                throw new ServiceException("更新订单退款状态失败");
            }

            // 如果是第一个处理的订单，记录其交易ID用于向支付平台发起退款请求
            if (primaryTransactionId == null && order.getTransactionId() != null && !order.getTransactionId().isEmpty()) {
                primaryTransactionId = order.getTransactionId();
                // 将交易ID记录到提现记录中
                withdrawalRecord.setTransactionId(primaryTransactionId);
                withdrawalRecordService.updateById(withdrawalRecord);
            }

            // 减少剩余需要退款的金额
            remainingAmount = remainingAmount.subtract(currentRefundAmount);

            // 如果已经退完了所需金额，可以退出循环
            if (remainingAmount.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
        }

        // 记录涉及的订单ID到提现记录的备注中，方便后续查询
        withdrawalRecord.setRemark(reason + " [涉及订单:" + involvedOrderIds.toString() + "]");
        withdrawalRecordService.updateById(withdrawalRecord);

        // 9. 如果剩余金额大于0，说明订单不足以退款
        if (remainingAmount.compareTo(BigDecimal.ZERO) > 0) {
            throw new ServiceException("可退款的充值订单金额不足");
        }

        // 10. 调用微信支付退款接口
//        try {
//            if (primaryTransactionId != null) {
//                // 构建退款请求
//                WxPayRefundRequest refundRequest = new WxPayRefundRequest();
//                refundRequest.setTransactionId(primaryTransactionId);
//                refundRequest.setOutRefundNo(outRefundNo);
//                refundRequest.setRefundDesc(reason);
//                
//                // 这里是总金额和退款金额，需要根据微信支付API要求设置正确的金额
//                // 通常总金额是订单原始金额，退款金额是本次退款金额
//                // 这里的逻辑可能需要根据实际业务和微信支付API文档调整
//                refundRequest.setTotalFee(amount.multiply(new BigDecimal("100")).intValue()); // 转为分
//                refundRequest.setRefundFee(amount.multiply(new BigDecimal("100")).intValue()); // 转为分
//                
//                wxPayService.refund(refundRequest);
//            } else {
//                // 如果没有交易ID，可能是其他支付方式或者内部充值
//                // 这里处理其他退款渠道逻辑
//                // 直接更新提现记录为成功状态，因为不需要第三方支付平台退款
//                handleWithdrawalCallback(outRefundNo, "", "SUCCESS");
//            }
//        } catch (WxPayException e) {
//            log.error("调用微信支付退款接口失败", e);
//            throw new ServiceException("提现申请失败：" + e.getMessage());
//        }

        return withdrawalRecord.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handleWithdrawalCallback(String outRefundNo, String refundId, String status) {
        // 1. 查询提现记录
        LambdaQueryWrapper<OmindWithdrawalRecordEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmindWithdrawalRecordEntity::getOutRefundNo, outRefundNo)
                .eq(OmindWithdrawalRecordEntity::getDelFlag, 0);

        OmindWithdrawalRecordEntity withdrawalRecord = withdrawalRecordService.getOne(queryWrapper);
        if (withdrawalRecord == null) {
            log.error("提现记录不存在: {}", outRefundNo);
            return false;
        }

        // 2. 更新提现记录状态
        withdrawalRecord.setRefundId(refundId);
        withdrawalRecord.setStatus("SUCCESS".equals(status) ? 1 : 2); // 1-成功，2-失败
        if ("SUCCESS".equals(status)) {
            withdrawalRecord.setSuccessTime(LocalDateTime.now());
        }

        boolean updated = withdrawalRecordService.updateById(withdrawalRecord);
        if (!updated) {
            log.error("更新提现记录状态失败: {}", outRefundNo);
            return false;
        }

        // 3. 查询用户钱包
        OmindWalletEntity wallet = getWalletByUserId(withdrawalRecord.getUserId());
        if (wallet == null) {
            log.error("用户钱包不存在: {}", withdrawalRecord.getUserId());
            return false;
        }

        // 记录相关订单ID，用于流水记录
        StringBuilder relatedOrderIds = new StringBuilder();

        // 4. 解冻金额并处理用户钱包
        if ("SUCCESS".equals(status)) {
            // 提现成功，从冻结金额中扣除
            wallet.setFrozenAmount(wallet.getFrozenAmount().subtract(withdrawalRecord.getAmount()));

            // 减少累计充值金额（因为提现了部分充值金额）
            wallet.setTotalRecharge(wallet.getTotalRecharge().subtract(withdrawalRecord.getAmount()));

            // 5. 查询与此提现相关的充值订单
            LambdaQueryWrapper<OmindRechargeOrderEntity> orderQueryWrapper = new LambdaQueryWrapper<>();
            orderQueryWrapper.eq(OmindRechargeOrderEntity::getUserId, withdrawalRecord.getUserId())
                    .in(OmindRechargeOrderEntity::getRefundStatus, 1, 2) // 已部分退款或全额退款的订单
                    .gt(OmindRechargeOrderEntity::getRefundAmount, 0) // 有退款金额的订单
                    .eq(OmindRechargeOrderEntity::getDelFlag, 0);

            List<OmindRechargeOrderEntity> refundedOrders = rechargeOrderEntityIService.list(orderQueryWrapper);

            // 计算退款金额对应的赠送金额比例
            BigDecimal totalGiftAmountToDeduct = BigDecimal.ZERO;

            // 填充相关订单ID并计算相应的赠送金额
            for (OmindRechargeOrderEntity order : refundedOrders) {
                if (relatedOrderIds.length() > 0) {
                    relatedOrderIds.append(",");
                }
                relatedOrderIds.append(order.getId());

                // 计算这个订单中退款金额占总充值金额的比例
                if (order.getRechargeMoney().compareTo(BigDecimal.ZERO) > 0 && order.getGiftMoney().compareTo(BigDecimal.ZERO) > 0) {
                    // 该订单的总金额 = 充值金额 + 赠送金额
                    BigDecimal totalOrderAmount = order.getRechargeMoney().add(order.getGiftMoney());

                    // 已退款金额
                    BigDecimal refundAmount = order.getRefundAmount() != null ? order.getRefundAmount() : BigDecimal.ZERO;

                    // 计算退款比例
                    BigDecimal refundRatio = refundAmount.divide(totalOrderAmount, 4, BigDecimal.ROUND_HALF_UP);

                    // 根据退款比例计算需要扣减的赠送金额
                    BigDecimal giftAmountToDeduct = order.getGiftMoney().multiply(refundRatio);

                    // 累加到总需扣减的赠送金额
                    totalGiftAmountToDeduct = totalGiftAmountToDeduct.add(giftAmountToDeduct);
                }
            }

            // 从钱包余额中扣除相应的赠送金额
            if (totalGiftAmountToDeduct.compareTo(BigDecimal.ZERO) > 0) {
                wallet.setBalance(wallet.getBalance().subtract(totalGiftAmountToDeduct));

                // 记录赠送金额扣减的流水
                OmindBalanceFlowEntity giftFlowEntity = new OmindBalanceFlowEntity();
                giftFlowEntity.setUserId(withdrawalRecord.getUserId());
                giftFlowEntity.setFlowType(3); // 退款/提现
                giftFlowEntity.setAmount(totalGiftAmountToDeduct.negate()); // 使用负数表示减少
                giftFlowEntity.setBalance(wallet.getBalance());
                giftFlowEntity.setTradeNo(IdUtil.simpleUUID()); // 内部流水号
                giftFlowEntity.setOutTradeNo(outRefundNo + "-GIFT"); // 商户退款单号-赠送金额
                if (relatedOrderIds.length() > 0) {
                    giftFlowEntity.setRelatedId(relatedOrderIds.toString());
                }
                giftFlowEntity.setRemark("提现成功扣减赠送金额: " + withdrawalRecord.getReason());
                giftFlowEntity.setDelFlag(0);

                boolean giftFlowSaved = balanceFlowEntityIService.save(giftFlowEntity);
                if (!giftFlowSaved) {
                    log.error("创建赠送金额扣减流水记录失败: {}", outRefundNo);
                    return false;
                }
            }

        } else {
            // 提现失败，冻结金额返回到余额
            wallet.setBalance(wallet.getBalance().add(withdrawalRecord.getAmount()));
            wallet.setFrozenAmount(wallet.getFrozenAmount().subtract(withdrawalRecord.getAmount()));
        }

        boolean walletUpdated = walletEntityIService.updateById(wallet);
        if (!walletUpdated) {
            log.error("更新用户钱包失败: {}", withdrawalRecord.getUserId());
            return false;
        }

        // 6. 如果提现成功，记录余额流水
        if ("SUCCESS".equals(status)) {
            OmindBalanceFlowEntity flowEntity = new OmindBalanceFlowEntity();
            flowEntity.setUserId(withdrawalRecord.getUserId());
            flowEntity.setFlowType(3); // 退款/提现
            flowEntity.setAmount(withdrawalRecord.getAmount().negate()); // 使用负数表示减少
            flowEntity.setBalance(wallet.getBalance().add(withdrawalRecord.getAmount())); // 这里记录扣除赠送金额前的余额
            flowEntity.setTradeNo(IdUtil.simpleUUID()); // 内部流水号
            flowEntity.setOutTradeNo(outRefundNo); // 商户退款单号
            // 如果有关联订单ID，添加到关联ID字段
            if (relatedOrderIds.length() > 0) {
                flowEntity.setRelatedId(relatedOrderIds.toString());
            }
            flowEntity.setRemark("提现成功: " + withdrawalRecord.getReason());
            flowEntity.setDelFlag(0);

            boolean flowSaved = balanceFlowEntityIService.save(flowEntity);
            if (!flowSaved) {
                log.error("创建余额流水记录失败: {}", outRefundNo);
                return false;
            }
        }

        return true;
    }

    @Override
    public TableDataInfo<OmindWithdrawalRecordEntity> getWithdrawalRecords(Long userId, PageQuery pageQuery) {
        LambdaQueryWrapper<OmindWithdrawalRecordEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(userId != null, OmindWithdrawalRecordEntity::getUserId, userId)
                .eq(OmindWithdrawalRecordEntity::getDelFlag, 0)
                .orderByDesc(OmindWithdrawalRecordEntity::getCreateTime);

        Page<OmindWithdrawalRecordEntity> page = withdrawalRecordService.page(pageQuery.build(), queryWrapper);
        return TableDataInfo.build(page);
    }

    /**
     * 根据用户ID获取钱包信息
     */
    private OmindWalletEntity getWalletByUserId(Long userId) {
        LambdaQueryWrapper<OmindWalletEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmindWalletEntity::getUserId, userId)
                .eq(OmindWalletEntity::getDelFlag, 0);
        return walletEntityIService.getOne(queryWrapper);
    }

    /**
     * 调用微信支付退款接口
     *
     * @param transactionId 微信支付订单号
     * @param outRefundNo   商户退款单号
     * @param totalAmount   订单总金额
     * @param refundAmount  退款金额
     * @param reason        退款原因
     * @return 接口返回结果
     */
    private String applyWechatRefund(String transactionId, String outRefundNo, BigDecimal totalAmount, BigDecimal refundAmount, String reason) {
        // 这里只是示例，实际需要根据微信支付V3接口文档实现
        // https://pay.weixin.qq.com/doc/v3/merchant/4012791883

        String url = "https://api.mch.weixin.qq.com/v3/refund/domestic/refunds";

        // 构造请求参数
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("transaction_id", transactionId);
        requestBody.put("out_refund_no", outRefundNo);

        Map<String, Object> amount = new HashMap<>();
        amount.put("refund", refundAmount.multiply(new BigDecimal("100")).intValue()); // 转为分
        amount.put("total", totalAmount.multiply(new BigDecimal("100")).intValue()); // 转为分
        amount.put("currency", "CNY");

        requestBody.put("amount", amount);
        if (reason != null && !reason.isEmpty()) {
            requestBody.put("reason", reason);
        }

        // 设置请求头，包含认证信息
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // TODO: 实现微信支付API的签名认证
        // headers.set("Authorization", "签名信息");

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // 实际项目中应该使用RestTemplate或其他HTTP客户端发送请求
        // 这里只是模拟返回成功
        log.info("请求微信退款接口: URL={}, Body={}", url, requestBody);

        // 模拟成功返回
        return "{\"refund_id\":\"50000000382019052709732678859\",\"out_refund_no\":\"" + outRefundNo + "\",\"transaction_id\":\"" + transactionId + "\",\"status\":\"SUCCESS\"}";
    }
} 
