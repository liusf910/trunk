package com.ln.tms.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.util.TextUtils;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DateUtils - 日期时间处理工具类
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public final class DateUtils {

    public static final String DATETIMEFORMAT = "yyyy-MM-dd HH:mm:ss";
    public static final String DATEFORMAT = "yyyy/MM/dd HH:mm:ss";
    public static final String DATESIMPLEFORMAT = "yyyy-MM-dd";

    /**
     * 字符串转日期(yyyy-MM-dd HH:mm:ss)
     *
     * @param date 日期字符串
     * @return Date 日期
     */
    public static Date stringToDate(String date) {
        if (StringUtils.isBlank(date) || StringUtils.equals(date, "null")) {
            return null;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATETIMEFORMAT);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 字符串转日期(yyyy-MM-dd)
     *
     * @param date 日期字符串
     * @return Date 日期
     */
    public static Date strToDay(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATESIMPLEFORMAT);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 字符串转日期(yyyy/MM/dd HH:mm:ss)
     *
     * @param date 日期字符串
     * @return Date 日期
     */
    public static Date strToDate(String date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATEFORMAT);
        try {
            return simpleDateFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 获取时间差
     *
     * @param start 开始时间
     * @param end   结束时间
     * @return 时间差
     */
    public static Integer getDateDifference(Date start, Date end) {
        if (start != null && end != null) {
            LocalDate s = new LocalDate(start);
            LocalDate e = new LocalDate(end);
            Days days = Days.daysBetween(s, e);
            return days.getDays();
        } else {
            return null;
        }
    }

    /**
     * 日期大小比较(日期date1 >= date2)
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean compareToDate(Date date1, Date date2) {
        if (date1 != null && date2 != null) {
            if (date1.compareTo(date2) >= 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 日期格式转换
     *
     * @param date      日期
     * @param formatStr 定义的日期格式
     * @return
     */
    public static String getDateformat(Date date, String formatStr) {
        if (TextUtils.isEmpty(formatStr)) {
            formatStr = DATETIMEFORMAT;
        }
        DateFormat df = new SimpleDateFormat(formatStr);
        return df.format(date);
    }


    /**
     * 日期格式转换
     *
     * @param date      日期
     * @param formatStr 定义的日期格式
     * @return
     */
    public static String getNeDateformat(Date date, String formatStr) {
        if (TextUtils.isEmpty(formatStr)) {
            formatStr = DATETIMEFORMAT;
        }
        DateFormat df = new SimpleDateFormat(formatStr);
        return df.format(date);
    }

    /**
     * 日期格式转换(到天)
     *
     * @param date 日期
     * @return 格式(yyyy-MM-dd)
     */
    public static String getNewDaysFormat(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        return df.format(date);
    }

    /**
     * 日期格式转换(到天)
     *
     * @param date 日期
     * @return 格式(yyyy-MM-dd)
     */
    public static String getDaysFormat(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        return df.format(date);
    }

    /**
     * 日期格式转换到秒
     *
     * @param date 日期
     * @return 格式(yyyy/MM/dd HH:mm:ss)
     */
    public static String getDateformat(Date date) {
        DateFormat df = new SimpleDateFormat(DATEFORMAT);
        return df.format(date);
    }

    /**
     * 增加天
     *
     * @param start 基础时间
     * @param days  天数
     * @return 增加后的时间
     */
    public static Date getDateAddDays(Date start, Integer days) {
        DateTime dateTime = new DateTime(start);
        return dateTime.plusDays(days).toDate();
    }


    /**
     * 获得相差时间（小时）
     *
     * @param endDate
     * @param nowDate
     * @return
     */
    public static long getDateHours(Date endDate, Date nowDate) {
        long nd = 1000 * 24 * 60 * 60;
        long nh = 1000 * 60 * 60;
        // 获得两个时间的毫秒时间差异
        long diff = endDate.getTime() - nowDate.getTime();
        // 计算差多少天
        long day = diff / nd;
        // 计算差多少小时
        long hour = diff % nd / nh;
        if (day > 0) {
            hour = hour + day * 24;
        }
        return hour;
    }

    /**
     * 获取系统日期的前一天
     *
     * @return
     */
    public static String getBeforDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -1); //得到前一天
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(DATESIMPLEFORMAT);
        return sdf.format(date);
    }

    /**
     * 获取系统日期的后一天
     *
     * @return
     */
    public static String getAfterDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 1); //得到后一天
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(DATESIMPLEFORMAT);
        return sdf.format(date);
    }

    /**
     * 获取系统日期的后二天
     *
     * @return
     */
    public static String getAfterTowDays() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, 2); //得到后一天
        Date date = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat(DATESIMPLEFORMAT);
        return sdf.format(date);
    }

}