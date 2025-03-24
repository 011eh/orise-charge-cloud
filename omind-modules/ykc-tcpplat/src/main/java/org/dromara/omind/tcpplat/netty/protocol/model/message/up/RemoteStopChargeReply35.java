package org.dromara.omind.tcpplat.netty.protocol.model.message.up;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField.DataType;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;

import static org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType.Direction.UP;

@Getter
@Setter
@ToString(callSuper = true)
@MessageType(type = 0x35, direction = UP, desc = "远程停止充电命令回复")
public class RemoteStopChargeReply35 extends Message {

    @ProtocolField(type = DataType.BIN, length = 1)
    private String gunCode;

    @ProtocolField(type = DataType.UINT8, options = {
            @ProtocolField.Option(code = 0x00, desc = "失败"),
            @ProtocolField.Option(code = 0x01, desc = "成功")
    })
    private Integer stopResult;

    @ProtocolField(type = DataType.UINT8, options = {
            @ProtocolField.Option(code = 0x00, desc = "无"),
            @ProtocolField.Option(code = 0x01, desc = "设备编号不匹配"),
            @ProtocolField.Option(code = 0x02, desc = "枪未处于充电状态"),
            @ProtocolField.Option(code = 0x03, desc = "其他")
    })
    private Integer failReason;
} 
