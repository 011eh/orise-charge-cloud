package org.dromara.omind.tcpplat.web.handler;

import lombok.RequiredArgsConstructor;
import org.apache.dubbo.config.annotation.DubboReference;
import org.dromara.omind.baseplat.api.domain.entity.PlatTradingRecordData;
import org.dromara.omind.baseplat.api.domain.entity.SysChargeOrder;
import org.dromara.omind.baseplat.api.service.RemoteSysChargeOrderService;
import org.dromara.omind.baseplat.api.service.RemoteTradeService;
import org.dromara.omind.mq.api.producer.ChargeOrderProducer;
import org.dromara.omind.tcpplat.netty.common.constant.ProtocolConstant;
import org.dromara.omind.tcpplat.netty.protocol.model.message.Message;
import org.dromara.omind.tcpplat.netty.protocol.model.message.down.TransactionRecordConfirm40;
import org.dromara.omind.tcpplat.netty.protocol.model.message.up.TransactionRecord3B;
import org.dromara.omind.tcpplat.utils.YkcUtil;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@RequiredArgsConstructor
public class TransactionRecordHandler implements IMsgHandler<TransactionRecord3B> {

    private final ChargeOrderProducer chargeOrderProducer;

    @DubboReference
    RemoteSysChargeOrderService remoteSysChargeOrderService;

    @DubboReference
    RemoteTradeService remoteTradeService;

    @Override
    public Message handler(TransactionRecord3B message) {
        TransactionRecordConfirm40 reply = new TransactionRecordConfirm40().setTransactionSeqNo(message.getTransactionSeqNo()).setConfirmResult(0);
        SysChargeOrder theChargeOrder = remoteSysChargeOrderService.getChargeOrderByStartChargeSeq(message.getTransactionSeqNo());

        //充电完成后，更新充电订单信息，发送充电数据
        if (theChargeOrder == null) {
            return ProtocolConstant.NO_RESP;
        }
        theChargeOrder.setEndTime(new Date());

        PlatTradingRecordData platTradingRecordData = new PlatTradingRecordData();
        platTradingRecordData.setTradeNo(theChargeOrder.getStartChargeSeq());
        platTradingRecordData.setPileNo(message.getPileCode());
        platTradingRecordData.setGunNo(message.getGunCode());
        platTradingRecordData.setStartTime(YkcUtil.cp56Time2aToDate(message.getStartTime()));
        platTradingRecordData.setEndTime(YkcUtil.cp56Time2aToDate(message.getEndTime()));

        platTradingRecordData.setSharpPerPrice(YkcUtil.getBigDecimal(message.getPeakPrice(), 4));
        platTradingRecordData.setSharpKwh(YkcUtil.getBigDecimal(message.getPeakEnergy(), 4));
        platTradingRecordData.setSharpAllKwh(YkcUtil.getBigDecimal(message.getPeakLossEnergy(), 4));
        platTradingRecordData.setSharpPrice(YkcUtil.getBigDecimal(message.getPeakAmount(), 4));

        platTradingRecordData.setPeakPerPrice(YkcUtil.getBigDecimal(message.getHighPrice(), 4));
        platTradingRecordData.setPeakKwh(YkcUtil.getBigDecimal(message.getHighEnergy(), 4));
        platTradingRecordData.setPeakAllKwh(YkcUtil.getBigDecimal(message.getHighLossEnergy(), 4));
        platTradingRecordData.setPeakPrice(YkcUtil.getBigDecimal(message.getHighAmount(), 4));

        platTradingRecordData.setFlatPerPrice(YkcUtil.getBigDecimal(message.getNormalPrice(), 4));
        platTradingRecordData.setFlatKwh(YkcUtil.getBigDecimal(message.getNormalEnergy(), 4));
        platTradingRecordData.setFlatAllKwh(YkcUtil.getBigDecimal(message.getNormalLossEnergy(), 4));
        platTradingRecordData.setFlatPrice(YkcUtil.getBigDecimal(message.getNormalAmount(), 4));

        platTradingRecordData.setValleyPerPrice(YkcUtil.getBigDecimal(message.getValleyPrice(), 4));
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


        remoteTradeService.finishTrade(platTradingRecordData);

        //remoteSysChargeOrderService.update(theChargeOrder);
        chargeOrderProducer.sendMsg(theChargeOrder);

        return reply;
    }
}
