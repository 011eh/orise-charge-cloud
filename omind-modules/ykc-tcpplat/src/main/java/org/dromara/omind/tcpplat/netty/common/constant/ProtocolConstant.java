package org.dromara.omind.tcpplat.netty.common.constant;

import io.netty.util.AttributeKey;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;

import java.util.Map;

public class ProtocolConstant {
    // 起始标志
    public static final byte START_FLAG = 0x68;

    // 最大数据长度
    public static final int MAX_DATA_LENGTH = 200;

    // 最大帧长度 = 1(起始) + 1(长度) + 200(最大数据域) + 2(CRC) = 204
    public static final int MAX_DATA_FRAME_LENGTH = 204;

    // 不加密
    public static final byte ENCRYPT_NONE = 0x00;

    // 3DES加密
    public static final byte ENCRYPT_3DES = 0x01;

    public static final int SUCCESS = 0x00;
    public static final int FAIL = 0x00;

    public static final AttributeKey<Boolean> AUTHENTICATED = AttributeKey.valueOf("authenticated");
    public static final AttributeKey<String> PILE_CODE = AttributeKey.valueOf("pileCode");
    public static final AttributeKey<Integer> SEQUENCE_ID = AttributeKey.valueOf("sequenceId");
    public static final AttributeKey<Map<String, Long>> GUN_SET = AttributeKey.valueOf("gunMap");
    public static final AttributeKey<Boolean> IS_CHARGING = AttributeKey.valueOf("charging");
    public static final AttributeKey<String> TRADE_NO = AttributeKey.valueOf("tradeNo");

    public static final Message NO_RESP = new Message() {
    };

} 
