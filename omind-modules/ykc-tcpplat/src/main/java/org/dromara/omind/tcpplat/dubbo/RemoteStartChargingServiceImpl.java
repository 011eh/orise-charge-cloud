package org.dromara.omind.tcpplat.dubbo;

import com.alibaba.fastjson.JSON;
import com.baomidou.lock.LockInfo;
import com.baomidou.lock.LockTemplate;
import com.baomidou.lock.executor.RedissonLockExecutor;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.common.core.exception.base.BaseException;
import org.dromara.common.json.utils.JsonUtils;
import org.dromara.omind.baseplat.api.constant.HlhtRedisKey;
import org.dromara.omind.baseplat.api.domain.PolicyInfoData;
import org.dromara.omind.baseplat.api.domain.entity.SysChargeOrder;
import org.dromara.omind.baseplat.api.domain.entity.SysConnector;
import org.dromara.omind.baseplat.api.domain.entity.SysOperator;
import org.dromara.omind.baseplat.api.domain.request.QueryStartChargeData;
import org.dromara.omind.baseplat.api.service.RemoteSysChargeOrderService;
import org.dromara.omind.baseplat.api.service.RemoteSysConnectorService;
import org.dromara.omind.baseplat.api.service.RemoteSysPriceService;
import org.dromara.omind.baseplat.api.service.pile.RemoteStartChargingServiceTcp;
import org.dromara.omind.tcpplat.netty.protocol.model.message.down.RemoteStartCharge34;
import org.dromara.omind.tcpplat.netty.protocol.model.message.up.RemoteStartChargeReply33;
import org.dromara.omind.tcpplat.netty.service.ChannelManager;
import org.dromara.omind.tcpplat.utils.TradeNoGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Log4j2
@DubboService
@Service
@RequiredArgsConstructor
public class RemoteStartChargingServiceImpl implements RemoteStartChargingServiceTcp {

    private final ChannelManager channelManager;
    private final TradeNoGenerator tradeNoGenerator;

    @DubboReference
    RemoteSysConnectorService remoteSysConnectorService;

    @DubboReference
    RemoteSysChargeOrderService remoteSysChargeOrderService;
    @DubboReference
    RemoteSysPriceService priceService;
    @Autowired
    private LockTemplate lockTemplate;


    @Override
    public int startCharging(SysOperator sysOperator, QueryStartChargeData queryStartChargeData) throws BaseException {
        String connectorId = queryStartChargeData.getConnectorID();
        if (!channelManager.isOnline(connectorId)) {
            return 2;
        }

        SysConnector sysConnector = remoteSysConnectorService.getConnectorById(connectorId);
        if (sysConnector == null || sysConnector.getState() != 0 || sysConnector.getStatus() != 2) {
            //getStatus状态 0离网 1空闲 2占用（未充电）3占用（充电）4占用（预约锁定）255故障
            //state充电桩状态 0正常 1故障
            return 3;
        }
        SysChargeOrder sysChargeOrder = new SysChargeOrder();
        String lockKey = HlhtRedisKey.LOCK_KEY_CHARGE_ORDER_INFO + queryStartChargeData.getStartChargeSeq();
        LockInfo lockInfo = null;
        try {
            lockInfo = lockTemplate.lock(lockKey, 3000L, 5000L, RedissonLockExecutor.class);

            sysChargeOrder.setStartChargeSeq(queryStartChargeData.getStartChargeSeq());
            sysChargeOrder.setOperatorId(sysOperator.getOperatorId());

            String tradeNo = tradeNoGenerator.getTradeNo(connectorId);
            sysChargeOrder.setTradeNo(tradeNo);
            sysChargeOrder.setConnectorId(connectorId);
            sysChargeOrder.setStartChargeSeqStat((short) 2);
            sysChargeOrder.setConnectorStatus(3);
            sysChargeOrder.setStartTime(new Date());
            sysChargeOrder.setSyncFlag((short) 0);
            sysChargeOrder.setReportGov((short) 0);
            sysChargeOrder.setPhoneNum(queryStartChargeData.getPhoneNum());
            sysChargeOrder.setPlateNum(queryStartChargeData.getPlateNum());
            List<PolicyInfoData> priceList = priceService.getConnectorPriceList(connectorId);
            if (priceList != null && !priceList.isEmpty()) {
                sysChargeOrder.setPriceInfo(JsonUtils.toJsonString(priceList));
            }
            log.info(JSON.toJSONString(sysChargeOrder));

            RemoteStartCharge34 charge34 = new RemoteStartCharge34();
            charge34.setTransactionSeqNo(tradeNo);
            charge34.setGunCode(connectorId.substring(connectorId.length() - 2));
            charge34.setLogicalCardNo("0000003511355988");
            charge34.setPhysicalCardNo("00000000D14B0A54");
            charge34.setAccountBalance(new BigDecimal("99.99").multiply(new BigDecimal(1000)).intValue());
            try {
                String pileCode = connectorId.substring(0, connectorId.length() - 2);
                RemoteStartChargeReply33 message = (RemoteStartChargeReply33) channelManager.writeWithResponse(pileCode, charge34);
                sysChargeOrder.setFailReason(message.getFailReason());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            remoteSysChargeOrderService.save(sysChargeOrder);
            log.info("启动订单创建成功：" + JSON.toJSONString(sysChargeOrder));
        } finally {
            if (lockInfo != null) {
                lockTemplate.releaseLock(lockInfo);
            }
        }
        return 0;

    }
}
