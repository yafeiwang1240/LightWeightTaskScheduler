package com.githup.yafeiwang1240.scheduler.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
}
