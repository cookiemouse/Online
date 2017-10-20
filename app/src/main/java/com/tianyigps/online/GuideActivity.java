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
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baidu.mapapi.SDKInitializer;
import com.google.gson.Gson;
import com.tianyigps.online.activity.FragmentContentActivity;
import com.tianyigps.online.activity.LoginActivity;
import com.tianyigps.online.bean.CheckUserBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.interfaces.OnCheckUserListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.DeviceU;
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

    private NetManager mNetManager;

    private String mUserName, mPassword;
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

        Log.i(TAG, "init: VersionName-->" + DeviceU.getVersionName(this));
        Log.i(TAG, "init: VersionCode-->" + DeviceU.getVersionCode(this));
        Log.i(TAG, "init: DeviceId-->" + DeviceU.getDeviceId(this));
        Log.i(TAG, "init: PhoneBrand-->" + DeviceU.getPhoneBrand());
        Log.i(TAG, "init: PhoneModel-->" + DeviceU.getPhoneModel());
        Log.i(TAG, "init: BuildLevel-->" + DeviceU.getBuildLevel());
        Log.i(TAG, "init: BuildVersion-->" + DeviceU.getBuildVersion());
        Log.i(TAG, "init: DeviceWidth-->" + DeviceU.getDeviceWidth(this));
        Log.i(TAG, "init: DeviceHeight-->" + DeviceU.getDeviceHeight(this));

        mNetManager.sendUserInfo(DeviceU.getDeviceId(this)
                , DeviceU.getPhoneBrand() + "  " + DeviceU.getPhoneModel()
                , DeviceU.getVersionName(this)
                , "39.989584"
                , "116.480724"
                , "", "account", "2");

        applyPermiss();
    }

    private void setEventListener() {
        mNetManager.setOnCheckUserListener(new OnCheckUserListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                CheckUserBean checkUserBean = gson.fromJson(result, CheckUserBean.class);
                if (!checkUserBean.isSuccess()) {
                    myHandler.sendEmptyMessage(Data.MSG_ERO);
                    return;
                }

                myHandler.sendEmptyMessage(Data.MSG_2);
            }

            @Override
            public void onFailure() {
                myHandler.sendEmptyMessage(Data.MSG_ERO);
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
                        myHandler.sendEmptyMessageDelayed(Data.MSG_1, Data.DELAY_1000);
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

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_ERO: {
                    //  跳转到登录页
                    toLoginActivity();
                    break;
                }
                case Data.MSG_1: {
                    //  开始验证
                    login();
                    break;
                }
                case Data.MSG_2: {
                    //  跳转到主页
                    JPushInterface.setAlias(GuideActivity.this, 0, mUserName);
                    toFragmentContent();
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default." + msg.what);
                }
            }
        }
    }
}
