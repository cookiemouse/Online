package com.tianyigps.online;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.tianyigps.online.activity.FragmentContentActivity;
import com.tianyigps.online.activity.LoginActivity;
import com.tianyigps.online.bean.CheckUserBean;
import com.tianyigps.online.bean.CheckVersionBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.interfaces.OnCheckUserListener;
import com.tianyigps.online.interfaces.OnCheckVersionListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.RegularU;
import com.xsj.crasheye.Crasheye;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RationaleListener;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class GuideActivity extends Activity {

    private static final String TAG = "GuideActivity";

    private ImageView mImageView;

    //  Handler
    private MyHandler myHandler;

    private SharedManager mSharedManager;
    private String mStringMessage;

    private NetManager mNetManager;

    private String mUserName, mPassword, mCid;
    private boolean mIsAuto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        setContentView(R.layout.activity_guide);

        //  百度地图初始化
        SDKInitializer.initialize(getApplicationContext());

        //  初始化极光推送
        JPushInterface.init(getApplicationContext());
//        JPushInterface.setDebugMode(true);

        //  Crasheye
        Crasheye.init(this, "acaed6b0");

        init();

        setEventListener();
    }

    private void init() {
        mImageView = (ImageView) findViewById(R.id.iv_activity_guide);

        myHandler = new MyHandler();

        mNetManager = new NetManager();

        mSharedManager = new SharedManager(this);
        mUserName = mSharedManager.getAccount();
        mPassword = mSharedManager.getPassword();
        mIsAuto = mSharedManager.getAutoLogin();
        mCid = "" + mSharedManager.getCid();

        applyPermiss();
    }

    private void setEventListener() {
        mNetManager.setOnCheckUserListener(new OnCheckUserListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CheckUserBean checkUserBean = gson.fromJson(result, CheckUserBean.class);
                if (!checkUserBean.isSuccess()) {
                    mStringMessage = checkUserBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }

                myHandler.sendEmptyMessage(Data.MSG_2);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }
        });

        mNetManager.setCheckVersionListener(new OnCheckVersionListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                CheckVersionBean checkVersionBean = gson.fromJson(result, CheckVersionBean.class);
                if (!checkVersionBean.isSuccess()) {
                    myHandler.sendEmptyMessageDelayed(Data.MSG_1, Data.DELAY_1000);
                    return;
                }
                String version = checkVersionBean.getObj();
                if (!RegularU.isEmpty(version) && Float.valueOf(version) > 4) {
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                myHandler.sendEmptyMessageDelayed(Data.MSG_1, Data.DELAY_1000);
            }

            @Override
            public void onFailure() {
                myHandler.sendEmptyMessageDelayed(Data.MSG_1, Data.DELAY_1000);
            }
        });
    }

    //  登录
    private void login() {
        Log.i(TAG, "init: mUserName-->" + mUserName);
        Log.i(TAG, "init: mToken-->" + mPassword);
        if (mIsAuto && !RegularU.isEmpty(mPassword) && !RegularU.isEmpty(mUserName)) {
            mNetManager.login(mUserName, mPassword);
        } else {
            myHandler.sendEmptyMessage(Data.MSG_ERO);
        }
    }

    //  跳转至登录页
    private void toLoginActivity() {
        Intent intent = new Intent(GuideActivity.this, LoginActivity.class);
        startActivity(intent);
        this.finish();
    }

    //  跳转到主页
    private void toFragmentContent() {
        Intent intent = new Intent(GuideActivity.this, FragmentContentActivity.class);
        startActivity(intent);
        this.finish();
    }

    //  运行时权限
    private void applyPermiss() {
        AndPermission
                .with(GuideActivity.this)
                .requestCode(100)
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.CAMERA)
                .rationale(new RationaleListener() {
                    @Override
                    public void showRequestPermissionRationale(int requestCode, Rationale rationale) {
                        AndPermission.rationaleDialog(GuideActivity.this, rationale).show();
                    }
                })
                .callback(new PermissionListener() {
                    @Override
                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
                        myHandler.sendEmptyMessage(Data.MSG_3);
                    }

                    @Override
                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
                        if (AndPermission.hasAlwaysDeniedPermission(GuideActivity.this, deniedPermissions)) {
                            showMessageDialog();
                        }
                    }
                }).start();
    }

    private void showMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(GuideActivity.this);
        builder.setMessage("应用权限被禁止，请打开相关权限");
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                GuideActivity.this.finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //  显示信息对话框
    public void showMessageDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  do nothing
                myHandler.sendEmptyMessage(Data.MSG_1);
            }
        });
        builder.create().show();
    }

    //  显示Toast
    private void showToastCenter(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_ERO: {
                    //  跳转到登录页
                    if (!RegularU.isEmpty(mStringMessage)) {
                        showToastCenter(mStringMessage);
                    }
                    toLoginActivity();
                    break;
                }
                case Data.MSG_MSG: {
                    showMessageDialog("有新版本，请前往各大市场下载更新");
                    break;
                }
                case Data.MSG_1: {
                    //  开始验证
                    login();
                    break;
                }
                case Data.MSG_2: {
                    //  跳转到主页
                    JPushInterface.setAlias(GuideActivity.this, 0, mCid);
                    toFragmentContent();
                    break;
                }
                case Data.MSG_3: {
                    //  检查版本
                    mNetManager.checkVersion("1");
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default." + msg.what);
                }
            }
        }
    }
}
