package org.dromara.omind.tcpplat.netty.protocol.model;

import lombok.Data;
import lombok.experimental.Accessors;
import org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;

@Data
@Accessors(chain = true)
public class MsgLogEntry {

    private String rawHex;
    private Message message;
    private long timestamp;
    private byte messageType;
    private MessageType.Direction direction;

    public MsgLogEntry(String rawHex, Message message) {
        this.rawHex = rawHex;
        this.message = message;
        timestamp = System.currentTimeMillis();
        MessageType annotation = message.getClass().getAnnotation(MessageType.class);
        messageType = annotation.type();
        direction = annotation.direction();
    }
}
