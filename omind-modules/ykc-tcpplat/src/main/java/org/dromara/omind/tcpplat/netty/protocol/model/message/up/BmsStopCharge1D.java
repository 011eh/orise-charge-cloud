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
@MessageType(type = 0x1D, direction = UP, desc = "GBT-27930充电桩与BMS充电阶段BMS中止报文")
public class BmsStopCharge1D extends Message {

    @ProtocolField(type = DataType.BIN, index = 1, length = 16)
    private String transactionSeqNo;

    @ProtocolField(type = DataType.BIN, length = 1)
    private String gunCode;
    
    // 1-2位:所需求的SOC目标值;3-4位:达到总电压的设定值;5-6位:达到单体电压设定值;7-8位:充电机主动中止
    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer bmsStopChargeReason; 
    
    // 如描述中各比特位的故障原因
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer bmsStopChargeFaultReason; 
    
    // 1-2位:电流过大;3-4位:电压异常;5-8位:预留位
    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer bmsStopChargeErrorReason; 
} 
