package org.dromara.omind.tcpplat.web.handler;

import cn.hutool.core.util.RandomUtil;
import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dromara.omind.baseplat.api.domain.entity.PlatConnectorRealtimeData;
import org.dromara.omind.mq.api.producer.RealtimeDataProducer;
import org.dromara.omind.tcpplat.netty.common.constant.ProtocolConstant;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;
import org.dromara.omind.tcpplat.netty.protocol.model.message.up.Status13;
import org.dromara.omind.tcpplat.netty.service.ChannelManager;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class RealTimeDataHandler implements IMsgHandler<Status13> {

    public static final AttributeKey<Status13> SIMULATION_LAST_DATA = AttributeKey.valueOf("simulationLastData");
    public static final AttributeKey<Long> SIMULATION_START_TIME = AttributeKey.valueOf("simulationStartTime");
    private final RealtimeDataProducer realtimeDataProducer;
    private final ChannelManager channelManager;

    @Override
    public Message handler(Status13 message) {
        Channel channel = channelManager.getOrThrow(message.getPileCode());
        if (message.getStatus() == 3) {
            String tradeNo = channelManager.get(message.getPileCode()).attr(ProtocolConstant.TRADE_NO).get();
            if (tradeNo != null) {
                message.setTrxSeqNo(tradeNo);
            }
            if (!channel.hasAttr(SIMULATION_LAST_DATA) || channel.attr(SIMULATION_LAST_DATA).get() == null) {
                channel.attr(SIMULATION_LAST_DATA).set(message);
                channel.attr(SIMULATION_START_TIME).set(System.currentTimeMillis());
            } else {
                message = channel.attr(SIMULATION_LAST_DATA).get();
                message.setOutputVoltage(RandomUtil.randomInt(2200));
                message.setOutputCurrent(RandomUtil.randomInt(4000));
                message.setBatteryMaxTemperature(RandomUtil.randomInt(-50, 250));
                message.setGunLineTemperature(RandomUtil.randomInt(-50, 250));
                Long startTime = channel.attr(SIMULATION_START_TIME).get();
                long chargingMin = (System.currentTimeMillis() - startTime) / 60000;
                message.setCumulativeChargingTime(message.getCumulativeChargingTime() + (int) chargingMin);
                if (message.getSoc() < 100) {
                    message.setSoc(message.getSoc() + (int) chargingMin);
                    message.setRemainingTime(100 - message.getSoc());
                }
                int currentChargeEnergy = RandomUtil.randomInt(2000, 5000);
                message.setChargedEnergy(message.getChargedEnergy() + currentChargeEnergy);
                message.setChargedAmount(message.getChargedEnergy() * 2);
            }
        }
        PlatConnectorRealtimeData realTimeData = toRealTimeData(message);
        realtimeDataProducer.sendMsg(realTimeData);
        return ProtocolConstant.NO_RESP;
    }

    public PlatConnectorRealtimeData toRealTimeData(Status13 message) {
        PlatConnectorRealtimeData data = new PlatConnectorRealtimeData();
        data.setTradeNo(message.getTrxSeqNo());
        data.setConnectorId(message.getPileCode() + message.getGunCode());
        data.setPileNo(message.getPileCode());
        data.setGunNo(message.getGunCode());
        data.setState(message.getStatus().shortValue());
        data.setGunState(message.getIsGunInserted().shortValue());
        data.setGunLink(message.getIsGunInserted().shortValue());
        data.setOutVoltage(new BigDecimal(message.getOutputVoltage()).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP));
        data.setOutCurrent(new BigDecimal(message.getOutputCurrent()).divide(new BigDecimal(10), 1, BigDecimal.ROUND_HALF_UP));
        data.setGunlineTemp(message.getGunLineTemperature());
        data.setGunlineNo(Long.parseLong(message.getGunLineCode(), 16));
        data.setSoc(new BigDecimal(message.getSoc()).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP));
        data.setBatteryMaxTemp(message.getBatteryMaxTemperature());
        data.setTotalChargeDurant(message.getCumulativeChargingTime());
        data.setRemainChargeDurent(message.getRemainingTime());
        data.setChargingKWH(new BigDecimal(message.getChargedEnergy()).divide(new BigDecimal(10000), 4, BigDecimal.ROUND_HALF_UP));
        data.setLoseKwh(new BigDecimal(message.getCalculatedLossEnergy()).divide(new BigDecimal(10000), 4, BigDecimal.ROUND_HALF_UP));
        data.setChargeMoney(new BigDecimal(message.getChargedAmount()).divide(new BigDecimal(10000), 4, BigDecimal.ROUND_HALF_UP));
        data.setHdError(message.getHardwareFault().shortValue());
        data.setCreateTime(new Date());
        return data;
    }
}
