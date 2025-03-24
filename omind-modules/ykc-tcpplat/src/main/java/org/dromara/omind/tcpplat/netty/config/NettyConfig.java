package org.dromara.omind.tcpplat.netty.config;

import io.netty.channel.nio.NioEventLoopGroup;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "ykc")
public class NettyConfig {
    private int port;
    private int bossThreads;
    private int workerThreads;
    private int readerIdleTime = 30;

    @Bean
    public NioEventLoopGroup bossGroup() {
        return new NioEventLoopGroup(bossThreads);
    }

    @Bean
    public NioEventLoopGroup workerGroup() {
        return new NioEventLoopGroup(workerThreads);
    }
} 
