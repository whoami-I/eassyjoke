package com.baimeng.library.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by BaiMeng on 2016/12/20.
 */
public class TimeUtils {

    public static String formatTimeNoSecond(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date data = new Date(time);
        return format.format(data);
    }

    public static String formatTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return format.format(date);
    }

    public static String formatDay(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年M月d日");
        Date date = new Date(time);
        return format.format(date);
    }

    public static String getWeekOfYear(long time) {
        Date date = new Date(time);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return String.valueOf(calendar.get(Calendar.WEEK_OF_YEAR));
    }

    public static String getYear(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date date = new Date(time);
        return format.format(date);
    }

    public static String getMonthOfYear(long time) {
        SimpleDateFormat format = new SimpleDateFormat("MM");
        Date date = new Date(time);
        return format.format(date);
    }

    public static String getDay(long time) {
        SimpleDateFormat format = new SimpleDateFormat("dd");
        Date date = new Date(time);
        return format.format(date);
    }

    public static String getYearAndMonth(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年M月");
        Date date = new Date(time);
        return format.format(date);
    }

    public static Long getPreMonth(long time) {
        Date date = new Date(time);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, -1);
        return c.getTimeInMillis();
    }

    public static Long getNextMonth(long time) {
        Date date = new Date(time);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.MONTH, +1);
        return c.getTimeInMillis();
    }

    public static long stringToLong(String strTime, String formatType)
            throws ParseException {
        Date date = stringToDate(strTime, formatType); // String类型转成date类型
        if (date == null) {
            return 0;
        } else {
            long currentTime = dateToLong(date); // date类型转成long类型
            return currentTime;
        }
    }

    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    public static long dateToLong(Date date) {
        return date.getTime();
    }

    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }
}
