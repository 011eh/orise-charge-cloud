package org.dromara.omind.tcpplat.web.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.omind.mq.api.producer.HeartBeatProducer;
import org.dromara.omind.tcpplat.netty.common.constant.UpMsgType;
import org.dromara.omind.tcpplat.netty.protocol.annotation.MessageType;
import org.dromara.omind.tcpplat.netty.protocol.model.ProtocolCode;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;
import org.dromara.omind.tcpplat.netty.protocol.model.message.down.HeartbeatResp04;
import org.dromara.omind.tcpplat.netty.protocol.model.message.down.LoginResp02;
import org.dromara.omind.tcpplat.netty.protocol.model.message.down.PriceModelResp0A;
import org.dromara.omind.tcpplat.netty.protocol.model.message.down.PriceRuleResp06;
import org.dromara.omind.tcpplat.netty.protocol.model.message.up.Heartbeat03;
import org.dromara.omind.tcpplat.netty.protocol.model.message.up.Login01;
import org.dromara.omind.tcpplat.netty.protocol.model.message.up.PriceRuleReq05;
import org.dromara.omind.tcpplat.netty.service.ChannelManager;
import org.dromara.omind.tcpplat.netty.service.IPileService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.dromara.omind.tcpplat.netty.common.constant.ProtocolConstant.GUN_SET;
import static org.dromara.omind.tcpplat.netty.common.constant.ProtocolConstant.NO_RESP;

@Slf4j
@Order
@Service
@RequiredArgsConstructor
public class DefaultMsgHandler implements IMsgHandler<Message> {

    private final IPileService pileService;
    private final HeartBeatProducer heartBeatProducer;
    private final ChannelManager channelManager;

    @Override
    public Message handler(Message message) {
        byte type = message.getClass().getAnnotation(MessageType.class).type();
        String pileCode = message.getPileCode();
        switch (type) {
            case UpMsgType.LOGIN_01:
                Login01 loginMsg = (Login01) message;
                boolean authenticated = pileService.authenticate(pileCode);
                LoginResp02 loginResp = new LoginResp02().setLoginResult(new ProtocolCode(authenticated));
                log.info("pileCode：{}, 认证结果：{}", pileCode, authenticated);
                return loginResp;

            case UpMsgType.HEARTBEAT_03:
                Heartbeat03 heartbeat = (Heartbeat03) message;
                String connectorId = heartbeat.getPileCode() + heartbeat.getGunCode();
                heartBeatProducer.sendMsg(connectorId);
                channelManager.get(pileCode).attr(GUN_SET).get().put(connectorId, System.currentTimeMillis());
                return new HeartbeatResp04().setGunCode(heartbeat.getGunCode());

            case UpMsgType.PRICE_REQ_05:
                PriceRuleReq05 msg = (PriceRuleReq05) message;
                boolean ruleConsistent = pileService.isRuleConsistent(msg.getPileCode(), msg.getPriceRuleCode());
                return new PriceRuleResp06()
                        .setPriceRuleNo(msg.getPriceRuleCode())
                        .setResult(new ProtocolCode(ruleConsistent));
            case UpMsgType.PRICE_MODEL_REQ_09:
                List<Integer> list = new ArrayList<>(48);
                for (int i = 0; i < 48; i++) {
                    list.add(i % 4 + 1);
                }
                return new PriceModelResp0A()
                        .setPriceRuleCode("1000")
                        .setPeakElectPrice(1234)
                        .setPeakServicePrice(2345)
                        .setHighElectPrice(3456)
                        .setHighServicePrice(4567)
                        .setNormalElectPrice(5678)
                        .setNormalServicePrice(6789)
                        .setValleyElectPrice(7890)
                        .setValleyServicePrice(8901)
                        .setLossRatio(9)
                        .setTimeSegmentPriceType(list);
            default:
                log.info(message.toString());
        }
        return NO_RESP;
    }
}
