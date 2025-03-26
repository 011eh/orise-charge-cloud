package org.dromara.omind.tcpplat.utils;

import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class TradeNoGenerator {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    private static int seq;

    public String getTradeNo(String connectorId) {
        String time = dateFormat.format(new Date());
        return connectorId + time + String.format("%02d", getSeq());
    }

    private synchronized int getSeq() {
        if (seq++ > 99) {
            seq = 0;
        }
        return seq;
    }
}
