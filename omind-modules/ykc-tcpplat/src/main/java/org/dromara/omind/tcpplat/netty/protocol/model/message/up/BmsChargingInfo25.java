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
@MessageType(type = 0x25, direction = UP, desc = "GBT-27930充电桩与BMS充电过程BMS信息")
public class BmsChargingInfo25 extends Message {

    @ProtocolField(type = DataType.BIN, index = 1, length = 16)
    private String transactionSeqNo;

    @ProtocolField(type = DataType.BIN, length = 1)
    private String gunCode;
    
    // 1/位，1偏移量；数据范围：1~256
    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer bmsHighestCellVoltageNumber;
    
    // 1ºC/位，-50ºC偏移量；数据范围：-50ºC~+200ºC
    @ProtocolField(type = DataType.UINT8, offset = -50, length = 1)
    private Integer bmsHighestTemperature;
    
    // 1/位，1偏移量；数据范围：1~128
    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer highestTemperaturePointNumber;
    
    // 1ºC/位，-50ºC偏移量；数据范围：-50ºC~+200ºC
    @ProtocolField(type = DataType.UINT8, offset = -50, length = 1)
    private Integer bmsLowestTemperature;
    
    // 1/位，1偏移量；数据范围：1~128
    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer lowestTemperaturePointNumber;
    
    // Byte 1, bits 0-1 (<00>:=正常; <01>:=过高; <10>:=过低)
    @ProtocolField(type = DataType.BIT, bitOffset = 0, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "过高"),
        @ProtocolField.Option(code = 0x02, desc = "过低")
    })
    private Integer bmsCellVoltageStatus;
    
    // Byte 1, bits 2-3 (<00>:=正常; <01>:=过高; <10>:=过低)
    @ProtocolField(type = DataType.BIT, bitOffset = 2, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "过高"),
        @ProtocolField.Option(code = 0x02, desc = "过低")
    })
    private Integer bmsSocStatus;
    
    // Byte 1, bits 4-5 (<00>:=正常; <01>:=过流; <10>:=不可信状态)
    @ProtocolField(type = DataType.BIT, bitOffset = 4, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "过流"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer bmsCurrentStatus;
    
    // Byte 1, bits 6-7 (<00>:=正常; <01>:=过高; <10>:=不可信状态)
    @ProtocolField(type = DataType.BIT, bitOffset = 6, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "过高"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer bmsTemperatureStatus;
    
    // Byte 2, bits 0-1 (<00>:=正常; <01>:=故障; <10>:=不可信状态)
    @ProtocolField(type = DataType.BIT, bitOffset = 0, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "故障"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer bmsInsulationStatus;
    
    // Byte 2, bits 2-3 (<00>:=正常; <01>:=故障; <10>:=不可信状态)
    @ProtocolField(type = DataType.BIT, bitOffset = 2, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "故障"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer bmsConnectorStatus;
    
    // Byte 2, bits 4-5 (<00>:=禁止; <01>:=允许)
    @ProtocolField(type = DataType.BIT, bitOffset = 4, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "禁止"),
        @ProtocolField.Option(code = 0x01, desc = "允许")
    })
    private Integer chargeProhibitStatus;
    
    // Byte 2, bits 6-7 (预留位)
    @ProtocolField(type = DataType.BIT, bitOffset = 6, bitLength = 2)
    private Integer reserved;
} 
