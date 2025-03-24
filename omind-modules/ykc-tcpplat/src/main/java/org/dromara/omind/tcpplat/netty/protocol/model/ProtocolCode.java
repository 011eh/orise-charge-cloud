package org.dromara.omind.tcpplat.netty.protocol.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class ProtocolCode {

    private Integer code;

    private String desc;

    public ProtocolCode(Integer code) {
        this.code = code;
    }

    public ProtocolCode(Boolean code) {

        // 业务正常情况 true 返回 0
        this.code = code ? 0 : 1;
    }

    public ProtocolCode(Integer code, String desc) {
        this.desc = desc;
        this.code = code;
    }
}
