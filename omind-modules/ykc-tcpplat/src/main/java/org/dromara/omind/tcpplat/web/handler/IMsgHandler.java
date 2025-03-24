package org.dromara.omind.tcpplat.web.handler;

import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;

public interface IMsgHandler<T extends Message> {
    Message handler(T message);
}
