package org.dromara.omind.tcpplat.netty.protocol.model.message.up;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField.DataType;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField.Option;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;

import static org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType.Direction.UP;

@Getter
@Setter
@ToString(callSuper = true)
@MessageType(type = 0x15, direction = UP, desc = "充电握手")
public class BmsHandshake15 extends Message {

    @ProtocolField(type = DataType.BIN, index = 1, length = 16)
    private String transactionSeqNo;

    @ProtocolField(type = DataType.BIN, length = 1)
    private String gunCode;

    @ProtocolField(type = DataType.ARBITRARY_INT, length = 3)
    private Integer bmsProtocolVersion;

    @ProtocolField(type = DataType.UINT8, options = {
            @Option(code = 0x01, desc = "铅酸电池"),
            @Option(code = 0x02, desc = "氢电池"),
            @Option(code = 0x03, desc = "磷酸铁锂电池"),
            @Option(code = 0x04, desc = "锰酸锂电池"),
            @Option(code = 0x05, desc = "钴酸锂电池"),
            @Option(code = 0x06, desc = "三元材料电池"),
            @Option(code = 0x07, desc = "聚合物锂离子电池"),
            @Option(code = 0x08, desc = "钛酸锂电池"),
            @Option(code = 0xFF, desc = "其他")
    })
    private Integer bmsBatteryType;

    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsRatedCapacity;

    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsRatedVoltage;

    @ProtocolField(type = DataType.STRING, length = 4)
    private String bmsManufacturerName;

    @ProtocolField(type = DataType.BIN, length = 4)
    private String bmsBatterySerialNo;

    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer bmsBatteryProductionYear;

    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer bmsBatteryProductionMonth;

    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer bmsBatteryProductionDay;

    @ProtocolField(type = DataType.ARBITRARY_INT, length = 3)
    private Integer bmsChargingCount;

    @ProtocolField(type = DataType.UINT8, options = {
            @Option(code = 0x00, desc = "租赁"),
            @Option(code = 0x01, desc = "车自有")
    })
    private Integer bmsOwnershipFlag;

    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer reserved;

    @ProtocolField(type = DataType.BIN, length = 17)
    private String bmsVehicleIdentificationCode;

    @ProtocolField(type = DataType.BIN, length = 8)
    private String bmsSoftwareVersion;
}
