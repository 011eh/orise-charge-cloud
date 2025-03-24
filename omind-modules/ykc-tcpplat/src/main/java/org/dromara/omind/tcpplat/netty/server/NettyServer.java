package org.dromara.omind.tcpplat.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.omind.tcpplat.netty.config.NettyConfig;
import org.dromara.omind.tcpplat.netty.handler.MsgDuplexChannelHandler;
import org.dromara.omind.tcpplat.netty.handler.UpMessageChannelHandler;
import org.dromara.omind.tcpplat.netty.protocol.codec.DataFrameDecoder;
import org.dromara.omind.tcpplat.netty.protocol.codec.MessageCodec;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class NettyServer {

    private final NettyConfig nettyConfig;
    private final NioEventLoopGroup bossGroup;
    private final NioEventLoopGroup workerGroup;
    private final UpMessageChannelHandler upMessageChannelHandler;
    private final MessageCodec messageCodec;
    private final MsgDuplexChannelHandler msgDuplexChannelHandler;

    private ChannelFuture channelFuture;

    @PostConstruct
    public void start() {
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline()
                                    .addLast()
                                    .addLast(new IdleStateHandler(nettyConfig.getReaderIdleTime(), 0, 0, TimeUnit.SECONDS))
                                    .addLast(new DataFrameDecoder())
                                    .addLast(msgDuplexChannelHandler)
                                    .addLast(messageCodec)
                                    .addLast(msgDuplexChannelHandler)
                                    .addLast(upMessageChannelHandler)
                            ;
                        }
                    });

            int port = nettyConfig.getPort();
            channelFuture = serverBootstrap.bind(port).sync();
            log.info("Netty服务启动成功，监听端口：{}", port);
        } catch (InterruptedException e) {
            log.error("Netty服务启动失败", e);
            Thread.currentThread().interrupt();
        }
    }

    @PreDestroy
    public void stop() {
        if (channelFuture != null) {
            channelFuture.channel().close().awaitUninterruptibly();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        log.info("Netty服务已安全关闭");
    }

} 
