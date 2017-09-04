package com.tianyigps.online.utils;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cookiemouse on 2017/8/4.
 */

public class RegularU {

    private static final String TAG = "RegularU";
    private static final String CHECK_CAR_NO = "[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5,6}$";

    public static boolean checkCarNo(String carNo) {
        Log.i(TAG, "checkCarNo: carNo-->" + carNo);
        Pattern pattern = Pattern.compile(CHECK_CAR_NO);
        Matcher matcher = pattern.matcher(carNo);
        Log.i(TAG, "checkCarNo: result-->" + matcher.matches());
        return matcher.matches();
    }

    public static boolean isEmpty(String content) {
        return null == content || "".equals(content);
    }
}
