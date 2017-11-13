package com.tianyigps.online.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.baidu.mapapi.map.BaiduMap;
import com.google.gson.Gson;
import com.tianyigps.online.R;
import com.tianyigps.online.bean.PathBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.data.DatePickerData;
import com.tianyigps.online.dialog.DatePickerDialogFragment;
import com.tianyigps.online.dialog.SpeedPickerDialogFragment;
import com.tianyigps.online.interfaces.OnFindHisPointsListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.TimeFormatU;

public class PathGaodeActivity extends AppCompatActivity {

    private static final String TAG = "PathGaode";

    private MapView mMapView;
    private AMap mGaodeMap;

    private TextView mTextViewTitle;
    private ImageView mImageViewBack, mImageViewDate, mImageViewSpeed;

    private TextView mTextViewNormal, mTextViewSatellite;

    private ImageView mImageViewPlay;
    private TextView mTextViewTime, mTextViewSpeed, mTextViewStart, mTextViewEnd;
    private SeekBar mSeekBar;

    //  速度选择
    SpeedPickerDialogFragment mSpeedPickerDialogFragment;
    private int mSpeed = Data.SPEED_100;

    //  日期选择
    DatePickerDialogFragment mDatePickerDialogFragment;

    private NetManager mNetManager;
    private SharedManager mSharedManager;
    private MyHandler myHandler;
    private String mToken;
    private String mImei;
    private String mStringMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_path_gaode);

        init();

        setEventListener();

        mMapView.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        mMapView.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    private void init() {

        mTextViewTitle = (TextView) findViewById(R.id.tv_activity_path_title);
        mImageViewBack = (ImageView) findViewById(R.id.iv_activity_path_1);
        mImageViewSpeed = (ImageView) findViewById(R.id.iv_activity_path_2);
        mImageViewDate = (ImageView) findViewById(R.id.iv_activity_path_3);

        mTextViewSatellite = (TextView) findViewById(R.id.tv_activity_path_satellite);
        mTextViewNormal = (TextView) findViewById(R.id.tv_activity_path_normal);

        mImageViewPlay = (ImageView) findViewById(R.id.iv_activity_path_play);
        mTextViewTime = (TextView) findViewById(R.id.tv_activity_path_time);
        mTextViewSpeed = (TextView) findViewById(R.id.tv_activity_path_speed);
        mTextViewStart = (TextView) findViewById(R.id.tv_activity_path_start);
        mTextViewEnd = (TextView) findViewById(R.id.tv_activity_path_end);

        mSeekBar = (SeekBar) findViewById(R.id.sb_activity_path);

        mMapView = (MapView) findViewById(R.id.mv_activity_path_gaode);
        mGaodeMap = mMapView.getMap();
        UiSettings uiSettings = mGaodeMap.getUiSettings();
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);

        Intent intent = getIntent();
        mImei = intent.getStringExtra(Data.INTENT_IMEI);
        mTextViewTitle.setText(intent.getStringExtra(Data.INTENT_NAME));

        mSpeedPickerDialogFragment = new SpeedPickerDialogFragment();
        mDatePickerDialogFragment = new DatePickerDialogFragment();

        mNetManager = new NetManager();
        mSharedManager = new SharedManager(this);
        mToken = mSharedManager.getToken();

        myHandler = new MyHandler();

        showDatePickerDialog();
    }

    private void setEventListener() {

        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mImageViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        mImageViewSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showPopup();
                showSpeedPickerDialog();
            }
        });

        mTextViewNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewNormal.setBackgroundResource(R.drawable.bg_map_control_select);
                mTextViewSatellite.setBackgroundResource(R.drawable.bg_map_control);
                mGaodeMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            }
        });

        mTextViewSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewNormal.setBackgroundResource(R.drawable.bg_map_control);
                mTextViewSatellite.setBackgroundResource(R.drawable.bg_map_control_select);
                mGaodeMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            }
        });

        mSpeedPickerDialogFragment.setOnChoiceSpeedListener(new SpeedPickerDialogFragment.OnChoiceSpeedListener() {
            @Override
            public void onChoice(int speed) {
                mSpeed = speed;
            }
        });

        mDatePickerDialogFragment.setOnChoiceDateListener(new DatePickerDialogFragment.OnChoiceDateListener() {
            @Override
            public void onChoice(DatePickerData datePickerData) {
                Log.i(TAG, "onChoice: start-->" + datePickerData.getStart());
                Log.i(TAG, "onChoice: end-->" + datePickerData.getEnd());
                getPath(datePickerData.getStart(), datePickerData.getEnd());
                String start = TimeFormatU.dateToHourMin(datePickerData.getStart());
                String end = TimeFormatU.dateToHourMin(datePickerData.getEnd());
                mTextViewStart.setText(start);
                mTextViewEnd.setText(end);
            }
        });

        mNetManager.setOnFindHisPointsListener(new OnFindHisPointsListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                PathBean pathBean = gson.fromJson(result, PathBean.class);
                if (!pathBean.isSuccess()) {
                    mStringMessage = pathBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                //  清除播放handler
                myHandler.removeMessages(Data.MSG_2);

                //  清除轨迹点
//                mMapListBeanList.clear();
//                PathBean.ObjBean objBean = pathBean.getObj();
//                if (!RegularU.isEmpty(objBean.getSpeedAlam())) {
//                    mSpeedLimit = Integer.valueOf(objBean.getSpeedAlam());
//                }
//                if (objBean.getMaplist().size() > 1) {
//                    for (PathBean.ObjBean.MaplistBean maplistBean : objBean.getMaplist()) {
//                        mMapListBeanList.add(maplistBean);
//                    }
//                }
                myHandler.sendEmptyMessage(Data.MSG_1);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });
    }

    //  获取轨迹数据
    private void getPath(String start, String end) {
        mNetManager.findHisPoints(mToken, mImei, start, end);
    }

    //  显示选择时间对话框
    private void showDatePickerDialog() {
        mDatePickerDialogFragment.show(getSupportFragmentManager(), "");
    }

    //  显示速度选择对话框
    private void showSpeedPickerDialog() {
        Bundle bundle = new Bundle();
        bundle.putInt(Data.KEY_SPEED, mSpeed);
        mSpeedPickerDialogFragment.setArguments(bundle);
        mSpeedPickerDialogFragment.show(getSupportFragmentManager(), "speed_picker");
    }

    //  显示信息对话框
    public void showMessageDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  do nothing
            }
        });
        builder.create().show();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_MSG: {
                    showMessageDialog(mStringMessage);
                    break;
                }
                case Data.MSG_NOTHING: {
                    break;
                }
                case Data.MSG_ERO: {
                    break;
                }
                case Data.MSG_1: {
                }
            }
        }
    }
}
