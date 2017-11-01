package com.tianyigps.online.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by djc on 2017/7/17.
 */

public class TimeFormatU {

    private static final String TAG = "TimeFormatU";

    public static String millisToDate(long mills) {
        Date date = new Date(mills);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static String millisToDate2(long mills) {
        Date date = new Date(mills);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return simpleDateFormat.format(date);
    }

    public static String millisToDate3(long mills) {
        Date date = new Date(mills);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }

    public static String millisToClock(long mills) {
        Log.i(TAG, "millisToClock: mills-->" + mills);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis(mills);
//        int day = calendar.get(Calendar.DAY_OF_YEAR) - 1;
//        int hour = calendar.get(Calendar.HOUR_OF_DAY);
//        int min = calendar.get(Calendar.MINUTE);
//        int second = calendar.get(Calendar.SECOND);
        mills = mills / 1000;
        int day = (int) (mills / (24 * 3600));
        int hour = (int) ((mills % (24 * 3600)) / 3600);
        int min = (int) (mills % 3600 / 60);
        int second = (int) (mills % 3600 % 60);
        Log.i(TAG, "millisToClock: day-->" + day);
        Log.i(TAG, "millisToClock: hour-->" + hour);
        Log.i(TAG, "millisToClock: min-->" + min);
        Log.i(TAG, "millisToClock: second-->" + second);
        String time = "";
        if (day > 0) {
            time = day + "天";
        }
        if (hour > 0) {
            if (hour < 10) {
                time += "0" + hour;
            } else {
                time += "" + hour;
            }
            time += "小时";
        }
        if (min > 0) {
            if (min < 10) {
                time += "0" + min;
            } else {
                time += min;
            }
            time += "分钟";
        }
        if (second < 10) {
            time += "0" + second;
        } else {
            time += second;
        }
        time += "秒";
        return time;
    }

    public static String millisToClock2(double mills) {
        Log.i(TAG, "millisToClock2: mills-->" + mills);
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTimeInMillis((long) mills);
//        int day = calendar.get(Calendar.DAY_OF_YEAR) - 1;
//        int hour = calendar.get(Calendar.HOUR);
//        int min = calendar.get(Calendar.MINUTE);
//        int second = calendar.get(Calendar.SECOND);
        mills = mills / 1000;
        int day = (int) (mills / (24 * 3600));
        int hour = (int) ((mills % (24 * 3600)) / 3600);
        int min = (int) (mills % 3600 / 60);
        int second = (int) (mills % 3600 % 60);
        Log.i(TAG, "millisToClock2: day-->" + day);
        Log.i(TAG, "millisToClock2: hour-->" + hour);
        Log.i(TAG, "millisToClock2: min-->" + min);
        Log.i(TAG, "millisToClock2: second-->" + second);
        String time = "";
        if (mills == 0) {
            return time;
        }
        if (day > 0) {
            time = day + "天";
            return time;
        }
        if (hour > 0) {
            time = hour + "小时";
            return time;
        }
        if (min > 0) {
            time = min + "分钟";
            return time;
        }
        if (second > 0) {
            time = second + "秒";
            return time;
        }
        return time;
    }

    public static String millsToMinSec(long mills) {
//        int hour = (int) (mills / 1000 / 3600);
//        int min = (int) (mills / 1000 % 3600 / 60);
        int min = (int) (mills / 1000 / 60);
        int second = (int) (mills / 1000 % 3600 % 60);
        String time = "";
//        if (hour < 10) {
//            time = "0" + hour;
//        } else {
//            time = "" + hour;
//        }
        if (min < 10) {
            time += "0" + min;
        } else {
            time += "" + min;
        }
        if (second < 10) {
            time += ":0" + second;
        } else {
            time += ":" + second;
        }
        return time;
    }

    public static String millsToHourMin(long mills) {
        String time;
        int hour = (int) (mills / 1000 / 3600);
        int min = (int) (mills / 1000 % 3600 / 60);

        if (hour < 10) {
            time = "0" + hour;
        } else {
            time = "" + hour;
        }

        if (min < 10) {
            time += ":0" + min;
        } else {
            time += ":" + min;
        }

        return time;
    }

    //  转为分钟秒
    public static String millsToMinSec2(long mills) {
        String time;
        int min = (int) (mills / 1000 / 60);
        int second = (int) (mills / 1000 % 3600 % 60);
        time = min + "分钟" + second + "秒";
        return time;
    }

    public static String millsToMothDay(long mills) {
        Date date = new Date(mills);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M月dd日");
        return simpleDateFormat.format(date);
    }

    //字符串转时间戳
    public static long dateToMillis(String time) {
        long timeStamp = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = sdf.parse(time);
            timeStamp = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    //字符串转时间戳
    public static long dateToMillis2(String time) {
        long timeStamp = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        try {
            Date date = sdf.parse(time);
            timeStamp = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return timeStamp;
    }

    //  获取当前年-月-日
    public static String getDate() {
        String date;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int mon = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        date = year + "-" + mon + "-" + day;
        Log.i(TAG, "getDate: date-->" + date);
        return date;
    }
}