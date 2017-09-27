package com.tianyigps.online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.tianyigps.online.R;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.dialog.DatePickerDialogFragment;
import com.tianyigps.online.interfaces.OnFindHisPointsListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;

public class PathActivity extends AppCompatActivity {

    private TextView mTextViewTitle;
    private ImageView mImageViewBack, mImageViewDate, mImageViewSpeed;

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private TextView mTextViewNormal, mTextViewSatellite;

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
        setContentView(R.layout.activity_path);

        init();

        setEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
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

        mMapView = (MapView) findViewById(R.id.mv_activity_path);
        mBaiduMap = mMapView.getMap();
        //  开启地位图层
        mBaiduMap.setMyLocationEnabled(true);

        mTextViewSatellite = (TextView) findViewById(R.id.tv_activity_path_satellite);
        mTextViewNormal = (TextView) findViewById(R.id.tv_activity_path_normal);

        Intent intent = getIntent();
        mImei = intent.getStringExtra(Data.INTENT_IMEI);
        mTextViewTitle.setText(intent.getStringExtra(Data.INTENT_NAME));

        mNetManager = new NetManager();
        mSharedManager = new SharedManager(this);
        mToken = mSharedManager.getToken();

        myHandler = new MyHandler();
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
                showPopup();
            }
        });

        mTextViewSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                mTextViewSatellite.setBackgroundResource(R.drawable.bg_map_control_select);
                mTextViewNormal.setBackgroundResource(R.drawable.bg_map_control);
            }
        });

        mTextViewNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                mTextViewNormal.setBackgroundResource(R.drawable.bg_map_control_select);
                mTextViewSatellite.setBackgroundResource(R.drawable.bg_map_control);
            }
        });

        mNetManager.setOnFindHisPointsListener(new OnFindHisPointsListener() {
            @Override
            public void onSuccess(String result) {
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });
    }

    //  显示速度选择PopupWindow
    private void showPopup() {
        View viewPop = LayoutInflater.from(this).inflate(R.layout.view_popup_window, null);

        LinearLayout linearLayout = viewPop.findViewById(R.id.ll_view_popup_window);

        final PopupWindow popupWindow = new PopupWindow(viewPop
                , ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.MATCH_PARENT
                , true);

        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
            }
        });

        popupWindow.showAsDropDown(mTextViewNormal, 0, 0);
    }

    //  显示选择时间对话框
    private void showDatePickerDialog() {
        DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment();
        datePickerDialogFragment.show(getSupportFragmentManager(), "");
    }

    //  获取轨迹数据
    private void getPath(String start, String end) {
        mNetManager.findHisPoints(mToken, mImei, start, end);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_MSG: {
                    break;
                }
                case Data.MSG_NOTHING: {
                    break;
                }
                case Data.MSG_ERO: {
                    break;
                }
                case Data.MSG_1: {
                    //  获取轨迹数据
                    break;
                }
            }
        }
    }
}
