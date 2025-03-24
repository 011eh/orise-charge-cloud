package org.dromara.omind.tcpplat.netty.protocol.model.message;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField;

@Getter
@Setter
@ToString
public abstract class Message {

    protected int sequenceId;

    @ProtocolField(type = ProtocolField.DataType.BIN, length = 7)
    protected String pileCode;

} 
