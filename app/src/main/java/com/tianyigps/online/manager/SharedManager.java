package com.tianyigps.online.manager;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.tianyigps.online.data.Data;

/**
 * Created by cookiemouse on 2017/9/4.
 */

public class SharedManager {

    private static final String TAG = "SharedManager";

    private SharedPreferences mSharedPreferences;

    public SharedManager(Context context) {
        mSharedPreferences = context.getSharedPreferences(Data.DATA_SHAREDPREFERENCES, Context.MODE_PRIVATE);
    }

    /**
     * 保存用户信息
     */
    public void saveUserData(int cid, String path, String contactAddr, String contactName, String contactPhone
            , String name, String token, String account, String password) {

        Log.i(TAG, "saveUserData: name-->" + name);
        Log.i(TAG, "saveUserData: token-->" + token);

        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(Data.CHECK_USER_CID, cid);
        editor.putString(Data.CHECK_USER_PATH, path);
        editor.putString(Data.CHECK_USER_CONTACT_ADDR, contactAddr);
        editor.putString(Data.CHECK_USER_CONTACT_NAME, contactName);
        editor.putString(Data.CHECK_USER_CONTACT_PHONE, contactPhone);
        editor.putString(Data.CHECK_USER_NAME, name);
        editor.putString(Data.CHECK_USER_TOKEN, token);
        editor.putString(Data.CHECK_USER_ACCOUNT, account);
        editor.putString(Data.CHECK_USER_PASSWORD, password);
        editor.apply();
    }

    /**
     * 获取cid
     *
     * @return CID
     */
    public int getCid() {
        return mSharedPreferences.getInt(Data.CHECK_USER_CID, 0);
    }

    /**
     * 获取path
     */
    public String getPath() {
        return mSharedPreferences.getString(Data.CHECK_USER_PATH, "");
    }

    /**
     * 获取ContactAddr
     */
    public String getContactAddr() {
        return mSharedPreferences.getString(Data.CHECK_USER_CONTACT_ADDR, "");
    }

    /**
     * 获取ContactName
     */
    public String getContactName() {
        return mSharedPreferences.getString(Data.CHECK_USER_CONTACT_NAME, "");
    }

    /**
     * 获取ContactPhone
     */
    public String getContactPhone() {
        return mSharedPreferences.getString(Data.CHECK_USER_CONTACT_PHONE, "");
    }

    /**
     * 获取Name
     */
    public String getName() {
        return mSharedPreferences.getString(Data.CHECK_USER_NAME, "");
    }

    /**
     * 获取Token
     */
    public String getToken() {
        return mSharedPreferences.getString(Data.CHECK_USER_TOKEN, "");
    }

    /**
     * 获取UserName
     */
    public String getAccount() {
        return mSharedPreferences.getString(Data.CHECK_USER_ACCOUNT, "");
    }

    //获取密码
    public String getPassword() {
        return mSharedPreferences.getString(Data.CHECK_USER_PASSWORD, "");
    }

    /**
     * 保存是否记住密码
     */
    public void saveRememberPassword(boolean remember) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Data.LOGIN_REMEMBER_PASSWORD, remember);
        editor.apply();
    }

    /**
     * 获取是否保存密码
     */
    public boolean getRememberPassword() {
        return mSharedPreferences.getBoolean(Data.LOGIN_REMEMBER_PASSWORD, false);
    }

    /**
     * 保存是否自动登录
     */
    public void saveAutoLogin(boolean auto) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Data.LOGIN_AUTO, auto);
        editor.apply();
    }

    /**
     * 获取是否自动登录
     */
    public boolean getAutoLogin() {
        return mSharedPreferences.getBoolean(Data.LOGIN_AUTO, false);
    }

    /**
     * 保存报警类型
     * 01,99
     */
    public void saveWarnType(String type) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Data.WARN_TYPE, type);
        editor.apply();
    }

    /**
     * 获取报警类型
     */
    public String getWarnType() {
        return mSharedPreferences.getString(Data.WARN_TYPE, "01,99,84,83,93,97,98");
    }

    /**
     * 保存是否接收报警
     */
    public void saveWarn(boolean isWarn) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Data.IS_WARN, isWarn);
        editor.apply();
    }

    /**
     * 获取是否接收报警
     */
    public boolean isWarn() {
        return mSharedPreferences.getBoolean(Data.IS_WARN, false);
    }

    /**
     * 保存刷新时间
     */
    public void saveFlushTime(int time) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(Data.FLUSH_TIME, time);
        editor.apply();
    }

    /**
     * 获取刷新时间
     */
    public int getFlushTime() {
        return mSharedPreferences.getInt(Data.FLUSH_TIME, 10);
    }

    /**
     * 保存是否显示已加关注车辆
     */
    public void saveShowAttention(boolean attention) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(Data.SHOW_ATTENTION, attention);
        editor.apply();
    }

    /**
     * 获取是否显示已加关注车辆
     */
    public boolean getShowAttention() {
        return mSharedPreferences.getBoolean(Data.SHOW_ATTENTION, false);
    }

    /**
     * 保存登陆主页
     * 0 = 车辆列表，1 = 监控页面，2 = 监控页面
     */
    public void saveMainPage(int page) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(Data.MAIN_PAGE, page);
        editor.apply();
    }

    public int getMainPage() {
        return mSharedPreferences.getInt(Data.MAIN_PAGE, 0);
    }

    /**
     * 保存当天日期
     * date
     */
    public void saveDate(String date) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putString(Data.DATE, date);
        editor.apply();
    }

    public String getDate() {
        return mSharedPreferences.getString(Data.DATE, "");
    }
}
