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
@MessageType(type = 0x1B, direction = UP, desc = "GBT-27930充电桩与BMS充电错误报文")
public class BmsError1B extends Message {

    @ProtocolField(type = DataType.BIN, index = 1, length = 16)
    private String transactionSeqNo;

    @ProtocolField(type = DataType.BIN, length = 1)
    private String gunCode;
    
    // Byte 1, bits 0-1
    @ProtocolField(type = DataType.BIT, bitOffset = 0, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "超时"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer chargerIdentificationTimeoutFlag;
    
    // Byte 1, bits 2-3
    @ProtocolField(type = DataType.BIT, bitOffset = 2, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "超时"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer chargerIdentificationAaTimeoutFlag;
    
    // Byte 1, bits 4-7
    @ProtocolField(type = DataType.BIT, bitOffset = 4, bitLength = 4)
    private Integer reserved1;
    
    // Byte 2, bits 0-1
    @ProtocolField(type = DataType.BIT, bitOffset = 0, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "超时"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer chargerTimeSyncTimeoutFlag;
    
    // Byte 2, bits 2-3
    @ProtocolField(type = DataType.BIT, bitOffset = 2, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "超时"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer chargerReadyTimeoutFlag;
    
    // Byte 2, bits 4-7
    @ProtocolField(type = DataType.BIT, bitOffset = 4, bitLength = 4)
    private Integer reserved2;
    
    // Byte 3, bits 0-1
    @ProtocolField(type = DataType.BIT, bitOffset = 0, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "超时"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer chargerStatusTimeoutFlag;
    
    // Byte 3, bits 2-3
    @ProtocolField(type = DataType.BIT, bitOffset = 2, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "超时"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer chargerStopTimeoutFlag;
    
    // Byte 3, bits 4-7
    @ProtocolField(type = DataType.BIT, bitOffset = 4, bitLength = 4)
    private Integer reserved3;
    
    // Byte 4, bits 0-1
    @ProtocolField(type = DataType.BIT, bitOffset = 0, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "超时"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer chargerStatisticsTimeoutFlag;
    
    // Byte 4, bits 2-7
    @ProtocolField(type = DataType.BIT, bitOffset = 2, bitLength = 6)
    private Integer bmsOtherError;
    
    // Byte 5, bits 0-1
    @ProtocolField(type = DataType.BIT, bitOffset = 0, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "超时"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer bmsIdentificationTimeoutFlag;
    
    // Byte 5, bits 2-7
    @ProtocolField(type = DataType.BIT, bitOffset = 2, bitLength = 6)
    private Integer reserved4;
    
    // Byte 6, bits 0-1
    @ProtocolField(type = DataType.BIT, bitOffset = 0, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "超时"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer batteryParametersTimeoutFlag;
    
    // Byte 6, bits 2-3
    @ProtocolField(type = DataType.BIT, bitOffset = 2, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "超时"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer bmsReadyTimeoutFlag;
    
    // Byte 6, bits 4-7
    @ProtocolField(type = DataType.BIT, bitOffset = 4, bitLength = 4)
    private Integer reserved5;
    
    // Byte 7, bits 0-1
    @ProtocolField(type = DataType.BIT, bitOffset = 0, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "超时"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer batteryStatusTimeoutFlag;
    
    // Byte 7, bits 2-3
    @ProtocolField(type = DataType.BIT, bitOffset = 2, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "超时"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer batteryRequirementsTimeoutFlag;
    
    // Byte 7, bits 4-5
    @ProtocolField(type = DataType.BIT, bitOffset = 4, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "超时"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer bmsStopTimeoutFlag;
    
    // Byte 7, bits 6-7
    @ProtocolField(type = DataType.BIT, bitOffset = 6, bitLength = 2)
    private Integer reserved6;
    
    // Byte 8, bits 0-1
    @ProtocolField(type = DataType.BIT, bitOffset = 0, bitLength = 2, options = {
        @ProtocolField.Option(code = 0x00, desc = "正常"),
        @ProtocolField.Option(code = 0x01, desc = "超时"),
        @ProtocolField.Option(code = 0x02, desc = "不可信状态")
    })
    private Integer bmsStatisticsTimeoutFlag;
    
    // Byte 8, bits 2-7
    @ProtocolField(type = DataType.BIT, bitOffset = 2, bitLength = 6)
    private Integer chargerOtherError;
} 
