package org.dromara.omind.tcpplat.netty.protocol.codec;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.dromara.omind.tcpplat.netty.common.constant.ProtocolConstant;

public class DataFrameDecoder extends LengthFieldBasedFrameDecoder {

    public DataFrameDecoder() {
        // lengthFieldOffset: 长度字段的偏移量(起始标志后1字节)
        // lengthFieldLength: 长度字段占用的字节数
        // lengthAdjustment: 长度调整值
        // initialBytesToStrip: 跳过的字节数
        super(ProtocolConstant.MAX_DATA_FRAME_LENGTH, 1, 1, 2, 0);
    }
} 
