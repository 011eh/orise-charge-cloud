package org.dromara.omind.tcpplat.netty.protocol.model.message.down;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField.DataType;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;

import static org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType.Direction.DOWN;

@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
@MessageType(type = 0x34, direction = DOWN, desc = "运营平台远程控制启机")
public class RemoteStartCharge34 extends Message {

    @ProtocolField(type = DataType.BIN, index = 1, length = 16)
    private String transactionSeqNo;

    @ProtocolField(type = DataType.BIN, length = 1)
    private String gunCode;

    @ProtocolField(type = DataType.BIN, length = 8)
    private String logicalCardNo;

    @ProtocolField(type = DataType.BIN, length = 8)
    private String physicalCardNo;

    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer accountBalance;
}
