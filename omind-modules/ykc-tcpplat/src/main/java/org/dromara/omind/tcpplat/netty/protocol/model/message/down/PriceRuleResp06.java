package org.dromara.omind.tcpplat.netty.protocol.model.message.down;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField.DataType;
import org.dromara.omind.tcpplat.netty.protocol.model.ProtocolCode;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;

import static org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType.Direction.DOWN;

@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
@MessageType(type = 0x06, direction = DOWN, desc = "计费模型验证请求应答")
public class PriceRuleResp06 extends Message {

    @ProtocolField(type = DataType.BIN, length = 2)
    private String priceRuleNo;

    @ProtocolField(type = DataType.UINT8, length = 1, options = {
            @ProtocolField.Option(code = 0x00, desc = "桩计费模型与平台一致"),
            @ProtocolField.Option(code = 0x01, desc = "桩计费模型与平台不一致")
    })
    private ProtocolCode result;
}
