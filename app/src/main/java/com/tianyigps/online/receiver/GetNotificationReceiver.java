package com.tianyigps.online.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.tianyigps.online.bean.WarnTypeBean;
import com.tianyigps.online.manager.SharedManager;

import cn.jpush.android.api.JPushInterface;

public class GetNotificationReceiver extends BroadcastReceiver {

    private static final String TAG = "GetNotificationReceiver";

    private SharedManager mSharedManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        mSharedManager = new SharedManager(context);
        String warnTypeLocate = mSharedManager.getWarnType();
        boolean isReceiveWarn = mSharedManager.isWarn();
        Log.i(TAG, "onReceive: ");
        Bundle bundle = intent.getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
        int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        String file = bundle.getString(JPushInterface.EXTRA_MSG_ID);

        Log.i(TAG, "onReceive: bundle-->" + bundle.toString());
        Log.i(TAG, "onReceive: title-->" + title);
        Log.i(TAG, "onReceive: content-->" + content);
        Log.i(TAG, "onReceive: type-->" + type);
        Log.i(TAG, "onReceive: notificationId-->" + notificationId);
        Log.i(TAG, "onReceive: file-->" + file);

        Gson gson = new Gson();
        WarnTypeBean warnTypeBean = gson.fromJson(type, WarnTypeBean.class);
        String warnType = warnTypeBean.getWarnType();

        Log.i(TAG, "onReceive: warnType-->" + warnType);
        Log.i(TAG, "onReceive: warnTypeLocate-->" + warnTypeLocate);
        if (!warnTypeLocate.contains(warnType) && !isReceiveWarn) {
            JPushInterface.clearNotificationById(context, notificationId);
        }
    }
}
