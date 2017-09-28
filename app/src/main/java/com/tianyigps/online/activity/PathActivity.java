package com.tianyigps.online.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.tianyigps.online.R;
import com.tianyigps.online.bean.PathBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.dialog.DatePickerDialogFragment;
import com.tianyigps.online.dialog.SpeedPickerDialogFragment;
import com.tianyigps.online.interfaces.OnFindHisPointsListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.TimeFormatU;

import java.util.ArrayList;
import java.util.List;

public class PathActivity extends AppCompatActivity {

    private static final String TAG = "PathActivity";

    private static final int MARKER_START = 1;
    private static final int MARKER_RUN = 2;
    private static final int MARKER_PAUSU = 3;
    private static final int MARKER_END = 4;

    private static final long PAUSE_TIME = 6 * 60 * 1000;

    private TextView mTextViewTitle;
    private ImageView mImageViewBack, mImageViewDate, mImageViewSpeed;

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private TextView mTextViewNormal, mTextViewSatellite;

    private ImageView mImageViewPlay;
    private TextView mTextViewTime, mTextViewSpeed, mTextViewStart, mTextViewEnd;
    private SeekBar mSeekBar;

    private NetManager mNetManager;
    private SharedManager mSharedManager;
    private MyHandler myHandler;
    private String mToken;
    private String mImei;
    private String mStringMessage;

    //  轨迹点
    private List<PathBean.ObjBean.MaplistBean> mMapListBeanList;
    private Overlay mOverlayLine;
    private Marker mMarkerStart, mMarkerEnd, mMarkerRun;
    private List<Marker> mMarkerPauseList;

    private int mProgress = 0;

    private boolean isPlaying = false, isPause = false;

    //  屏幕
    private WindowManager mWindowManager;
    private int mWidth, mHeight;

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

        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mWidth = mWindowManager.getDefaultDisplay().getWidth();
        mHeight = mWindowManager.getDefaultDisplay().getHeight();

        mMapView.setPadding(0, 0, 0, mHeight / 9);
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

        mImageViewPlay = (ImageView) findViewById(R.id.iv_activity_path_play);
        mTextViewTime = (TextView) findViewById(R.id.tv_activity_path_time);
        mTextViewSpeed = (TextView) findViewById(R.id.tv_activity_path_speed);
        mTextViewStart = (TextView) findViewById(R.id.tv_activity_path_start);
        mTextViewEnd = (TextView) findViewById(R.id.tv_activity_path_end);

        mSeekBar = (SeekBar) findViewById(R.id.sb_activity_path);

        Intent intent = getIntent();
        mImei = intent.getStringExtra(Data.INTENT_IMEI);
        mTextViewTitle.setText(intent.getStringExtra(Data.INTENT_NAME));

        mNetManager = new NetManager();
        mSharedManager = new SharedManager(this);
        mToken = mSharedManager.getToken();

        mMapListBeanList = new ArrayList<>();
        mMarkerPauseList = new ArrayList<>();

        myHandler = new MyHandler();

        showDatePickerDialog();

        getPath("2017-09-26 13:51:00", "2017-09-28 14:51:00");
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

        mImageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlaying) {
                    if (isPause) {
                        myHandler.sendEmptyMessage(Data.MSG_2);
                    } else {
                        myHandler.removeMessages(Data.MSG_2);
                    }
                    mImageViewPlay.setSelected(isPause);
                    isPause = !isPause;
                } else {
                    //  消除停留点Marker
                    if (mMarkerPauseList.size() > 0) {
                        for (Overlay overlay : mMarkerPauseList) {
                            overlay.remove();
                        }
                    }
                    mProgress = 0;
                    isPause = false;
                    isPlaying = true;
                    mImageViewPlay.setSelected(true);
                    myHandler.sendEmptyMessage(Data.MSG_2);
                }
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
                mMapListBeanList.clear();
                PathBean.ObjBean objBean = pathBean.getObj();
                if (objBean.getMaplist().size() > 1) {
                    for (PathBean.ObjBean.MaplistBean maplistBean : objBean.getMaplist()) {
                        mMapListBeanList.add(maplistBean);
                    }
                }
                myHandler.sendEmptyMessage(Data.MSG_1);
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

    //  显示速度选择对话框
    private void showSpeedPickerDialog() {
        SpeedPickerDialogFragment speedPickerDialogFragment = new SpeedPickerDialogFragment();
        speedPickerDialogFragment.show(getSupportFragmentManager(), "");
    }

    //  获取轨迹数据
    private void getPath(String start, String end) {
        mNetManager.findHisPoints(mToken, mImei, start, end);
    }

    //  计算停留点
    private boolean isPause(int position) {
        if (position < mMapListBeanList.size() - 1) {
            long time1 = TimeFormatU.dateToMillis(mMapListBeanList.get(position).getLocate_time());
            long time2 = TimeFormatU.dateToMillis(mMapListBeanList.get(position + 1).getLocate_time());

            return time2 - time1 > PAUSE_TIME;
        }
        return false;
    }

    //  绘制轨迹线
    private void addLine() {
        if (null != mOverlayLine) {
            mOverlayLine.remove();
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(8);
        polylineOptions.color(0xff00ff00);
        List<LatLng> latLngList = new ArrayList<>();
        for (PathBean.ObjBean.MaplistBean maplistBean : mMapListBeanList) {
            latLngList.add(new LatLng(maplistBean.getLatitudeF(), maplistBean.getLongitudeF()));
        }
        polylineOptions.points(latLngList);
        mOverlayLine = mBaiduMap.addOverlay(polylineOptions);
    }

    //  添加Marker
    private void addMaker(LatLng latLng, int type) {
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.anchor(0.5f, 1);
        markerOptions.position(latLng);
        switch (type) {
            case MARKER_START: {
                if (null != mMarkerStart) {
                    mMarkerStart.remove();
                }
                View view = LayoutInflater.from(this).inflate(R.layout.view_map_marker_start, null);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
                markerOptions.icon(bitmapDescriptor);
                mMarkerStart = (Marker) mBaiduMap.addOverlay(markerOptions);
                break;
            }
            case MARKER_RUN: {
                View view = LayoutInflater.from(this).inflate(R.layout.view_map_marker_run, null);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
                markerOptions.icon(bitmapDescriptor);
                mMarkerRun = (Marker) mBaiduMap.addOverlay(markerOptions);
                break;
            }
            case MARKER_PAUSU: {
                View view = LayoutInflater.from(this).inflate(R.layout.view_map_marker_pause, null);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
                markerOptions.icon(bitmapDescriptor);
                mMarkerPauseList.add((Marker) mBaiduMap.addOverlay(markerOptions));
                break;
            }
            case MARKER_END: {
                if (null != mMarkerEnd) {
                    mMarkerEnd.remove();
                }
                View view = LayoutInflater.from(this).inflate(R.layout.view_map_marker_end, null);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
                markerOptions.icon(bitmapDescriptor);
                mMarkerEnd = (Marker) mBaiduMap.addOverlay(markerOptions);
                break;
            }
            default: {
                Log.i(TAG, "addStartMaker: default-->" + type);
            }
        }
    }

    //  播放运动点
    private void moveRunMarker(LatLng latLng) {
        if (null == mMarkerRun) {
            addMaker(latLng, MARKER_RUN);
            return;
        }
        mMarkerRun.setPosition(latLng);
    }

    //  运动点是否在屏幕内
    private boolean isOutScreen(LatLng latLng) {
        Point point = mBaiduMap.getProjection().toScreenLocation(latLng);
        return (point.x < 0 || point.x > mWidth || point.y < 0 || point.y > mHeight * 7 / 8);
    }

    //  跳到地图中心
    private void moveToCenter(LatLng latLng) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng);
        MapStatus status = builder.build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        mBaiduMap.setMapStatus(update);
    }

    //  animeToCenter
    private void animToCenter(LatLng latLng) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng);
        MapStatus status = builder.build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        mBaiduMap.animateMapStatus(update);
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
                    int size = mMapListBeanList.size();
                    mSeekBar.setMax(size);
                    if (size > 1) {
                        PathBean.ObjBean.MaplistBean maplistBeanStart = mMapListBeanList.get(0);
                        if (null != maplistBeanStart) {
                            LatLng latLng = new LatLng(maplistBeanStart.getLatitudeF(), maplistBeanStart.getLongitudeF());
                            addMaker(latLng, MARKER_START);
                        }
                        PathBean.ObjBean.MaplistBean maplistBeanEnd = mMapListBeanList.get(size - 1);
                        if (null != maplistBeanEnd) {
                            LatLng latLng = new LatLng(maplistBeanEnd.getLatitudeF(), maplistBeanEnd.getLongitudeF());
                            addMaker(latLng, MARKER_END);
                        }
                        addLine();
                    }
                    break;
                }
                case Data.MSG_2: {
                    //  播放路径
                    if (mProgress < mMapListBeanList.size()) {
                        PathBean.ObjBean.MaplistBean maplistBeanStart = mMapListBeanList.get(mProgress);
                        String time = "时间：" + maplistBeanStart.getLocate_time();
                        String speed = maplistBeanStart.getSpeed() + " km/h";
                        mTextViewTime.setText(time);
                        mTextViewSpeed.setText(speed);
                        LatLng latLng = new LatLng(maplistBeanStart.getLatitudeF(), maplistBeanStart.getLongitudeF());
                        moveRunMarker(latLng);
                        if (isOutScreen(latLng)) {
                            moveToCenter(latLng);
                        }
                        if (isPause(mProgress)) {
                            addMaker(latLng, MARKER_PAUSU);
                        }
                        mSeekBar.setProgress(mProgress);
                        mProgress++;
                        myHandler.sendEmptyMessageDelayed(Data.MSG_2, 100);
                    } else {
                        isPlaying = false;
                        mImageViewPlay.setSelected(false);
                    }
                    break;
                }
            }
        }
    }
}
