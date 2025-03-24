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
@MessageType(type = 0x13, direction = UP, desc = "上传实时监测数据")
public class Status13 extends Message {

    @ProtocolField(type = DataType.BIN, index = 1, length = 16)
    private String trxSeqNo;

    @ProtocolField(type = DataType.BIN, length = 1)
    private String gunCode;

    @ProtocolField(type = DataType.UINT8, options = {
            @ProtocolField.Option(code = 0x00, desc = "离线"),
            @ProtocolField.Option(code = 0x01, desc = "故障"),
            @ProtocolField.Option(code = 0x02, desc = "空闲"),
            @ProtocolField.Option(code = 0x03, desc = "充电")
    })
    private Integer status;

    @ProtocolField(type = DataType.UINT8, options = {
            @ProtocolField.Option(code = 0x00, desc = "否"),
            @ProtocolField.Option(code = 0x01, desc = "是"),
            @ProtocolField.Option(code = 0x02, desc = "未知")
    })
    private Integer isGunReturned;

    @ProtocolField(type = DataType.UINT8, options = {
            @ProtocolField.Option(code = 0x00, desc = "否"),
            @ProtocolField.Option(code = 0x01, desc = "是")
    })
    private Integer isGunInserted;

    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer outputVoltage;

    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer outputCurrent;

    @ProtocolField(type = DataType.UINT8, offset = -50, length = 1)
    private Integer gunLineTemperature;

    @ProtocolField(type = DataType.BIN, length = 8)
    private String gunLineCode;

    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer soc;

    @ProtocolField(type = DataType.UINT8, offset = -50, length = 1)
    private Integer batteryMaxTemperature;

    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer cumulativeChargingTime;

    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer remainingTime;

    @ProtocolField(type = DataType.UINT32, length = 4)
    private Integer chargedEnergy;

    @ProtocolField(type = DataType.UINT32, length = 4)
    private Integer calculatedLossEnergy;

    @ProtocolField(type = DataType.UINT32, length = 4)
    private Integer chargedAmount;

    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer hardwareFault;
}
