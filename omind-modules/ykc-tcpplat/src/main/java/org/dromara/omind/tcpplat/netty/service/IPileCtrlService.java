package org.dromara.omind.tcpplat.netty.service;

import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;

public interface IPileCtrlService {
    Message ctrl12(String pileCode);

    Message ctrl34(String pileCode);

    Message ctrl36(String pileCode);
}
