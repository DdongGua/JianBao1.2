package com.bgs.jianbao12.utils;

/**
 * Created by Administrator on 2016/12/29.
 */

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 时间转换
 *
 * @author vendor
 */
public class TimeUtil {

    private static final String TAG = "TimeUtil";

    /**
     * 时间格式：yyyy-MM-dd
     */
    public static final String DATE_FORMAT = "yyyy年MM月dd日";

    /**
     * 时间格式：yyyy-MM-dd HH:mm:ss
     */
    public static final String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 时间格式：yyyy-MM-dd HH:mm
     */
    public static final String TIME_FORMAT2 = "yyyy-MM-dd HH:mm";

    /**
     * 在之前
     */
    public static final int TIME_BEFORE = 1;

    /**
     * 在中间
     */
    public static final int TIME_ING = 2;

    /**
     * 在之后
     */
    public static final int TIME_AFTER = 3;

    /**
     * 异常
     */
    public static final int TIME_ERROR = -1;

    /**
     * string型时间转换
     *
     * @param timeFormat 时间格式
     * @param timestamp  时间
     * @return 刚刚  x分钟  小时前  ...
     */
    public static String convertTime(String timeFormat, String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
        try {
            return convertTime(sdf.parse(timestamp).getTime());
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
            return null;
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
        }

        return timestamp;
    }

    /**
     * string型时间转换
     *
     * @param timeFormat 时间格式
     * @param timestamp  时间
     * @return 刚刚  x分钟  小时前  ...
     */
    public static String convertTime(String timeFormat, long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
        try {
            Date date = new Date();
            date.setTime(timestamp);
            return sdf.format(date);
        } catch (IllegalArgumentException e) {
            Log.e(TAG, e.getMessage());
            return "";
        }
    }

    /**
     * int型时间转换
     *
     * @param timestamp 时间
     * @return 刚刚  x分钟  一天内  ...
     */
    public static String convertTime(long timestamp) {
        String timeStr = null;

        long interval = (System.currentTimeMillis() - timestamp) / 1000;
        if (interval <= 60) { //1分钟内 服务端的时间 可能和本地的有区别 所以小于0的 对于这个情况全部都显示刚刚
            timeStr = "刚刚";
        } else if (interval < 60 * 60) { // 1小时内
            timeStr = (interval / 60 == 0 ? 1 : interval / 60) + "分钟前";
        } else if (interval < 24 * 60 * 60) { // 一天内
            timeStr = (interval / 60 * 60 == 0 ? 1 : interval / (60 * 60)) + "小时前";
        } else if (interval < 30 * 24 * 60 * 60) { // 天前
            timeStr = (interval / 24 * 60 * 60 == 0 ? 1 : interval / (24 * 60 * 60)) + "天前";
        } else if (interval < 12 * 30 * 24 * 60 * 60) { // 月前
            timeStr = (interval / 30 * 24 * 60 * 60 == 0 ? 1 : interval / (30 * 24 * 60 * 60)) + "月前";
        } else if (interval < 12 * 30 * 24 * 60 * 60) { // 年前
            timeStr = (interval / 12 * 30 * 24 * 60 * 60 == 0 ? 1 : interval / (12 * 30 * 24 * 60 * 60)) + "年前";
        } else {
            Date date = new Date();
            date.setTime(timestamp);
            timeStr = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(date);
        }

        return timeStr;
    }

    /**
     * int型时间转换 比较距离结束
     * @param timestamp 时间
     * @return 刚刚  x分钟  一天后  ...
     */
    public static String convertEndTime(long timestamp) {
        String timeStr = null;

        long interval = (timestamp - System.currentTimeMillis()) / 1000;
        if (interval <= 60) { //1分钟内 服务端的时间 可能和本地的有区别 所以小于0的 对于这个情况全部都显示刚刚
            timeStr = "刚刚";
        } else if (interval < 60 * 60) { // 1小时内
            timeStr = (interval / 60 == 0 ? 1 : interval / 60) + "分钟后";
        } else if (interval < 24 * 60 * 60) { // 一天内
            timeStr = (interval / 60 * 60 == 0 ? 1 : interval / (60 * 60)) + "小时后";
        } else if (interval < 30 * 24 * 60 * 60) { // 天前
            timeStr = (interval / 24 * 60 * 60 == 0 ? 1 : interval / (24 * 60 * 60)) + "天后";
        } else if (interval < 12 * 30 * 24 * 60 * 60) { // 月前
            timeStr = (interval / 30 * 24 * 60 * 60 == 0 ? 1 : interval / (30 * 24 * 60 * 60)) + "月后";
        } else if (interval < 12 * 30 * 24 * 60 * 60) { // 年前
            timeStr = (interval / 12 * 30 * 24 * 60 * 60 == 0 ? 1 : interval / (12 * 30 * 24 * 60 * 60)) + "年后";
        } else {
            Date date = new Date();
            date.setTime(interval);
            timeStr = new SimpleDateFormat(DATE_FORMAT, Locale.getDefault()).format(date);
        }

        return timeStr;
    }

