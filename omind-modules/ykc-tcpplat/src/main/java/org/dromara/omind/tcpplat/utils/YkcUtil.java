package org.dromara.omind.tcpplat.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;

public class YkcUtil {

    public static BigDecimal getBigDecimal(long number, int scale) {
        return BigDecimal.valueOf(number).divide(BigDecimal.valueOf(Math.pow(10, scale))).setScale(scale, RoundingMode.UP);
    }

    /**
     * 将 CP56Time2a 格式的长整型转换为 Date 对象
     * @param cp56Time2a CP56Time2a 格式的时间值（7字节）
     * @return Java Date 对象
     */
    public static Date cp56Time2aToDate(long cp56Time2a) {
        // 从长整型中提取各个时间分量
        int milliseconds = (int)(cp56Time2a & 0xFFFF);         // 低 16 位为毫秒
        int minute = (int)((cp56Time2a >> 16) & 0x3F);         // 分钟 (0-59)
        int hour = (int)((cp56Time2a >> 24) & 0x1F);           // 小时 (0-23)
        int day = (int)((cp56Time2a >> 32) & 0x1F);            // 日 (1-31)
        int month = (int)((cp56Time2a >> 40) & 0x0F);          // 月 (1-12)
        int year = (int)((cp56Time2a >> 48) & 0x7F);           // 年 (0-99)

        // 创建 Calendar 实例并设置时间
        Calendar calendar = Calendar.getInstance();
        calendar.clear();

        // 年份处理 (2000-2099)
        calendar.set(Calendar.YEAR, 2000 + year);
        calendar.set(Calendar.MONTH, month - 1);  // Calendar 月份从 0 开始
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, milliseconds / 1000);
        calendar.set(Calendar.MILLISECOND, milliseconds % 1000);

        return calendar.getTime();
    }

    /**
     * 将 Date 对象转换为 CP56Time2a 格式的长整型
     * @param date Java Date 对象
     * @return CP56Time2a 格式的时间值（7字节）
     */
    public static long dateToCp56Time2a(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        // 提取时间分量
        int milliseconds = calendar.get(Calendar.MILLISECOND) +
                calendar.get(Calendar.SECOND) * 1000;           // 毫秒和秒合并
        int minute = calendar.get(Calendar.MINUTE);                       // 分钟 (0-59)
        int hour = calendar.get(Calendar.HOUR_OF_DAY);                    // 小时 (0-23)
        int day = calendar.get(Calendar.DAY_OF_MONTH);                    // 日 (1-31)
        int month = calendar.get(Calendar.MONTH) + 1;                     // 月 (1-12)
        int year = calendar.get(Calendar.YEAR) - 2000;                    // 年 (0-99)

        // 组装为 CP56Time2a 格式的长整型
        long cp56Time2a = 0;
        cp56Time2a |= (long)milliseconds & 0xFFFF;
        cp56Time2a |= ((long)minute & 0x3F) << 16;
        cp56Time2a |= ((long)hour & 0x1F) << 24;
        cp56Time2a |= ((long)day & 0x1F) << 32;
        cp56Time2a |= ((long)month & 0x0F) << 40;
        cp56Time2a |= ((long)year & 0x7F) << 48;

        return cp56Time2a;
    }
}
