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
@MessageType(type = 0x23, direction = UP, desc = "GBT-27930充电桩与BMS充电过程BMS需求、充电机输出")
public class BmsChargingData23 extends Message {

    @ProtocolField(type = DataType.BIN, index = 1, length = 16)
    private String transactionSeqNo;

    @ProtocolField(type = DataType.BIN, length = 1)
    private String gunCode;
    
    // 0.1V/位，0V偏移量
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsVoltageRequirement; 
    
    // 0.1A/位，-400A偏移量
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsCurrentRequirement; 
    
    // 0x01：恒压充电；0x02：恒流充电
    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer bmsChargeMode; 
    
    // 0.1V/位，0V偏移量
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsChargeVoltage; 
    
    // 0.1A/位，-400A偏移量
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsChargeCurrent; 
    
    // 1-12位:最高单体动力蓄电池电压,0.01V/位;13-16位:最高单体动力蓄电池电压所在组号
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsHighestCellVoltageAndGroup; 
    
    // 1%/位，0%偏移量；数据范围：0~100%
    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer bmsCurrentSoc; 
    
    // 1min/位，0min偏移量；数据范围：0~600min
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsEstimatedRemainingTime; 
    
    // 0.1V/位，0V偏移量
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer chargerOutputVoltage; 
    
    // 0.1A/位，-400A偏移量
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer chargerOutputCurrent; 
    
    // 1min/位，0min偏移量；数据范围：0~600min
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer totalChargingTime; 
} 
