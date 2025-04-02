package org.dromara.omind.tcpplat.web.handler;

import io.netty.channel.Channel;
import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.omind.baseplat.api.domain.entity.PlatTradingRecordData;
import org.dromara.omind.baseplat.api.domain.entity.SysChargeOrder;
import org.dromara.omind.baseplat.api.service.RemoteSysChargeOrderService;
import org.dromara.omind.baseplat.api.service.RemoteTradeService;
import org.dromara.omind.mq.api.producer.ChargeOrderProducer;
import org.dromara.omind.tcpplat.netty.common.constant.ProtocolConstant;
import org.dromara.omind.tcpplat.netty.exception.BusinessException;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;
import org.dromara.omind.tcpplat.netty.protocol.model.message.down.TransactionRecordConfirm40;
import org.dromara.omind.tcpplat.netty.protocol.model.message.up.TransactionRecord3B;
import org.dromara.omind.tcpplat.netty.service.ChannelManager;
import org.dromara.omind.tcpplat.utils.YkcUtil;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Date;

import static org.dromara.omind.tcpplat.netty.common.constant.ProtocolConstant.SIMULATION_CHARGED_AMOUNT;
import static org.dromara.omind.tcpplat.netty.common.constant.ProtocolConstant.SIMULATION_CHARGED_ENERGY;

@Component
@RequiredArgsConstructor
public class TransactionRecordHandler implements IMsgHandler<TransactionRecord3B> {

    private final ChargeOrderProducer chargeOrderProducer;
    private final ChannelManager channelManager;

    @DubboReference
    RemoteSysChargeOrderService remoteSysChargeOrderService;

    @DubboReference
    RemoteTradeService remoteTradeService;


    @Override
    public Message handler(TransactionRecord3B message) {

        // 模拟数据
        String tradeNo = channelManager.get(message.getPileCode()).attr(ProtocolConstant.TRADE_NO).get();
        if (tradeNo == null) {
            throw new BusinessException("交易号不存在");
        }

        SysChargeOrder theChargeOrder = remoteSysChargeOrderService.getChargeOrderByTradeNo(tradeNo);

        //充电完成后，更新充电订单信息，发送充电数据
        if (theChargeOrder == null) {
            return ProtocolConstant.NO_RESP;
        }

        PlatTradingRecordData platTradingRecordData = new PlatTradingRecordData();
        platTradingRecordData.setTradeNo(tradeNo);
        platTradingRecordData.setPileNo(message.getPileCode());
        platTradingRecordData.setGunNo(message.getGunCode());
        platTradingRecordData.setStartTime(YkcUtil.cp56Time2aToDate(message.getStartTime()));
        platTradingRecordData.setEndTime(YkcUtil.cp56Time2aToDate(message.getEndTime()));

        platTradingRecordData.setSharpPerPrice(YkcUtil.getBigDecimal(message.getPeakPrice(), 5));
        platTradingRecordData.setSharpKwh(YkcUtil.getBigDecimal(message.getPeakEnergy(), 4));
        platTradingRecordData.setSharpAllKwh(YkcUtil.getBigDecimal(message.getPeakLossEnergy(), 4));
        platTradingRecordData.setSharpPrice(YkcUtil.getBigDecimal(message.getPeakAmount(), 4));

        platTradingRecordData.setPeakPerPrice(YkcUtil.getBigDecimal(message.getHighPrice(), 5));
        platTradingRecordData.setPeakKwh(YkcUtil.getBigDecimal(message.getHighEnergy(), 4));
        platTradingRecordData.setPeakAllKwh(YkcUtil.getBigDecimal(message.getHighLossEnergy(), 4));
        platTradingRecordData.setPeakPrice(YkcUtil.getBigDecimal(message.getHighAmount(), 4));

        platTradingRecordData.setFlatPerPrice(YkcUtil.getBigDecimal(message.getNormalPrice(), 5));
        platTradingRecordData.setFlatKwh(YkcUtil.getBigDecimal(message.getNormalEnergy(), 4));
        platTradingRecordData.setFlatAllKwh(YkcUtil.getBigDecimal(message.getNormalLossEnergy(), 4));
        platTradingRecordData.setFlatPrice(YkcUtil.getBigDecimal(message.getNormalAmount(), 4));

        platTradingRecordData.setValleyPerPrice(YkcUtil.getBigDecimal(message.getValleyPrice(), 5));
        platTradingRecordData.setValleyKwh(YkcUtil.getBigDecimal(message.getValleyEnergy(), 4));
        platTradingRecordData.setValleyAllKwh(YkcUtil.getBigDecimal(message.getValleyLossEnergy(), 4));
        platTradingRecordData.setValleyPrice(YkcUtil.getBigDecimal(message.getValleyAmount(), 4));

        platTradingRecordData.setStartKwh(YkcUtil.getBigDecimal(message.getMeterStartValue(), 5));
        platTradingRecordData.setEndKwh(YkcUtil.getBigDecimal(message.getMeterEndValue(), 5));
        platTradingRecordData.setFinalKwh(YkcUtil.getBigDecimal(message.getTotalEnergy(), 4));
        platTradingRecordData.setFinalAllKwh(YkcUtil.getBigDecimal(message.getTotalLossEnergy(), 4));

        platTradingRecordData.setCost(YkcUtil.getBigDecimal(message.getTotalAmount(), 4));
        platTradingRecordData.setVin(message.getVinCode());
        platTradingRecordData.setTradeType(message.getTransactionFlag().shortValue());
        platTradingRecordData.setTradeTime(YkcUtil.cp56Time2aToDate(message.getTransactionTime()));
        platTradingRecordData.setStopType(message.getStopReason());
        platTradingRecordData.setSystemCardNo(message.getPhysicalCardNo());

        // 模拟数据
        Channel channel = channelManager.getOrThrow(message.getPileCode());
        Integer energy = channel.attr(SIMULATION_CHARGED_ENERGY).get();
        if (energy != null) {
            platTradingRecordData.setFinalKwh(new BigDecimal(energy).divide(new BigDecimal(10000), 4, BigDecimal.ROUND_HALF_UP));
            platTradingRecordData.setFinalAllKwh(new BigDecimal(energy).divide(new BigDecimal(10000), 4, BigDecimal.ROUND_HALF_UP));
        }
        Integer amount = channel.attr(SIMULATION_CHARGED_AMOUNT).get();
        if (amount != null) {
            platTradingRecordData.setCost(new BigDecimal(amount).divide(new BigDecimal(10000), 4, BigDecimal.ROUND_HALF_UP));
        }

        remoteTradeService.finishTrade(platTradingRecordData);
        theChargeOrder.setEndTime(new Date());
        chargeOrderProducer.sendMsg(theChargeOrder);
        TransactionRecordConfirm40 reply = new TransactionRecordConfirm40().setTransactionSeqNo(tradeNo).setConfirmResult(0);
        return reply;
    }
}