    /**
     * 将long型时间转为固定格式的时间字符串
     *
     * @param longTime 时间
     * @return {@link TimeUtil#TIME_FORMAT}
     */
    public static String convertToTime(long longTime) {
        return convertToTime(TIME_FORMAT, longTime);
    }

    /**
     * 将long型时间转为固定格式的时间字符串
     *
     * @param timeformat 时间格式
     * @param longTime   时间
     * @return timeformat
     */
    public static String convertToTime(String timeformat, long longTime) {
        Date date = new Date(longTime);
        return convertToTime(timeformat, date);
    }

    /**
     * 将Date型时间转为固定格式的时间字符串
     * @param timeformat 时间格式
     * @param date 时间
     * @return timeformat
     */
    public static String convertToTime(String timeformat, Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeformat, Locale.getDefault());
        return sdf.format(date);
    }

    /**
     * 将Calendar型时间转为固定格式的时间字符串
     * @param timeformat 时间格式
     * @param calendar   时间
     * @return timeformat
     */
    public static String convertToTime(String timeformat, Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeformat, Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    /**
     * 将long型时间转为固定格式的日期字符串
     *
     * @param longTime 时间
     * @return {@link TimeUtil#DATE_FORMAT}
     */
    public static String convertToDate(long longTime) {
        return convertToTime(DATE_FORMAT, longTime);
    }

    /**
     * 将String类型时间转为long类型时间
     *
     * @param timeFormat 解析格式
     * @param timestamp  yyyy-MM-dd HH:mm:ss
     * @return 时间
     */
    public static long covertToLong(String timeFormat, String timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat(timeFormat, Locale.getDefault());
        try {
            Date date = sdf.parse(timestamp);
            return date.getTime();
        } catch (ParseException e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }
    }

    /**
     * long型时间转换
     *
     * @param longTime 长整型时间
     * @return 2013年7月3日 18:05(星期三)
     */
    public static String convertDayOfWeek(long longTime) {
        final String format = "%d年%d月%d日 %s:%s(%s)";

        Calendar c = Calendar.getInstance(); // 日历实例
        c.setTime(new Date(longTime));

        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        String h = hour > 9 ? String.valueOf(hour) : "0" + hour;
        int minute = c.get(Calendar.MINUTE);
        String m = minute > 9 ? String.valueOf(minute) : "0" + minute;
        return String.format(Locale.getDefault(), format, year, month + 1, date, h, m, converToWeek(c.get(Calendar.DAY_OF_WEEK)));
    }

    /**
     * 转换数字的星期为字符串的
     *
     * @param w
     * @return 星期x
     */
    private static String converToWeek(int w) {
        String week = null;

        switch (w) {
            case 1:
                week = "星期日";
                break;
            case 2:
                week = "星期一";
                break;
            case 3:
                week = "星期二";
                break;
            case 4:
                week = "星期三";
                break;
            case 5:
                week = "星期四";
                break;
            case 6:
                week = "星期五";
                break;
            case 7:
                week = "星期六";
                break;
        }

        return week;
    }

    /**
     * 计算时间是否在区间内
     *
     * @param time  time
     * @param time1 time
     * @param time2 time
     * @return {@link TimeUtil#TIME_BEFORE}{@link TimeUtil#TIME_ING}{@link TimeUtil#TIME_AFTER}
     */
    public static int betweenTime(long time, long time1, long time2) {
        if (time1 > time2) {  //时间1大
            long testTime = time1;
            time1 = time2;
            time2 = testTime;
        }

//已经过去
        if (time1 > time) {
            return TIME_BEFORE;
        } else if (time2 < time) {
            return TIME_AFTER;
        } else {
            return TIME_ING;
        }
    }

    public static String computeTimeDifference(String time) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.util.Date date = df.parse(time);
            return computeTimeDifference(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String computeTimeDifference(long time) {
        long l = time - System.currentTimeMillis();
        if(l <= 0) {
            return "" + 0 + "天" + 0 + "小时" + 0 + "分" + 0 + "秒";
        }

        long day = l / (24 * 60 * 60 * 1000);
        long hour = (l / (60 * 60 * 1000) - day * 24);
        long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
        long s = (l / 1000 - day * 24 * 60 * 60 - hour * 60 * 60 - min * 60);
        return "" + day + "天" + hour + "小时" + min + "分" + s + "秒";
    }

    public static String timeFormat(long time){
        long l = time - System.currentTimeMillis();
        if(l <= 0) {
            return "" + 0 + "天" + 0 + "小时";
        }

        long diffHour = l/(1000 * 60 * 60);
        long day = l/(1000 * 60 *60 * 24);
        if (diffHour < 24){
// 显示为小时
            return  "" + diffHour + "小时";
        } else {
// 显示天
            return "" + day + "天";
        }
    }
}
