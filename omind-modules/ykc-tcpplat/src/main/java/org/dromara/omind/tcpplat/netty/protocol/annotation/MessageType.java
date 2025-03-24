package org.dromara.omind.tcpplat.netty.protocol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface MessageType {
    byte type();

    Direction direction();

    String desc() default "";

    enum Direction {
        UP,
        DOWN
    }
}
