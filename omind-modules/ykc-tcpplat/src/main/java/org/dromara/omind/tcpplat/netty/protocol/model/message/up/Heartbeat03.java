package org.dromara.omind.tcpplat.netty.protocol.model.message.up;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField.DataType;
import org.dromara.omind.tcpplat.netty.protocol.model.ProtocolCode;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;

import static org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType.Direction.UP;

@Getter
@Setter
@ToString(callSuper = true)
@MessageType(type = 0x03, direction = UP, desc = "充电桩心跳包")
public class Heartbeat03 extends Message {

    @ProtocolField(type = DataType.BIN, length = 1)
    private String gunCode;

    @ProtocolField(type = DataType.INT8, length = 1, options = {
            @ProtocolField.Option(code = 0x00, desc = "正常"),
            @ProtocolField.Option(code = 0x01, desc = " 故障")
    })
    private ProtocolCode gunStatus;
}
