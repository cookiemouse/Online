package com.tianyigps.online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableRow;

import com.google.gson.Gson;
import com.tianyigps.online.R;
import com.tianyigps.online.base.BaseActivity;
import com.tianyigps.online.bean.UnifenceOprBean;
import com.tianyigps.online.bean.UnifenceStatusBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.interfaces.OnGetUnifenceStatusListener;
import com.tianyigps.online.interfaces.OnUnifenceOprListener;
import com.tianyigps.online.manager.NetManager;

public class MoreActivity extends BaseActivity {
    private static final String TAG = "MoreActivity";

    private TableRow mTableRowDetails, mTableRowSet;
    private View mViewLine;
    private ImageView mImageViewManage;

    private NetManager mNetManager;
    private String mStringMessage;
    private String mImei;
    private boolean mStatus = false;
    private boolean mOpr = false;

    private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);

        init();

        setEventListener();
    }

    private void init() {
        mTableRowDetails = (TableRow) findViewById(R.id.tr_activity_more_details);
        mTableRowSet = (TableRow) findViewById(R.id.tr_activity_more_enclosure_set);
        mViewLine = findViewById(R.id.view_activity_more_line);
        mImageViewManage = (ImageView) findViewById(R.id.iv_activity_more_enclosure_manage);

        Intent intent = getIntent();
        mImei = intent.getStringExtra(Data.INTENT_IMEI);
        String name = intent.getStringExtra(Data.INTENT_NAME);
        this.setTitleText(name);

        myHandler = new MyHandler();

        mNetManager = new NetManager();

        getTerminalEnclosureStatus();
    }

    private void setEventListener() {
        mImageViewManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mStatus = !mStatus;
                setTerminalEncloseueStatus(mStatus);
                mImageViewManage.setSelected(mStatus);
            }
        });

        mTableRowDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 2017/9/25 设备详情
                toDetailsActivity();
            }
        });

        mTableRowSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/9/25 围栏设置
            }
        });

        mNetManager.setOnGetUnifenceStatusListener(new OnGetUnifenceStatusListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                UnifenceStatusBean unifenceStatusBean = gson.fromJson(result, UnifenceStatusBean.class);
                if (!unifenceStatusBean.isSuccess()) {
                    mStringMessage = Data.DEFAULT_MESSAGE;
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                if ("1".equals(unifenceStatusBean.getObj().getStatus())) {
                    mStatus = true;
                } else {
                    mStatus = false;
                }
                myHandler.sendEmptyMessage(Data.MSG_1);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });

        mNetManager.setOnUnifenceOprListener(new OnUnifenceOprListener() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                UnifenceOprBean unifenceOprBean = gson.fromJson(result, UnifenceOprBean.class);
                if (unifenceOprBean.isSuccess()) {
                    mOpr = mStatus;
                }else {
                    mOpr = !mStatus;

                }
                myHandler.sendEmptyMessage(Data.MSG_2);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });
    }

    //  获取该设备围栏是否已开启
    private void getTerminalEnclosureStatus() {
        mNetManager.getUnifenceStatus(mImei);
    }

    //  设置围栏是否开启
    private void setTerminalEncloseueStatus(boolean status) {
        int s;
        if (status) {
            s = 1;
        } else {
            s = 2;
        }
        mNetManager.unifenceOpr(mImei, s);
    }

    //  跳转到设备详情页面
    private void toDetailsActivity(){
        Intent intent = new Intent(MoreActivity.this, DetailsActivity.class);
        intent.putExtra(Data.INTENT_IMEI, mImei);
        startActivity(intent);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_ERO: {
                    break;
                }
                case Data.MSG_MSG: {
                    MoreActivity.this.showToast(mStringMessage);
                    break;
                }
                case Data.MSG_NOTHING: {
                    break;
                }
                case Data.MSG_1: {
                    //  获取设置是否打开围栏
                    mImageViewManage.setSelected(mStatus);
                    if (mStatus) {
                        mTableRowSet.setVisibility(View.VISIBLE);
                        mViewLine.setVisibility(View.VISIBLE);
                    } else {
                        mTableRowSet.setVisibility(View.GONE);
                        mViewLine.setVisibility(View.GONE);
                    }
                    break;
                }
                case Data.MSG_2: {
                    //  打开围栏是否成功
                    mStatus = mOpr;
                    mImageViewManage.setSelected(mStatus);
                    if (mStatus) {
                        mTableRowSet.setVisibility(View.VISIBLE);
                        mViewLine.setVisibility(View.VISIBLE);
                    } else {
                        mTableRowSet.setVisibility(View.GONE);
                        mViewLine.setVisibility(View.GONE);
                    }
                    break;
                }
            }
        }
    }
}
