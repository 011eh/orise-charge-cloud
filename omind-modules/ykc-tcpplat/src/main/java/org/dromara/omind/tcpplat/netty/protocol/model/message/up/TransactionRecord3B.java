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
@MessageType(type = 0x3B, direction = UP, desc = "交易记录")
public class TransactionRecord3B extends Message {

    @ProtocolField(type = DataType.BIN, index = 1, length = 16)
    private String transactionSeqNo;

    @ProtocolField(type = DataType.BIN, length = 1)
    private String gunCode;

    @ProtocolField(type = DataType.ARBITRARY_INT, length = 7)
    private Long startTime;

    @ProtocolField(type = DataType.ARBITRARY_INT, length = 7)
    private Long endTime;

    // 精确到小数点后五位（尖电费+尖服务费）
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer peakPrice;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer peakEnergy;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer peakLossEnergy;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer peakAmount;

    // 精确到小数点后五位（峰电费+峰服务费）
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer highPrice;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer highEnergy;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer highLossEnergy;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer highAmount;

    // 精确到小数点后五位（平电费+平服务费）
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer normalPrice;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer normalEnergy;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer normalLossEnergy;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer normalAmount;

    // 精确到小数点后五位（谷电费+谷服务费）
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer valleyPrice;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer valleyEnergy;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer valleyLossEnergy;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer valleyAmount;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 5)
    private Long meterStartValue;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 5)
    private Long meterEndValue;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer totalEnergy;

    // 精确到小数点后四位
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer totalLossEnergy;

    // 精确到小数点后四位，包含电费、服务费
    @ProtocolField(type = DataType.ARBITRARY_INT, length = 4)
    private Integer totalAmount;

    // VIN码
    @ProtocolField(type = DataType.STRING, length = 17)
    private String vinCode;

    @ProtocolField(type = DataType.ARBITRARY_INT, length = 1, options = {
            @ProtocolField.Option(code = 0x01, desc = "app启动"),
            @ProtocolField.Option(code = 0x02, desc = "卡启动"),
            @ProtocolField.Option(code = 0x04, desc = "离线卡启动"),
            @ProtocolField.Option(code = 0x05, desc = "vin码启动充电")
    })
    private Integer transactionFlag;

    @ProtocolField(type = DataType.ARBITRARY_INT, length = 7)
    private Long transactionTime;

    @ProtocolField(type = DataType.ARBITRARY_INT, length = 1)
    private Integer stopReason;

    @ProtocolField(type = DataType.BIN, length = 8)
    private String physicalCardNo;
} 
