package org.dromara.omind.tcpplat.client;

import org.dromara.common.core.exception.base.BaseException;
import org.dromara.omind.baseplat.api.domain.entity.SysChargeOrder;

public interface PileIClient {

    /**
     * 充电桩-车插枪
     */
    void link() throws BaseException;

    /**
     * 充电桩-车拔枪
     */
    void unlink() throws BaseException;

    /**
     * 启动充电
     */
    void startCharge(SysChargeOrder sysChargeOrder) throws BaseException;

    /**
     * 停止充电
     */
    void stopCharge(int type, SysChargeOrder sysChargeOrder) throws BaseException;

    void sendRealTimeData(Boolean sendForce) throws BaseException;

    void sendTradeInfo(int stopType, String startChargeSeq) throws BaseException;

}
