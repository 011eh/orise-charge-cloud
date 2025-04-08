package org.dromara.omind.userplat;

import lombok.RequiredArgsConstructor;
import org.dromara.omind.userplat.api.domain.entity.OmindConnectorEntity;
import org.dromara.omind.userplat.api.domain.notifications.NotificationChargeOrderInfoData;
import org.dromara.omind.userplat.service.impl.OmindBillServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("userplat/test")
@RequiredArgsConstructor
public class UserPlatTest {


    private final OmindBillServiceImpl OmindBillServiceImpl;

    @GetMapping("/{n}")
    public Object name(@PathVariable Integer n) {
        switch (n) {
            case Integer.MAX_VALUE -> {
                return null;
            }
            case 1 -> {
                NotificationChargeOrderInfoData notificationChargeOrderInfoData = new NotificationChargeOrderInfoData();
                notificationChargeOrderInfoData.setStartChargeSeq("MA01D1QR8165563129210875904");  // 充电序列号
                notificationChargeOrderInfoData.setVin("TESTVIN123456789");  // 车辆VIN码
                notificationChargeOrderInfoData.setStartTime("2024-03-20 10:00:00");  // 开始充电时间
                notificationChargeOrderInfoData.setEndTime("2024-03-20 11:30:00");    // 结束充电时间
                notificationChargeOrderInfoData.setTotalMoney(new BigDecimal("50.00"));        // 总金额
                notificationChargeOrderInfoData.setTotalElecMoney(new BigDecimal("40.00"));    // 电费金额
                notificationChargeOrderInfoData.setTotalSeviceMoney(new BigDecimal("10.00"));  // 服务费金额
                notificationChargeOrderInfoData.setTotalPower(new BigDecimal("20.5"));  // 充电总电量
                notificationChargeOrderInfoData.setStopReason((short) 1);                       // 停止原因
                notificationChargeOrderInfoData.setSumPeriod((short) 90);                       // 充电总时长(分钟)
                notificationChargeOrderInfoData.setStartChargeSeq("MA01D1QR8165563129210875904");
                OmindBillServiceImpl.chargeOrderInfoDeal(notificationChargeOrderInfoData, new OmindConnectorEntity(), "MA80GFQM8", (short) 1);
            }

            default -> throw new IllegalStateException("Unexpected value: " + n);
        }
        return "ok";
    }
}
