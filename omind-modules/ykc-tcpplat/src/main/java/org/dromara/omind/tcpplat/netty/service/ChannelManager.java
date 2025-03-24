package org.dromara.omind.tcpplat.netty.service;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.*;

import static org.dromara.omind.tcpplat.netty.common.constant.ProtocolConstant.SEQUENCE_ID;

@Slf4j
@Component
public class ChannelManager {

    private final Map<String, Channel> channelMap = new ConcurrentHashMap<>();
    private final Map<Integer, CompletableFuture<Message>> responseFutures = new ConcurrentHashMap<>();

    public void bind(String pileCode, Channel channel) {
        channel.closeFuture().addListener((future) -> channelMap.remove(pileCode));
        channelMap.put(pileCode, channel);
    }

    public Channel get(String pileCode) {
        return channelMap.get(pileCode);
    }

    public int getSequenceId(String pileCode) {
        Channel channel = channelMap.get(pileCode);
        synchronized (channel) {
            Attribute<Integer> sequenceId = channel.attr(SEQUENCE_ID);
            Integer id = sequenceId.get();
            if (id++ > 0xffff) {
                id = 0;
            }
            sequenceId.set(id);
            return id;
        }
    }

    public Message writeWithResponse(String pileCode, Message message) {
        Channel channel = channelMap.get(pileCode);
        if (channel == null) {
            throw new RuntimeException("未找到 Channel，桩编号为：" + pileCode);
        }

        int sequenceId = getSequenceId(pileCode);
        message.setSequenceId(sequenceId);
        message.setPileCode(pileCode);
        
        CompletableFuture<Message> future = new CompletableFuture<>();
        responseFutures.put(sequenceId, future);

        channel.writeAndFlush(message);

        try {
            return future.get(1, TimeUnit.MINUTES);
        } catch (TimeoutException e) {
            responseFutures.remove(sequenceId);
            throw new RuntimeException(String.format("响应超时，桩编号：%s,序列号：%s，消息：%s", pileCode, sequenceId, message));
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    public CompletableFuture<Message> getFuture(int sequenceId) {
        return responseFutures.remove(sequenceId);
    }
}
