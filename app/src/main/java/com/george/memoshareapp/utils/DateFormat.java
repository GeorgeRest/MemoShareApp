package com.george.memoshareapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;

/**
 * @projectName: Memosahre
 * @package: com.george.memoshareapp.utils
 * @className: DateFormat
 * @author: George
 * @description: TODO
 * @date: 2023/5/15 15:49
 * @version: 1.0
 */

public class DateFormat {

    public static String getMessageDate(String dateOriginal) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date;
        try {
            date = inputFormat.parse(dateOriginal);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        long original = date.getTime();
        long oneDay = 1000 * 60 * 60 * 24;

        // 设置一个日期表示今天零点
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);

        // 与今天零点比较
        if (original >= today.getTimeInMillis()) {
            // 今天
            return new SimpleDateFormat("HH:mm").format(date);
        } else {
            // 与昨天零点比较
            if (original >= today.getTimeInMillis() - oneDay) {
                // 昨天
                return "昨天" + new SimpleDateFormat("HH:mm").format(date);
            }
        }
        // 都不是返回原始数据，去掉分钟数后的部分
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);
    }

    public static String getCurrentDateTime(String dateOriginal) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date;
        try {
            date = inputFormat.parse(dateOriginal);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        long original = date.getTime();
        long oneDay = 1000 * 60 * 60 * 24;

        // 设置一个日期表示今天零点
        Calendar today = Calendar.getInstance();
        today.set(Calendar.HOUR_OF_DAY, 0);
        today.set(Calendar.MINUTE, 0);
        today.set(Calendar.SECOND, 0);
        today.set(Calendar.MILLISECOND, 0);


        // 都不是返回原始数据，去掉分钟数后的部分
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(date);

    }

}
