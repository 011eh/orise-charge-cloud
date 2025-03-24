package org.dromara.omind.tcpplat.netty.protocol.model.message.down;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.dromara.omind.tcpplat.netty.common.constant.ProtocolConstant;
import org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField.DataType;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField.Option;
import org.dromara.omind.tcpplat.netty.protocol.model.ProtocolCode;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;

import static org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType.Direction.DOWN;

@Getter
@Setter
@ToString(callSuper = true)
@Accessors(chain = true)
@MessageType(type = 0x02, direction = DOWN, desc = "登录认证应答")
public class LoginResp02 extends Message {

    @ProtocolField(type = DataType.UINT8, options = {
            @Option(code = 0x00, desc = "登录成功"),
            @Option(code = 0x01, desc = "登录失败")
    })
    private ProtocolCode loginResult;

    public boolean isSuccess() {
        return loginResult.getCode().equals(ProtocolConstant.SUCCESS);
    }
} 
