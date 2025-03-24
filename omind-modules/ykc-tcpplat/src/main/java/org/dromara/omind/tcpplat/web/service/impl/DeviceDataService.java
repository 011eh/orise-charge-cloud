package org.dromara.omind.tcpplat.web.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;
import org.dromara.omind.tcpplat.netty.service.IDeviceDataService;
import org.dromara.omind.tcpplat.web.handler.IMsgHandler;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.ParameterizedType;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DeviceDataService implements IDeviceDataService {

    private final IMsgHandler<Message> defaultMsgHandler;
    private Map<Class<? extends Message>, IMsgHandler<? extends Message>> handlerMap;

    @Autowired
    @SuppressWarnings("unchecked")
    public void initHandlerMap(ObjectProvider<IMsgHandler<? extends Message>> handlers) {
        handlerMap = handlers.stream().collect(Collectors.toMap(
                p -> (Class<? extends Message>) ((ParameterizedType) p.getClass().getGenericInterfaces()[0]).getActualTypeArguments()[0],
                Function.identity()));
    }

    @Override
    @SuppressWarnings("unchecked")
    public Message handle(Message msg) {
        IMsgHandler<Message> handler = (IMsgHandler<Message>) handlerMap.getOrDefault(msg.getClass(), defaultMsgHandler);
        return handler.handler(msg);
    }
}
