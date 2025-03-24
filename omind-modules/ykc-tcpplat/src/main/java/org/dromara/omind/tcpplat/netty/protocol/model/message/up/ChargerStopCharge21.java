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
@MessageType(type = 0x21, direction = UP, desc = "GBT-27930充电桩与BMS充电阶段充电机中止报文")
public class ChargerStopCharge21 extends Message {

    @ProtocolField(type = DataType.BIN, index = 1, length = 16)
    private String transactionSeqNo;

    @ProtocolField(type = DataType.BIN, length = 1)
    private String gunCode;
    
    // 1-2位:达到充电机设定的条件中止;3-4位:人工中止;5-6位:异常中止;7-8位:BMS主动中止
    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer chargerStopChargeReason; 
    
    // 如描述中各比特位的故障原因
    @ProtocolField(type = DataType.UINT16, length = 2)
    private Integer chargerStopChargeFaultReason; 
    
    // 1-2位:电流不匹配;3-4位:电压异常;5-8位:预留位
    @ProtocolField(type = DataType.UINT8, length = 1)
    private Integer chargerStopChargeErrorReason; 
} 
