package com.hyl.zhanmaoj.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 */
public class DateUtils {

    public static Date convertStringToDate(Date date, String timeStr) {
        SimpleDateFormat wholeSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat simpleSdf = new SimpleDateFormat("yyyy-MM-dd");
        if (date == null) {
            date = new Date();
        }
        String dateStr = simpleSdf.format(date).toString();
        String dateTimeStr = dateStr + " " + timeStr;
        Date combinedDate = null;
        try {
            combinedDate = wholeSdf.parse(dateTimeStr);
        } catch (ParseException e) {
            // 处理解析异常，例如记录日志或抛出自定义异常
            e.printStackTrace();
        }
        return combinedDate;
    }
}
