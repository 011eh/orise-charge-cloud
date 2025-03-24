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
@MessageType(type = 0x17, direction = UP, desc = "GBT-27930充电桩与BMS参数配置阶段报文")
public class BmsSettingConfig17 extends Message {

    @ProtocolField(type = DataType.BIN, index = 1, length = 16)
    private String transactionSeqNo;

    @ProtocolField(type = DataType.BIN, length = 1)
    private String gunCode;

    // 0.01V/位，0V偏移量；数据范围：0~24V
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsMaxCellVoltage; 

    // 0.1A/位，-400A偏移量
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsMaxChargingCurrent; 

    // 0.1kWh/位，0kWh偏移量；数据范围：0~1000kWh
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsNominalTotalEnergy; 

    // 0.1V/位，0V偏移量
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsMaxChargingTotalVoltage; 

    // 1ºC/位，-50ºC偏移量；数据范围：-50ºC~+200ºC
    @ProtocolField(type = DataType.UINT8, offset = -50, length = 1)
    private Integer bmsMaxAllowedTemperature; 

    // 0.1%/位，0%偏移量；数据范围：0~100%
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsBatterySOC; 

    // 整车动力蓄电池总电压
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsBatteryVoltage; 

    // 0.1V/位，0V偏移量
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer pileMaxOutputVoltage; 

    // 0.1V/位，0V偏移量
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer pileMinOutputVoltage; 

    // 0.1A/位，-400A偏移量
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer pileMaxOutputCurrent; 

    // 0.1A/位，-400A偏移量
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer pileMinOutputCurrent; 
} 
