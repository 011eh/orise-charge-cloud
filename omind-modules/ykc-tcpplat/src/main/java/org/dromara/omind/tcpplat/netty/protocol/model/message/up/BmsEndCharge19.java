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
@MessageType(type = 0x19, direction = UP, desc = "GBT-27930充电桩与BMS充电结束阶段报文")
public class BmsEndCharge19 extends Message {

    @ProtocolField(type = DataType.BIN, index = 1, length = 16)
    private String transactionSeqNo;

    @ProtocolField(type = DataType.BIN, length = 1)
    private String gunCode;

    // 1%/位，0%偏移量；数据范围：0~100%
    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer bmsEndSOC; 

    // 0.01V/位，0V偏移量；数据范围：0~24V
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsMinCellVoltage; 

    // 0.01V/位，0V偏移量；数据范围：0~24V
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsMaxCellVoltage; 

    // 1ºC/位，-50ºC偏移量；数据范围：-50ºC~+200ºC
    @ProtocolField(type = DataType.UINT8, offset = -50, length = 1)
    private Integer bmsMinTemperature; 

    // 1ºC/位，-50ºC偏移量；数据范围：-50ºC~+200ºC
    @ProtocolField(type = DataType.UINT8, offset = -50, length = 1)
    private Integer bmsMaxTemperature; 

    // 1min/位，0min偏移量；数据范围：0~600min
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer pileTotalChargingTime; 

    // 0.1kWh/位，0kWh偏移量；数据范围：0~1000kWh
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer pileOutputEnergy; 

    // 充电机编号，1/位，1偏移量，数据范围：0～0xFFFFFFFF
    @ProtocolField(type = DataType.UINT32, length = 4)
    private Integer pileChargerNo; 
} 
