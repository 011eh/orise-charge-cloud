package org.dromara.omind.tcpplat.netty.service;

import io.netty.channel.Channel;
import io.netty.util.Attribute;
import lombok.extern.slf4j.Slf4j;
import org.dromara.omind.tcpplat.netty.exception.BusinessException;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static org.dromara.omind.tcpplat.netty.common.constant.ProtocolConstant.GUN_SET;
import static org.dromara.omind.tcpplat.netty.common.constant.ProtocolConstant.SEQUENCE_ID;

@Slf4j
@Component
public class ChannelManager {

    private final Map<String, Channel> channelMap = new ConcurrentHashMap<>();
    private final Map<Integer, CompletableFuture<Message>> responseFutures = new ConcurrentHashMap<>();

    @NotNull
    private static String getPileCode(String connectorID) {
        return connectorID.substring(0, connectorID.length() - 2);
    }

    public Channel get(String pileCode) {
        return channelMap.get(pileCode);
    }

    public void bind(String pileCode, Channel channel) {
        channel.closeFuture().addListener((future) -> channelMap.remove(pileCode));
        channel.attr(GUN_SET).set(new HashMap<>());
        channelMap.put(pileCode, channel);
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

    public Channel getOrThrow(String pileCode) {
        if (!channelMap.containsKey(pileCode)) {
            throw new BusinessException(String.format("未找到对应 Channel，变编号：%s", pileCode));
        }
        return get(pileCode);
    }

    public boolean isOnline(String connectorId) {
        Channel channel = get(getPileCode(connectorId));
        if (channel == null) {
            return false;
        }
        Long pingTime = channel.attr(GUN_SET).get().getOrDefault(connectorId, 0L);
        return System.currentTimeMillis() - pingTime <= 6000;
    }
}
