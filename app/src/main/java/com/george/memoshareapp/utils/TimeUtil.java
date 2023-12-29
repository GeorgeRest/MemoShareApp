package com.george.memoshareapp.utils;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;


public class TimeUtil {

    private static final String TAG = "RouteManTimeUtil";

    /**
     * @param dt
     * @param pattern
     * @return 如果时间转换成功则返回结果，否则返回空字符串""
     */
    public static String getTimeString(Date dt, String pattern) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);//"yyyy-MM-dd HH:mm:ss"
            sdf.setTimeZone(TimeZone.getDefault());
            return sdf.format(dt);
        }
        catch(Exception e) {
            return"";
        }
    }

    /**
     * 1）7天之内的日期显示逻辑是：今天、昨天(-1d)、前天(-2d)、星期？（只显示总计7天之内的星期数，即<=-4d）；<br>
     * 2）7天之外（即>7天）的逻辑：直接显示完整日期时间。
     * @param srcDate 要处理的源日期时间对象
     * @param mustIncludeTime true用于在聊天页面中，false用于消息列表的时间
     * @return 输出格式形如：“10:30”、“昨天 12:04”、“前天 20:51”、“星期二”、“2019/2/21 12:09”等形式
     */

    public static  String getTimeStringAutoShort2(Date srcDate, boolean mustIncludeTime) {
        String ret = "";
        try {
            GregorianCalendar gcCurrent = new GregorianCalendar();
            gcCurrent.setTime(new Date());
            int currentYear = gcCurrent.get(GregorianCalendar.YEAR);
            int currentMonth = gcCurrent.get(GregorianCalendar.MONTH) + 1;
            int currentDay = gcCurrent.get(GregorianCalendar.DAY_OF_MONTH);
            GregorianCalendar gcSrc = new GregorianCalendar();
            gcSrc.setTime(srcDate);
            int srcYear = gcSrc.get(GregorianCalendar.YEAR);
            int srcMonth = gcSrc.get(GregorianCalendar.MONTH) + 1;
            int srcDay = gcSrc.get(GregorianCalendar.DAY_OF_MONTH);
            // 要额外显示的时间分钟
            String timeExtraStr = (mustIncludeTime ? " " + getTimeString(srcDate, "HH:mm") : "");
            // 当年
            if (currentYear == srcYear) {
                long currentTimestamp = gcCurrent.getTimeInMillis();
                long srcTimestamp = gcSrc.getTimeInMillis();
                // 相差时间（单位：毫秒）
                long delta = (currentTimestamp - srcTimestamp);
                if (currentMonth == srcMonth && currentDay == srcDay) { // 当天（月份和日期一致才是）
                    ret = getTimeString(srcDate, "HH:mm");
                } else { // 当年 && 当天之外的时间（即昨天及以前的时间）
                    // 昨天（以“现在”的时候为基准-1天）
                    GregorianCalendar yesterdayDate = new GregorianCalendar();
                    yesterdayDate.add(GregorianCalendar.DAY_OF_MONTH, -1);
                    // 前天（以“现在”的时候为基准-2天）
                    GregorianCalendar beforeYesterdayDate = new GregorianCalendar();
                    beforeYesterdayDate.add(GregorianCalendar.DAY_OF_MONTH, -2);
                    // 用目标日期的“月”和“天”跟上方计算出来的“昨天”进行比较
                    if (srcMonth == (yesterdayDate.get(GregorianCalendar.MONTH) + 1)
                            && srcDay == yesterdayDate.get(GregorianCalendar.DAY_OF_MONTH)) {
                        ret = "昨天" + timeExtraStr;// -1d
                    } else if (srcMonth == (beforeYesterdayDate.get(GregorianCalendar.MONTH) + 1)
                            && srcDay == beforeYesterdayDate.get(GregorianCalendar.DAY_OF_MONTH)) { // “前天”判断逻辑同上
                        ret = "前天" + timeExtraStr;// -2d
                    } else {
                        // 跟当前时间相差的小时数
                        long deltaHour = (delta / (3600 * 1000));
                        // 如果小于 7*24小时就显示星期几
                        if (deltaHour < 7 * 24) {
                            String[] weekday = {"星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六"};
                            // 取出当前是星期几
                            String weedayDesc = weekday[gcSrc.get(GregorianCalendar.DAY_OF_WEEK) - 1];
                            ret = weedayDesc + timeExtraStr;
                        } else { // 否则直接显示完整日期时间
                            ret = getTimeString(srcDate, "MM月dd日") + timeExtraStr;
                        }
                    }
                }
            } else {
                ret = getTimeString(srcDate, "yyyy年MM月dd日") + timeExtraStr;
            }
        } catch (Exception e) {
            System.err.println("【DEBUG-getTimeStringAutoShort】计算出错：" + e.getMessage() + " 【NO】");
        }
        return ret;
    }

    public static boolean isShowTime(long nowTime,long lastTime){
        long delta = (nowTime - lastTime);
        if (delta <300*1000){
            return false;
        }else
            return true;
    }

    public static long convertToTimestamp(String dateTimeStr) {
        // 如果输入字符串为空，返回 0（1970 年 1 月 1 日的时间戳）
        if (dateTimeStr == null || dateTimeStr.isEmpty()) {
            return 0L;
        }

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = format.parse(dateTimeStr);
            if (date != null) {
                return date.getTime();
            } else {
                throw new ParseException("Unable to parse date: " + dateTimeStr, 0);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            // Handle the exception according to your needs
            return -1; // Indicating an error
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public static String convertWXTime(String input) {
        LocalDate today = LocalDate.now(); // 获取当前日期

        if (input == null || input.isEmpty()) {
            return "";
        }

        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM-dd");

        try {
            LocalDateTime dateTime = LocalDateTime.parse(input, inputFormatter);
            LocalDate date = dateTime.toLocalDate();
            long daysBetween = ChronoUnit.DAYS.between(date, today);

            if (daysBetween == 0) { // 今天
                return dateTime.format(timeFormatter);
            } else if (daysBetween == 1) { // 昨天
                return "昨天 " + dateTime.format(timeFormatter);
            } else if (daysBetween == 2) { // 前天
                return "前天 " + dateTime.format(timeFormatter);
            } else if (daysBetween > 2 && daysBetween <= 7) { // 一周内
                return "星期" + getChineseWeekDay(date.getDayOfWeek().getValue());
            } else if (date.getYear() == today.getYear()) { // 今年
                return date.format(dateFormatter);
            } else { // 去年或更早
                return date.format(inputFormatter);
            }
        } catch (Exception e) {
            return "日期格式错误";
        }
    }

    private static String getChineseWeekDay(int dayOfWeek) {
        switch (dayOfWeek) {
            case 1:
                return "一";
            case 2:
                return "二";
            case 3:
                return "三";
            case 4:
                return "四";
            case 5:
                return "五";
            case 6:
                return "六";
            case 7:
                return "日";
            default:
                return "";
        }
    }

}
