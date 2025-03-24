package org.dromara.omind.tcpplat.netty.protocol.model.message.up;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField.DataType;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;

import static org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType.Direction.UP;

@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
@MessageType(type = 0x33, direction = UP, desc = "远程启动充电命令回复")
public class RemoteStartChargeReply33 extends Message {

    @ProtocolField(type = DataType.BIN, index = 1, length = 16)
    private String transactionSeqNo;

    @ProtocolField(type = DataType.BIN, length = 1)
    private String gunCode;

    @ProtocolField(type = DataType.UINT8, options = {
            @ProtocolField.Option(code = 0x00, desc = "失败"),
            @ProtocolField.Option(code = 0x01, desc = "成功")
    })
    private Integer startResult;

    @ProtocolField(type = DataType.UINT8, options = {
            @ProtocolField.Option(code = 0x00, desc = "无"),
            @ProtocolField.Option(code = 0x01, desc = "设备编号不匹配"),
            @ProtocolField.Option(code = 0x02, desc = "枪已在充电"),
            @ProtocolField.Option(code = 0x03, desc = "设备故障"),
            @ProtocolField.Option(code = 0x04, desc = "设备离线"),
            @ProtocolField.Option(code = 0x05, desc = "未插枪")
    })
    private Integer failReason;
} 
