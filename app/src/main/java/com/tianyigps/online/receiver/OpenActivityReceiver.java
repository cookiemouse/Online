package com.tianyigps.online.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.tianyigps.online.GuideActivity;
import com.tianyigps.online.activity.FragmentContentActivity;
import com.tianyigps.online.bean.WarnTypeBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.RegularU;

import cn.jpush.android.api.JPushInterface;

public class OpenActivityReceiver extends BroadcastReceiver {

    private static final String TAG = "OpenActivityReceiver";

    private SharedManager mSharedManager;

    private String warnType = "";

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        mSharedManager = new SharedManager(context);
        Log.i(TAG, "onReceive: ");
        Bundle bundle = intent.getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String content = bundle.getString(JPushInterface.EXTRA_ALERT);
        String type = bundle.getString(JPushInterface.EXTRA_EXTRA);
        int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        String file = bundle.getString(JPushInterface.EXTRA_MSG_ID);

        Log.i(TAG, "onReceive: title-->" + title);
        Log.i(TAG, "onReceive: content-->" + content);
        Log.i(TAG, "onReceive: type-->" + type);
        Log.i(TAG, "onReceive: notificationId-->" + notificationId);
        Log.i(TAG, "onReceive: file-->" + file);

        if (null == content) {
            return;
        }

        String token = mSharedManager.getToken();
        Log.i(TAG, "onReceive: token-->" + token);
        if (RegularU.isEmpty(token)) {
            toGuide();
        } else {
            toWarn();
        }

        Gson gson = new Gson();
        WarnTypeBean warnTypeBean = gson.fromJson(type, WarnTypeBean.class);
        warnType = warnTypeBean.getWarnType();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                NetManager netManager = new NetManager();
                netManager.pushClick(warnType);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    private void toGuide() {
        Intent intent = new Intent(context, GuideActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    private void toWarn() {
        Intent intent = new Intent(context, FragmentContentActivity.class);
        intent.putExtra(Data.MAIN_PAGE, 2);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
