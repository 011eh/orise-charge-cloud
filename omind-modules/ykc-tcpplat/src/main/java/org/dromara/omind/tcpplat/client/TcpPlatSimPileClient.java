package org.dromara.omind.tcpplat.client;

import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.dromara.common.core.exception.base.BaseException;
import org.dromara.omind.baseplat.api.domain.entity.SysChargeOrder;

@Log4j2
@Data
public class TcpPlatSimPileClient implements PileIClient {

    @Override
    public void link() throws BaseException {

    }

    @Override
    public void unlink() throws BaseException {

    }

    @Override
    public void startCharge(SysChargeOrder sysChargeOrder) throws BaseException {

    }

    @Override
    public void stopCharge(int type, SysChargeOrder sysChargeOrder) throws BaseException {

    }

    @Override
    public void sendRealTimeData(Boolean sendForce) throws BaseException {

    }

    @Override
    public void sendTradeInfo(int stopType, String startChargeSeq) throws BaseException {

    }
} 
