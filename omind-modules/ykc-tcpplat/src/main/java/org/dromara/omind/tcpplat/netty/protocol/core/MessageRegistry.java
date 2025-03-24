package org.dromara.omind.tcpplat.netty.protocol.core;

import org.dromara.omind.tcpplat.netty.exception.BusinessException;
import org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;
import org.reflections.Reflections;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MessageRegistry {
    private static final Map<Byte, Class<? extends Message>> MESSAGE_TYPE_MAP = new HashMap<>();
    public static Reflections reflections;
    

    static {
        scanMessages("com.o11eh.ykc.netty.protocol.model.message");
    }

    private static void scanMessages(String basePackage) {
        reflections = new Reflections(basePackage);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(MessageType.class);
        classes.stream()
                .filter(clazz ->
                        Message.class.isAssignableFrom(clazz)
                                && clazz.isAnnotationPresent(MessageType.class)
                                && clazz.getAnnotation(MessageType.class).direction() == MessageType.Direction.UP)
                .forEach(clazz -> {
                    MessageType annotation = clazz.getAnnotation(MessageType.class);
                    byte type = annotation.type();
                    if (MESSAGE_TYPE_MAP.containsKey(type)) {
                        throw new BusinessException("消息编码重复: 0x" + Integer.toHexString(type));
                    }
                    MESSAGE_TYPE_MAP.put(type, (Class<? extends Message>) clazz);

                });
    }

    public static Class<? extends Message> getMessage(Byte type) {
        if (!MESSAGE_TYPE_MAP.containsKey(type)) {
            throw new BusinessException("Unknown message type: 0x" + Integer.toHexString(type));
        }
        return MESSAGE_TYPE_MAP.get(type);
    }
} 
