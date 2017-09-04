package com.tianyigps.online.manager;

import android.util.Log;

import com.tianyigps.online.data.Urls;
import com.tianyigps.online.interfaces.OnCheckUserListener;
import com.tianyigps.online.utils.MD5U;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by cookiemouse on 2017/9/4.
 */

public class NetManager {
    private static final String TAG = "NetManager";

    private OkHttpClient mOkHttpClient;
    private Request mRequest;

    private OnCheckUserListener mOnCheckUserListener;

    public NetManager() {
        mOkHttpClient = new OkHttpClient
                .Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    public void login(String userName, String password, String token) {
        Request.Builder builder = new Request.Builder();
        if ("".equals(password)) {
            builder.url(Urls.CHECK_USER + "userName=" + userName + "&token=" + token);
        } else {
            builder.url(Urls.CHECK_USER + "userName=" + userName
                    + "&password=" + MD5U.getMd5(password));
        }
        mRequest = builder.build();
        Log.i(TAG, "checkUser: url-OnCheckUserListener->" + mRequest.url());
        Call call = mOkHttpClient.newCall(mRequest);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if (null == mOnCheckUserListener) {
                    throw new NullPointerException("OnCheckUserListener is null");
                }
                mOnCheckUserListener.onFailure();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null == mOnCheckUserListener) {
                    throw new NullPointerException("OnCheckUserListener is null");
                }
                mOnCheckUserListener.onSuccess(response.body().string());
            }
        });
    }

    /**
     * 设置登录回调
     */
    public void setOnCheckUserListener(OnCheckUserListener listener) {
        this.mOnCheckUserListener = listener;
    }
}
