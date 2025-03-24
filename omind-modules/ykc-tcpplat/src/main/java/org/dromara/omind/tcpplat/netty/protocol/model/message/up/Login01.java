package org.dromara.omind.tcpplat.netty.protocol.model.message.up;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField.DataType;
import org.dromara.omind.tcpplat.netty.protocol.model.ProtocolCode;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;

import static org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType.Direction.UP;

@Getter
@Setter
@ToString(callSuper = true)
@MessageType(type = 0x01, direction = UP, desc = "充电桩登录认证")
public class Login01 extends Message {

    @ProtocolField(type = DataType.UINT8, options = {
            @ProtocolField.Option(code = 0x00, desc = "直流桩"),
            @ProtocolField.Option(code = 0x01, desc = "交流桩"),
    })
    private ProtocolCode pileType;

    @ProtocolField(type = DataType.UINT8)
    private Integer gunQuantity;

    @ProtocolField(type = DataType.UINT8)
    private Integer protocolVersion;

    @ProtocolField(type = DataType.STRING, length = 8)
    private String softwareVersion;

    @ProtocolField(type = DataType.UINT8, options = {
            @ProtocolField.Option(code = 0x00, desc = "SIM卡"),
            @ProtocolField.Option(code = 0x01, desc = "LAN"),
            @ProtocolField.Option(code = 0x02, desc = "WAN"),
            @ProtocolField.Option(code = 0x03, desc = "其他"),
    })
    private ProtocolCode networkType;

    @ProtocolField(type = DataType.BIN, length = 10)
    private String simCard;

    @ProtocolField(type = DataType.UINT8, options = {
            @ProtocolField.Option(code = 0x00, desc = "移动"),
            @ProtocolField.Option(code = 0x02, desc = "电信"),
            @ProtocolField.Option(code = 0x03, desc = "联通"),
            @ProtocolField.Option(code = 0x04, desc = "其他"),
    })
    private ProtocolCode operator;

    public String getProtocolVersion() {
        return "V" + (float) protocolVersion / 10;
    }
} 
