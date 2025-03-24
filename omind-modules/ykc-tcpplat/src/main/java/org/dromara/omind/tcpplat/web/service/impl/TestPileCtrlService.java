package org.dromara.omind.tcpplat.web.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;
import org.dromara.omind.tcpplat.netty.protocol.model.message.down.GetStatus12;
import org.dromara.omind.tcpplat.netty.protocol.model.message.down.RemoteStartCharge34;
import org.dromara.omind.tcpplat.netty.protocol.model.message.down.RemoteStopCharge36;
import org.dromara.omind.tcpplat.netty.service.ChannelManager;
import org.dromara.omind.tcpplat.netty.service.IPileCtrlService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestPileCtrlService implements IPileCtrlService {

    private final ChannelManager channelManager;

    @Override
    public Message ctrl12(String pileCode) {
        Message message = channelManager.writeWithResponse(pileCode, new GetStatus12().setGunCode("1"));
        log.info(message.toString());
        return message;
    }

    @Override
    public Message ctrl34(String pileCode) {
        RemoteStartCharge34 message = new RemoteStartCharge34()
                .setTransactionSeqNo("55031412782305012018061914444680")
                .setGunCode("01")
                .setLogicalCardNo("1000000573")
                .setPhysicalCardNo("D14B0A54");
        message.setAccountBalance(new BigDecimal("1000.00").multiply(new BigDecimal(100)).intValue());
        return channelManager.writeWithResponse(pileCode, message);
    }

    @Override
    public Message ctrl36(String pileCode) {
        RemoteStopCharge36 message = new RemoteStopCharge36()
                .setGunCode("01");
        return channelManager.writeWithResponse(pileCode, message);
    }
}
