package com.github.yafeiwang1240.scheduler.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    public static final String FORMAT_DATATIME = "yyyy-MM-dd HH:mm:ss";
    public static final String FORMAT_DATATIME_SHORT = "yyyyMMddHHmmss";
    public static final String FORMAT_DATATIME_MS = "yyyyMMddHHmmssSSS";
    public static final String FORMAT_DATAT = "yyyy-MM-dd";

    public static String toString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }
    public static String toString(Date date) {
        return toString(date, FORMAT_DATATIME);
    }

    public static Date toDate(String dateString, String format) throws ParseException {
        return new SimpleDateFormat(format).parse(dateString);
    }

    public static Date toDate(String dateString) throws ParseException {
        return toDate(dateString, FORMAT_DATATIME);
    }

    public static Date toDate(long milliseconds) {
        return new Date(milliseconds);
    }

    public static long toLong(Date date) {
        return date.getTime();
    }

    /**
     * 当月第几天
     * @param date
     * @return
     */
    public static int day(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 当月第几天
     * @param date
     * @return
     */
    public static int day(long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public static int week(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int week(Long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.DAY_OF_WEEK) - 1;
    }

    public static int month(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int month(Long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.MONTH) + 1;
    }

    public static int year(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    public static int year(Long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.YEAR);
    }

    public static int hour(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int hour(Long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int minute(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MINUTE);
    }

    public static int minute(Long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.MINUTE);
    }

    public static int second(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.SECOND);
    }

    public static int second(Long millis) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return calendar.get(Calendar.SECOND);
    }
}
