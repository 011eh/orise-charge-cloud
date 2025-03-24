package org.dromara.omind.tcpplat.netty.protocol.model.message.down;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField.DataType;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;

import java.util.List;

import static org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType.Direction.DOWN;

@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
@MessageType(type = 0x0A, direction = DOWN, desc = "计费模型请求应答")
public class PriceModelResp0A extends Message {
    
    // 价格数值为电费费率 * 100000，1.00000 -> 100000
    @ProtocolField(type = DataType.BIN, length = 2)
    private String priceRuleCode;

    @ProtocolField(type = DataType.UINT32, length = 4)
    private Integer peakElectPrice;

    @ProtocolField(type = DataType.UINT32, length = 4)
    private Integer peakServicePrice;

    @ProtocolField(type = DataType.UINT32, length = 4)
    private Integer highElectPrice;

    @ProtocolField(type = DataType.UINT32, length = 4)
    private Integer highServicePrice;

    @ProtocolField(type = DataType.UINT32, length = 4)
    private Integer normalElectPrice;

    @ProtocolField(type = DataType.UINT32, length = 4)
    private Integer normalServicePrice;

    @ProtocolField(type = DataType.UINT32, length = 4)
    private Integer valleyElectPrice;

    @ProtocolField(type = DataType.UINT32, length = 4)
    private Integer valleyServicePrice;

    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer lossRatio;

    @ProtocolField(type = DataType.UINT8, length = 1, listSize = 48, options = {
            @ProtocolField.Option(code = 0x00, desc = "尖费率"),
            @ProtocolField.Option(code = 0x01, desc = "峰费率"),
            @ProtocolField.Option(code = 0x02, desc = "平费率"),
            @ProtocolField.Option(code = 0x03, desc = "谷费率")
    })
    private List<Integer> timeSegmentPriceType;
}
