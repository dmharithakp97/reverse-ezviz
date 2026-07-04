/*
 * Decompiled with CFR 0.152.
 */
package com.videogo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtil {
    public static final String TIME_FORMAT_YMDHMS = "yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMAT_YMDHMS_SPACED = "yyyyMMdd HHmmss";
    public static final String TIME_FORMAT_YMDHMS_COMPACT = "yyyyMMddHHmmss";
    public static final String TIME_FORMAT_YMD = "yyyy-MM-dd";
    public static final String TIME_FORMAT_HMS = "HH:mm:ss";
    public static final String TIME_FORMAT_SSS = "yyyy-MM-dd HH:mm:ss.SSS";

    public static Date getNow() {
        Calendar c = Calendar.getInstance();
        return c.getTime();
    }

    public static Date beginDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(11, 0);
        c.set(12, 0);
        c.set(13, 0);
        c.set(14, 0);
        return c.getTime();
    }

    public static Date endDate(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(11, 23);
        c.set(12, 59);
        c.set(13, 59);
        return c.getTime();
    }

    public static String formatDateToString(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        SimpleDateFormat sdf = DateTimeUtil.getEZDateFormat(pattern);
        return sdf.format(date);
    }

    public static String formatDateToYMDHMSString(Date date) {
        return DateTimeUtil.formatDateToString(date, TIME_FORMAT_YMDHMS);
    }

    public static String formatDateToYMDString(Date date) {
        return DateTimeUtil.formatDateToString(date, TIME_FORMAT_YMD);
    }

    public static Date parseStringToDate(String strDate, String pattern) {
        if (strDate == null || strDate.isEmpty()) {
            return null;
        }
        try {
            SimpleDateFormat sdf = DateTimeUtil.getEZDateFormat(pattern);
            return sdf.parse(strDate);
        }
        catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date parseYMDHMSStringToDate(String strDate) {
        return DateTimeUtil.parseStringToDate(strDate, TIME_FORMAT_YMDHMS);
    }

    public static Date parseYMDStringToDate(String strDate) {
        return DateTimeUtil.parseStringToDate(strDate, TIME_FORMAT_YMD);
    }

    public static String formatTimeToString(long time, String pattern) {
        Date date = new Date();
        date.setTime(time);
        SimpleDateFormat sdf = DateTimeUtil.getEZDateFormat(pattern);
        return sdf.format(date);
    }

    public static String formatTimeToYMDHMSString(long time) {
        return DateTimeUtil.formatTimeToString(time, TIME_FORMAT_YMDHMS);
    }

    public static String formatTimeToYMDTHMSString(long time) {
        String string = DateTimeUtil.formatTimeToString(time, TIME_FORMAT_YMDHMS);
        return string.replace(" ", "T");
    }

    public static String formatTimeToHMSString(long seconds) {
        Date date = new Date(seconds * 1000L);
        SimpleDateFormat sdf = DateTimeUtil.getEZDateFormat(TIME_FORMAT_HMS);
        return sdf.format(date);
    }

    public static String calendarToString(Calendar calendar, String pattern) {
        return DateTimeUtil.formatDateToString(calendar.getTime(), pattern);
    }

    public static String calendarToYMDHMSString(Calendar calendar) {
        return DateTimeUtil.calendarToString(calendar, TIME_FORMAT_YMDHMS);
    }

    public static String calendarToHMSString(Calendar calendar) {
        return DateTimeUtil.calendarToString(calendar, TIME_FORMAT_HMS);
    }

    public static String calendarToYMDTHMSZString(Calendar calendar) {
        String string = DateTimeUtil.calendarToString(calendar, TIME_FORMAT_YMDHMS_SPACED);
        return string.replace(" ", "T") + "Z";
    }

    public static String calendarToYMDTHMSString(Calendar calendar) {
        String string = DateTimeUtil.calendarToString(calendar, TIME_FORMAT_YMDHMS_SPACED);
        return string.replace(" ", "T");
    }

    public static String calendarToYMDHMSCompactString(Calendar calendar) {
        return DateTimeUtil.calendarToString(calendar, TIME_FORMAT_YMDHMS_COMPACT);
    }

    public static Calendar parseStringToCalendar(String strTime, String pattern) {
        if (strTime == null) {
            return null;
        }
        Date date = DateTimeUtil.parseStringToDate(strTime, pattern);
        Calendar timeCalendar = Calendar.getInstance();
        timeCalendar.setTime(date);
        return timeCalendar;
    }

    public static Calendar parseYMDHMSStringToCalendar(String strTime) {
        return DateTimeUtil.parseStringToCalendar(strTime, TIME_FORMAT_YMDHMS);
    }

    public static SimpleDateFormat getEZDateFormat(String pattern) {
        String language = Locale.getDefault().getLanguage();
        if (language.contains("ar")) {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.US);
            return sdf;
        }
        return new SimpleDateFormat(pattern);
    }
}

