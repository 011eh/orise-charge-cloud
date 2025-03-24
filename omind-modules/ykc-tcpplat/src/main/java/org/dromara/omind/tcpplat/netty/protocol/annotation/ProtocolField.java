package org.dromara.omind.tcpplat.netty.protocol.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static org.dromara.omind.tcpplat.netty.protocol.annotation.ProtocolField.ByteOrder.LITTLE_ENDIAN;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ProtocolField {
    int length() default -1;

    int index() default Integer.MAX_VALUE;

    String charset() default "UTF-8";

    DataType type();

    int scale() default 1;

    ByteOrder BYTE_ORDER() default LITTLE_ENDIAN;

    Option[] options() default {};

    int listSize() default -1;

    int offset() default 0;

    int bitOffset() default -1;

    int bitLength() default -1;

    enum ByteOrder {
        BIG_ENDIAN,
        LITTLE_ENDIAN
    }

    enum DataType {
        INT8,
        UINT8,
        INT16,
        UINT16,
        INT32,
        UINT32,
        FLOAT,
        DOUBLE,
        BIN,
        STRING,
        BYTES,
        
        // 解码可以用
        BIT,
        
        // 任意字节长度的数字
        ARBITRARY_INT
    }                           

    @interface Option {
        int code();

        String desc();
    }
} 
