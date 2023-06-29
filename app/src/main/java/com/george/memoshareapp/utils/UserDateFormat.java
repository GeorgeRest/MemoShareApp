package com.george.memoshareapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class UserDateFormat {

    public static String formatMessageDate(String dateOriginal) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm");
        Date date;
        try {
            date = inputFormat.parse(dateOriginal);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        Calendar today = Calendar.getInstance();
        Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DAY_OF_YEAR, -1);

        Calendar inputDate = Calendar.getInstance();
        inputDate.setTime(date);

        if (isSameDay(inputDate, today)) {
            return "今天";
        } else if (isSameDay(inputDate, yesterday)) {
            return "昨天";
        } else {
            SimpleDateFormat outputFormat = new SimpleDateFormat("MM月dd日");
            return outputFormat.format(date);
        }
    }

    private static boolean isSameDay(Calendar cal1, Calendar cal2) {
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
                cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH);
    }
}

