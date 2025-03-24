package org.dromara.omind.tcpplat.netty.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.omind.tcpplat.netty.config.NettyConfig;
import org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;
import org.dromara.omind.tcpplat.netty.protocol.model.message.down.LoginResp02;
import org.dromara.omind.tcpplat.netty.service.ChannelManager;
import org.dromara.omind.tcpplat.netty.service.IDeviceDataService;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

import static org.dromara.omind.tcpplat.netty.common.constant.ProtocolConstant.*;

@Slf4j
@Component
@ChannelHandler.Sharable
@RequiredArgsConstructor
public class UpMessageChannelHandler extends SimpleChannelInboundHandler<Message> {

    private final IDeviceDataService deviceDataService;
    private final ChannelManager channelManager;
    private final NettyConfig nettyConfig;

    @Override
    public boolean acceptInboundMessage(Object msg) throws Exception {
        return super.acceptInboundMessage(msg) && msg.getClass().getAnnotation(MessageType.class).direction() == MessageType.Direction.UP;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Message msg) {
        CompletableFuture<Message> future = channelManager.getFuture(msg.getSequenceId());
        if (future != null) {
            future.complete(msg);
            return;
        }

        Message resp = deviceDataService.handle(msg);
        if (resp == NO_RESP) {
            return;
        }

        resp.setSequenceId(msg.getSequenceId());
        String pileCode = msg.getPileCode();
        resp.setPileCode(pileCode);

        if (resp instanceof LoginResp02) {
            handleLoginResp(ctx, (LoginResp02) resp, pileCode);
            return;
        }
        ctx.writeAndFlush(resp);
    }

    private void handleLoginResp(ChannelHandlerContext ctx, LoginResp02 resp, String pileCode) {
        ctx.writeAndFlush(resp);
        if (resp.isSuccess()) {
            ctx.channel().attr(AUTHENTICATED).set(true);
            ctx.channel().attr(SEQUENCE_ID).set(-1);
            ctx.channel().attr(PILE_CODE).set(pileCode);
            channelManager.bind(pileCode, ctx.channel());
        } else {
            ctx.channel().close();
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            if (e.state() == IdleState.READER_IDLE) {
                log.info("{}秒未收到心跳，关闭连接：ip：{}，桩编号：{}",
                        nettyConfig.getReaderIdleTime(),
                        ctx.channel().remoteAddress(), 
                        ctx.channel().attr(PILE_CODE).get());
                ctx.channel().close();
            }
        }
    }
}
