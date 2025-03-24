package org.dromara.omind.tcpplat.netty.common.util;

public class HexUtil {
    public static String toHexWithPrefix(int num) {
        return String.format("0x%02X", num & 0xFF);
    }
}
