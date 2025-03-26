package org.dromara.omind.tcpplat.dubbo;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.dromara.common.core.exception.base.BaseException;
import org.dromara.omind.baseplat.api.domain.entity.SysChargeOrder;
import org.dromara.omind.baseplat.api.domain.entity.SysConnector;
import org.dromara.omind.baseplat.api.domain.entity.SysOperator;
import org.dromara.omind.baseplat.api.domain.request.QueryStopChargeData;
import org.dromara.omind.baseplat.api.service.RemoteSysChargeOrderService;
import org.dromara.omind.baseplat.api.service.RemoteSysConnectorService;
import org.dromara.omind.baseplat.api.service.pile.RemoteStopChargingServiceTcp;
import org.dromara.omind.tcpplat.netty.protocol.model.message.down.RemoteStopCharge36;
import org.dromara.omind.tcpplat.netty.protocol.model.message.up.RemoteStopChargeReply35;
import org.dromara.omind.tcpplat.netty.service.ChannelManager;
import org.springframework.stereotype.Service;

@Log4j2
@DubboService
@Service
@RequiredArgsConstructor
public class RemoteStopChargingServiceImpl implements RemoteStopChargingServiceTcp {

    private final ChannelManager channelManager;

    @DubboReference
    RemoteSysConnectorService remoteSysConnectorService;

    @DubboReference
    RemoteSysChargeOrderService remoteSysChargeOrderService;


    @Override
    public int stopCharging(SysOperator sysOperator, QueryStopChargeData queryStopChargeData) throws BaseException {

        SysConnector sysConnector = remoteSysConnectorService.getConnectorById(queryStopChargeData.getConnectorID());
        if (sysConnector == null) {
            return 1;
        }
        if(!channelManager.isOnline(queryStopChargeData.getConnectorID())){
            return 2;
        }
        SysChargeOrder sysChargeOrder = remoteSysChargeOrderService.getChargeOrderByStartChargeSeq(queryStopChargeData.getStartChargeSeq());
        if (sysChargeOrder == null || sysChargeOrder.getStartChargeSeqStat() >= 3) {
            return 2;
        }
        String connectorId = queryStopChargeData.getConnectorID();
        String pileCode = connectorId.substring(0, connectorId.length() - 2);
        String gunCode = connectorId.substring(connectorId.length() - 2);
        RemoteStopChargeReply35 message = (RemoteStopChargeReply35) channelManager.writeWithResponse(pileCode, new RemoteStopCharge36().setGunCode(gunCode));
        if (message.getStopResult() == 0) {
            log.error(message.getFailReason());
            return 2;
        }
//        simCenter.stopCharge(sysChargeOrder);
        return 0;
    }
}
