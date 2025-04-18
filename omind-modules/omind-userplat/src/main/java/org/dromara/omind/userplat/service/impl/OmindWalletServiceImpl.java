package org.dromara.omind.userplat.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.common.core.exception.ServiceException;
import org.dromara.common.mybatis.core.page.PageQuery;
import org.dromara.common.mybatis.core.page.TableDataInfo;
import org.dromara.omind.userplat.api.domain.dto.OmindBalanceFlowDto;
import org.dromara.omind.userplat.api.domain.dto.OmindRechargePackageDto;
import org.dromara.omind.userplat.api.domain.dto.OmindWalletDto;
import org.dromara.omind.userplat.api.domain.dto.PaymentResponseDto;
import org.dromara.omind.userplat.api.domain.entity.OmindBalanceFlowEntity;
import org.dromara.omind.userplat.api.domain.entity.OmindRechargeOrderEntity;
import org.dromara.omind.userplat.api.domain.entity.OmindUserEntity;
import org.dromara.omind.userplat.api.domain.entity.OmindWalletEntity;
import org.dromara.omind.userplat.domain.service.OmindBalanceFlowEntityIService;
import org.dromara.omind.userplat.domain.service.OmindRechargeOrderEntityIService;
import org.dromara.omind.userplat.domain.service.OmindWalletEntityIService;
import org.dromara.omind.userplat.service.OmindRechargePackageService;
import org.dromara.omind.userplat.service.OmindUserService;
import org.dromara.omind.userplat.service.OmindWalletService;
import org.dromara.omind.userplat.service.PaymentService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OmindWalletServiceImpl implements OmindWalletService {

    private final OmindWalletEntityIService walletEntityIService;
    private final OmindBalanceFlowEntityIService balanceFlowEntityIService;
    private final OmindRechargeOrderEntityIService rechargeOrderEntityIService;
    private final OmindRechargePackageService rechargePackageService;
    private final OmindUserService userService;
    private final ObjectProvider<PaymentService> paymentServices;

    @Override
    public OmindWalletDto getUserWallet(Long userId) {
        LambdaQueryWrapper<OmindWalletEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmindWalletEntity::getUserId, userId)
            .eq(OmindWalletEntity::getDelFlag, 0);

        OmindWalletEntity walletEntity = walletEntityIService.getOne(queryWrapper);
        if (walletEntity == null) {
            // 如果用户钱包不存在，则创建一个
            walletEntity = createWallet(userId);
        }

        OmindWalletDto walletDto = BeanUtil.copyProperties(walletEntity, OmindWalletDto.class);
        
        // 设置用户信息
        OmindUserEntity userEntity = userService.getUserById(userId);
        if (userEntity != null) {
            walletDto.setUserName(userEntity.getNickName());
            walletDto.setMobile(userEntity.getMobile());
        }
        
        return walletDto;
    }

    @Override
    public TableDataInfo<OmindBalanceFlowDto> getBalanceFlowList(OmindBalanceFlowDto balanceFlowDto, PageQuery pageQuery) {
        LambdaQueryWrapper<OmindBalanceFlowEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(balanceFlowDto.getUserId() != null, OmindBalanceFlowEntity::getUserId, balanceFlowDto.getUserId())
            .eq(balanceFlowDto.getFlowType() != null, OmindBalanceFlowEntity::getFlowType, balanceFlowDto.getFlowType())
            .ge(balanceFlowDto.getBeginTime() != null, OmindBalanceFlowEntity::getCreateTime, balanceFlowDto.getBeginTime())
            .le(balanceFlowDto.getEndTime() != null, OmindBalanceFlowEntity::getCreateTime, balanceFlowDto.getEndTime())
            .eq(OmindBalanceFlowEntity::getDelFlag, 0)
            .orderByDesc(OmindBalanceFlowEntity::getCreateTime);

        Page<OmindBalanceFlowEntity> page = balanceFlowEntityIService.page(pageQuery.build(), queryWrapper);
        List<OmindBalanceFlowDto> flowDtoList = BeanUtil.copyToList(page.getRecords(), OmindBalanceFlowDto.class);
        
        // 设置流水类型名称
        for (OmindBalanceFlowDto dto : flowDtoList) {
            if (dto.getFlowType() != null) {
                switch (dto.getFlowType()) {
                    case 1:
                        dto.setFlowTypeName("充值");
                        break;
                    case 2:
                        dto.setFlowTypeName("消费");
                        break;
                    case 3:
                        dto.setFlowTypeName("退款");
                        break;
                    default:
                        dto.setFlowTypeName("未知");
                }
            }
        }
        
        return TableDataInfo.build(flowDtoList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentResponseDto recharge(Long userId, Long packageId, String payChannel, String tradeType) {
        
        // 查询用户钱包，不存在则创建
        OmindWalletEntity walletEntity = getWalletByUserId(userId);
        if (walletEntity == null) {
            walletEntity = createWallet(userId);
        }
        
        // 查询充值套餐
        OmindRechargePackageDto packageDto = rechargePackageService.getRechargePackageById(packageId);
        if (packageDto == null) {
            throw new ServiceException("充值套餐不存在");
        }
        
        // 使用套餐金额
        BigDecimal rechargeMoney = packageDto.getRechargeMoney();
        BigDecimal giftMoney = packageDto.getGiftMoney();
        
        // 生成订单号
        String outTradeNo = generateOrderNo();
        
        // 创建充值订单
        OmindRechargeOrderEntity orderEntity = new OmindRechargeOrderEntity();
        orderEntity.setUserId(userId);
        orderEntity.setPackageId(packageId);
        orderEntity.setOutTradeNo(outTradeNo);
        orderEntity.setRechargeMoney(rechargeMoney);
        orderEntity.setGiftMoney(giftMoney);
        orderEntity.setPayChannel(payChannel);
        orderEntity.setTradeType(tradeType);
        orderEntity.setStatus(0); // 待支付
        orderEntity.setRefundStatus(0); // 未退款
        orderEntity.setRefundAmount(BigDecimal.ZERO);
        orderEntity.setDelFlag(0);
        
        boolean result = rechargeOrderEntityIService.save(orderEntity);
        if (!result) {
            throw new ServiceException("创建充值订单失败");
        }
        PaymentResponseDto paymentResponse = null;

        boolean mock = true;
        if (!mock) {
            // 根据支付渠道和交易类型选择合适的支付服务
            PaymentService matchedPaymentService = paymentServices.stream()
                    .filter(service -> service.supports(payChannel, tradeType))
                    .findFirst()
                    .orElseThrow(() -> new ServiceException("不支持的支付渠道或交易类型: " + payChannel + ", " + tradeType));

            // 创建支付并获取响应
            paymentResponse = matchedPaymentService.createPaymentWithResponse(
                    userId,
                    rechargeMoney,
                    "充值订单：" + outTradeNo,
                    outTradeNo
            );
        }

        return paymentResponse;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean handleRechargeSuccess(String outTradeNo, String transactionId) {
        // 查询订单
        LambdaQueryWrapper<OmindRechargeOrderEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(OmindRechargeOrderEntity::getOutTradeNo, outTradeNo)
            .eq(OmindRechargeOrderEntity::getDelFlag, 0);
        
        // 验证订单信息
        OmindRechargeOrderEntity orderEntity = rechargeOrderEntityIService.getOne(queryWrapper);
        if (orderEntity == null) {
            throw new ServiceException("充值订单不存在");
        }
        
        if (orderEntity.getStatus() == 1) {
            // 已经处理过了，直接返回成功
            return true;
        }
        
        if (orderEntity.getStatus() != 0) {
            throw new ServiceException("充值订单状态异常");
        }
        
        // 更新订单状态
        OmindRechargeOrderEntity updateOrder = new OmindRechargeOrderEntity();
        updateOrder.setId(orderEntity.getId());
        updateOrder.setStatus(1); // 支付成功
        updateOrder.setTransactionId(transactionId);
        updateOrder.setPayTime(new Date());
        
        boolean orderResult = rechargeOrderEntityIService.updateById(updateOrder);
        if (!orderResult) {
            throw new ServiceException("更新充值订单失败");
        }
        
        // 查询用户钱包
        OmindWalletEntity walletEntity = getWalletByUserId(orderEntity.getUserId());
        if (walletEntity == null) {
            walletEntity = createWallet(orderEntity.getUserId());
        }
        
        // 计算金额变动
        BigDecimal rechargeMoney = orderEntity.getRechargeMoney();
        BigDecimal giftMoney = orderEntity.getGiftMoney();
        BigDecimal totalAmount = rechargeMoney.add(giftMoney);

        // 计算新的余额
        BigDecimal newBalance = walletEntity.getBalance().add(totalAmount);
        BigDecimal newRechargeBalance = walletEntity.getRechargeBalance().add(rechargeMoney);
        BigDecimal newTotalRecharge = walletEntity.getTotalRecharge().add(rechargeMoney);
        
        // 更新钱包余额
        OmindWalletEntity updateWallet = new OmindWalletEntity();
        updateWallet.setId(walletEntity.getId());
        updateWallet.setBalance(newBalance);
        updateWallet.setRechargeBalance(newRechargeBalance);
        updateWallet.setTotalRecharge(newTotalRecharge);
        updateWallet.setVersion(walletEntity.getVersion()); // 乐观锁版本号
        
        boolean walletResult = walletEntityIService.updateById(updateWallet);
        if (!walletResult) {
            throw new ServiceException("更新钱包余额失败");
        }
        
        // 创建余额流水记录
        OmindBalanceFlowEntity flowEntity = new OmindBalanceFlowEntity();
        flowEntity.setUserId(orderEntity.getUserId());
        flowEntity.setFlowType(1); // 充值
        flowEntity.setAmount(rechargeMoney);
        flowEntity.setGiftAmount(giftMoney);
        flowEntity.setStatus(1); // 成功

        // 设置交易前后余额
        flowEntity.setBalanceBefore(walletEntity.getBalance());
        flowEntity.setBalanceAfter(newBalance);
        flowEntity.setAvailableBefore(walletEntity.getRechargeBalance());
        flowEntity.setAvailableAfter(newRechargeBalance);

        flowEntity.setTransactionNo(IdUtil.simpleUUID()); // 内部流水号
        flowEntity.setOutTradeNo(outTradeNo); // 商户订单号
        flowEntity.setRelatedId(orderEntity.getId().toString()); // 关联订单ID
        flowEntity.setRemark("充值" + rechargeMoney + "元，赠送" + giftMoney + "元");
        flowEntity.setDelFlag(0);
        
        boolean flowResult = balanceFlowEntityIService.save(flowEntity);
        if (!flowResult) {
            throw new ServiceException("创建余额流水记录失败");
        }
        
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean consume(Long userId, BigDecimal amount, String relatedId, String remark) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ServiceException("消费金额必须大于0");
        }
        
        // 查询用户钱包
        OmindWalletEntity walletEntity = getWalletByUserId(userId);
        if (walletEntity == null) {
            throw new ServiceException("用户钱包不存在");
        }
        
        // 检查余额是否充足
        if (walletEntity.getBalance().compareTo(amount) < 0) {
            throw new ServiceException("余额不足");
        }

        // 计算新的余额
        BigDecimal newBalance = walletEntity.getBalance().subtract(amount);
        BigDecimal newRechargeBalance = walletEntity.getRechargeBalance().subtract(amount);
        BigDecimal newTotalConsume = walletEntity.getTotalConsumed().add(amount);
        BigDecimal newTotalRechargeConsumed = walletEntity.getTotalRechargeConsumed().add(amount);
        
        // 更新钱包余额
        OmindWalletEntity updateWallet = new OmindWalletEntity();
        updateWallet.setId(walletEntity.getId());
        updateWallet.setBalance(newBalance);
        updateWallet.setRechargeBalance(newRechargeBalance);
        updateWallet.setTotalConsumed(newTotalConsume);
        updateWallet.setTotalRechargeConsumed(newTotalRechargeConsumed);
        
        boolean walletResult = walletEntityIService.updateById(updateWallet);
        if (!walletResult) {
            throw new ServiceException("更新钱包余额失败");
        }
        
        // 创建余额流水记录
        OmindBalanceFlowEntity flowEntity = new OmindBalanceFlowEntity();
        flowEntity.setUserId(userId);
        flowEntity.setFlowType(2); // 消费
        flowEntity.setAmount(amount);
        flowEntity.setStatus(1); // 成功

        // 设置交易前后余额
        flowEntity.setBalanceBefore(walletEntity.getBalance());
        flowEntity.setBalanceAfter(newBalance);
        flowEntity.setAvailableBefore(walletEntity.getRechargeBalance());
        flowEntity.setAvailableAfter(newRechargeBalance);
        flowEntity.setTransactionNo(IdUtil.simpleUUID()); // 内部流水号
        flowEntity.setRelatedId(relatedId); // 关联ID
        flowEntity.setRemark(remark);
        flowEntity.setDelFlag(0);
        
        boolean flowResult = balanceFlowEntityIService.save(flowEntity);
        if (!flowResult) {
            throw new ServiceException("创建余额流水记录失败");
        }
        
        return true;
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
     * 创建用户钱包
     */
    private OmindWalletEntity createWallet(Long userId) {
        OmindWalletEntity walletEntity = new OmindWalletEntity();
        walletEntity.setUserId(userId);
        walletEntity.setBalance(BigDecimal.ZERO);
        walletEntity.setRechargeBalance(BigDecimal.ZERO);
        walletEntity.setFrozenBalance(BigDecimal.ZERO);
        walletEntity.setFrozenRechargeBalance(BigDecimal.ZERO);
        walletEntity.setTotalRecharge(BigDecimal.ZERO);
        walletEntity.setTotalConsumed(BigDecimal.ZERO);
        walletEntity.setTotalRechargeConsumed(BigDecimal.ZERO);
        walletEntity.setStatus(1); // 启用
        walletEntity.setVersion(0); // 初始版本号
        walletEntity.setDelFlag(0);
        
        boolean result = walletEntityIService.save(walletEntity);
        if (!result) {
            throw new ServiceException("创建用户钱包失败");
        }
        
        return walletEntity;
    }
    
    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        return DateUtil.format(new Date(), "yyyyMMddHHmmss") + IdUtil.fastSimpleUUID().substring(0, 6);
    }
} 
