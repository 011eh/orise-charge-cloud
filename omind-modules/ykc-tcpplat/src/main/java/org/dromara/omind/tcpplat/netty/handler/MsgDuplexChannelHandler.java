package org.dromara.omind.tcpplat.netty.handler;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import lombok.extern.slf4j.Slf4j;
import org.dromara.omind.tcpplat.netty.protocol.model.MsgLogEntry;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@ChannelHandler.Sharable
public class MsgDuplexChannelHandler extends ChannelDuplexHandler {
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof MsgLogEntry) {
            log.info(msg.toString());
            return;
        }
        super.write(ctx, msg, promise);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof MsgLogEntry) {
            log.info(msg.toString());
            return;
        }
        super.channelRead(ctx, msg);
    }
}
