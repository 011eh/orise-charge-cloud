package org.dromara.omind.tcpplat.netty.service;

import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;

public interface IDeviceDataService {

    Message handle(Message msg);
}
