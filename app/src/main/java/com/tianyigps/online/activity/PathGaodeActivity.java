package com.tianyigps.online.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
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
import com.tianyigps.online.utils.GeoCoderU;
import com.tianyigps.online.utils.RegularU;
import com.tianyigps.online.utils.TimeFormatU;

import java.util.ArrayList;
import java.util.List;

public class PathGaodeActivity extends AppCompatActivity implements AMap.InfoWindowAdapter {

    private static final String TAG = "PathGaode";

    private static final int MARKER_START = 1;
    private static final int MARKER_RUN = 2;
    private static final int MARKER_PAUSU = 3;
    private static final int MARKER_END = 4;

    private static final long PAUSE_TIME = 6 * 60 * 1000;

    private static final String KEY_STOP_TIME = "stop";
    private static final String KEY_START_TIME = "start";
    private static final String KEY_END_TIME = "end";
    private static final String KEY_LAT = "lat";
    private static final String KEY_LNG = "lng";

    private MapView mMapView;
    private AMap mGaodeMap;

    //  屏幕
    private WindowManager mWindowManager;
    private int mWidth, mHeight;

    private Marker mMarkerStart, mMarkerEnd, mMarkerRun, mMarkerVir;
    private List<Marker> mMarkerPauseList;
    private Polyline mPolyline;
    private List<Polyline> mPolylineSpeedList;
    private List<LatLng> mLatLngOverSpeedList;

    private TextView mTextViewTitle;
    private ImageView mImageViewBack, mImageViewDate, mImageViewSpeed;

    private TextView mTextViewNormal, mTextViewSatellite;

    private ImageView mImageViewPlay;
    private TextView mTextViewTime, mTextViewSpeed, mTextViewStart, mTextViewEnd;
    private SeekBar mSeekBar;

    private GeoCoderU mGeoCoderU;

    //  速度选择
    SpeedPickerDialogFragment mSpeedPickerDialogFragment;
    private int mSpeed = Data.SPEED_100;
    private int mProgress = 0;

    //  轨迹点
    private int mSpeedLimit = 999;
    private List<PathBean.ObjBean.MaplistBean> mMapListBeanList;

    //  停留点InfoWindow数据
    private String mInfoStopTime, mInfoStartTime, mInfoEndTime, mInfoAddress;
    private LatLng mLatLngInfo;

    private boolean isPlaying = false, isPause = false;

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

        mWindowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        mWidth = mWindowManager.getDefaultDisplay().getWidth();
        mHeight = mWindowManager.getDefaultDisplay().getHeight();
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
        myHandler.removeMessages(Data.MSG_2);
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View viewInfo = LayoutInflater.from(PathGaodeActivity.this).inflate(R.layout.view_map_info_window_path, null);

        TextView tvStopTime = viewInfo.findViewById(R.id.tv_view_info_window_path_stop);
        TextView tvStartTime = viewInfo.findViewById(R.id.tv_view_info_window_path_start);
        TextView tvEndTime = viewInfo.findViewById(R.id.tv_view_info_window_path_end);
        TextView tvAddress = viewInfo.findViewById(R.id.tv_view_info_window_path_address);

        tvStopTime.setText(mInfoStopTime);
        tvStartTime.setText(mInfoStartTime);
        tvEndTime.setText(mInfoEndTime);
        tvAddress.setText(mInfoAddress);

        ImageView imageViewClose = viewInfo.findViewById(R.id.iv_view_info_window_path_close);
        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMarkerVir.hideInfoWindow();
                mMarkerVir.remove();
            }
        });
        return viewInfo;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
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
        uiSettings.setZoomPosition(AMapOptions.ZOOM_POSITION_RIGHT_CENTER);

        Intent intent = getIntent();
        mImei = intent.getStringExtra(Data.INTENT_IMEI);
        mTextViewTitle.setText(intent.getStringExtra(Data.INTENT_NAME));

        mSpeedPickerDialogFragment = new SpeedPickerDialogFragment();
        mDatePickerDialogFragment = new DatePickerDialogFragment();

        mNetManager = new NetManager();
        mSharedManager = new SharedManager(this);
        mToken = mSharedManager.getToken();

        mMapListBeanList = new ArrayList<>();
        mMarkerPauseList = new ArrayList<>();
        mLatLngOverSpeedList = new ArrayList<>();
        mPolylineSpeedList = new ArrayList<>();

        myHandler = new MyHandler();

        mGeoCoderU = new GeoCoderU();

        showDatePickerDialog();
    }

    private void setEventListener() {
        mGaodeMap.setInfoWindowAdapter(this);

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
                mGaodeMap.setMapType(AMap.MAP_TYPE_NORMAL);
            }
        });

        mTextViewSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewNormal.setBackgroundResource(R.drawable.bg_map_control);
                mTextViewSatellite.setBackgroundResource(R.drawable.bg_map_control_select);
                mGaodeMap.setMapType(AMap.MAP_TYPE_SATELLITE);
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

        mImageViewPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mMapListBeanList && mMapListBeanList.size() < 1) {
                    mStringMessage = "轨迹数据不存在，请重新选择回放条件";
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                if (isPlaying) {
                    if (isPause) {
                        myHandler.sendEmptyMessage(Data.MSG_2);
                    } else {
                        myHandler.removeMessages(Data.MSG_2);
                    }
                    mImageViewPlay.setSelected(isPause);
                    isPause = !isPause;
                } else {
                    //  消除停留点
                    removeStopMarker();
                    //  消除InfoWindow
//                    mBaiduMap.hideInfoWindow();
                    mProgress = 0;
                    isPause = false;
                    isPlaying = true;
                    mImageViewPlay.setSelected(true);
                    myHandler.sendEmptyMessage(Data.MSG_2);
                }
            }
        });

        mGeoCoderU.setOnGetGeoGodeListener(new GeoCoderU.OnGetGeoCodeListener() {
            @Override
            public void onGetLatlng(double lat, double lng) {
            }

            @Override
            public void onGetAddress(String address) {
                Log.i(TAG, "onGetAddress: address-->" + address);
                mInfoAddress = address;
                if (null != mLatLngInfo){
                    addVirMaker(mLatLngInfo);
                }
            }
        });

        mGaodeMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i(TAG, "onMarkerClick: obj-->" + marker.getObject());
                Bundle bundle = (Bundle) marker.getObject();
                if (null == bundle) {
                    return true;
                }
                mInfoStopTime = bundle.getString(KEY_STOP_TIME);
                mInfoStartTime = bundle.getString(KEY_START_TIME);
                mInfoEndTime = bundle.getString(KEY_END_TIME);
                double lat = bundle.getDouble(KEY_LAT);
                double lng = bundle.getDouble(KEY_LNG);
                mLatLngInfo = new LatLng(lat, lng);
                mGeoCoderU.searchAddress(lat, lng);
                return true;
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
                mMapListBeanList.clear();
                PathBean.ObjBean objBean = pathBean.getObj();
                if (!RegularU.isEmpty(objBean.getSpeedAlam())) {
                    mSpeedLimit = Integer.valueOf(objBean.getSpeedAlam());
                }
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

    //  计算超速
    private boolean isOverSpeed(int position) {
        if (position < mMapListBeanList.size() - 1) {
//            int speed1 = mMapListBeanList.get(position).getSpeed();
            int speed2 = mMapListBeanList.get(position + 1).getSpeed();

            return speed2 > mSpeedLimit;
        }
        return false;
    }

    //  添加Marker
    private void addMaker(LatLng latLng, int type, Bundle bundle) {
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
                mMarkerStart = mGaodeMap.addMarker(markerOptions);
                break;
            }
            case MARKER_RUN: {
                View view = LayoutInflater.from(this).inflate(R.layout.view_map_marker_run, null);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
                markerOptions.icon(bitmapDescriptor);
                mMarkerRun = mGaodeMap.addMarker(markerOptions);
                break;
            }
            case MARKER_PAUSU: {
                View view = LayoutInflater.from(this).inflate(R.layout.view_map_marker_pause, null);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
                markerOptions.icon(bitmapDescriptor);
                Marker markerPause = mGaodeMap.addMarker(markerOptions);
                markerPause.setObject(bundle);
                mMarkerPauseList.add(markerPause);
                break;
            }
            case MARKER_END: {
                if (null != mMarkerEnd) {
                    mMarkerEnd.remove();
                }
                View view = LayoutInflater.from(this).inflate(R.layout.view_map_marker_end, null);
                BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(view);
                markerOptions.icon(bitmapDescriptor);
                mMarkerEnd = mGaodeMap.addMarker(markerOptions);
                break;
            }
            default: {
                Log.i(TAG, "addStartMaker: default-->" + type);
            }
        }
    }

    //  添加虚拟Marker，让其显示InfoWindow
    private void addVirMaker(LatLng latLng) {
        if (null == latLng) {
            return;
        }
        if (null != mMarkerVir) {
            mMarkerVir.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        View viewMarker = LayoutInflater.from(PathGaodeActivity.this).inflate(R.layout.view_map_marker_vir, null);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(viewMarker);
        markerOptions.position(latLng).icon(bitmapDescriptor).anchor(0.5f, 2f);

        mMarkerVir = mGaodeMap.addMarker(markerOptions);
        mMarkerVir.showInfoWindow();
        animToCenter(latLng);
        Log.i(TAG, "addVirMaker: ");
    }

    //  运动点是否在屏幕内
    private boolean isOutScreen(LatLng latLng) {
        Point point = mGaodeMap.getProjection().toScreenLocation(latLng);
        return (point.x < 0 || point.x > mWidth || point.y < 0 || point.y > mHeight * 7 / 8);
    }

    //  播放运动点
    private void moveRunMarker(LatLng latLng) {
        if (null == mMarkerRun) {
            addMaker(latLng, MARKER_RUN, null);
            return;
        }
        mMarkerRun.setPosition(latLng);
    }

    //  消除停留点
    private void removeStopMarker() {
        //  消除停留点Marker
        if (mMarkerPauseList.size() > 0) {
            for (Marker marker : mMarkerPauseList) {
                marker.remove();
            }
        }
    }

    //  清除超速线
    private void removeOverSpeedLine() {
        if (mPolylineSpeedList.size() > 0) {
            for (Polyline polyline : mPolylineSpeedList) {
                polyline.remove();
            }
        }
    }

    //  绘制轨迹线
    private void addLine() {
        if (null != mPolyline) {
            mPolyline.remove();
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(8);
        polylineOptions.color(0xff00ff00);
        List<LatLng> latLngList = new ArrayList<>();
        for (PathBean.ObjBean.MaplistBean maplistBean : mMapListBeanList) {
            latLngList.add(new LatLng(maplistBean.getLatitudeF(), maplistBean.getLongitudeF()));
        }
        polylineOptions.addAll(latLngList);
        mPolyline = mGaodeMap.addPolyline(polylineOptions);
    }

    //  绘制超速线
    private void addOverSpeedLine() {
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.width(8);
        polylineOptions.color(0xffff0000);
        polylineOptions.addAll(mLatLngOverSpeedList);
        mPolylineSpeedList.add(mGaodeMap.addPolyline(polylineOptions));
        mLatLngOverSpeedList.clear();
    }

    //  地图移动到目标点
    private void moveToCenter(LatLng latLng) {
        if (null == latLng) {
            return;
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, mGaodeMap.getCameraPosition().zoom, 0, 0));
        mGaodeMap.moveCamera(cameraUpdate);
    }

    //  animeToCenter
    private void animToCenter(LatLng latLng) {
        if (null == latLng) {
            return;
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, mGaodeMap.getCameraPosition().zoom, 0, 0));
        mGaodeMap.animateCamera(cameraUpdate);
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
                    //  获取轨迹数据

                    //  清除停留点marker
                    removeStopMarker();
                    //  清除停留点
                    mMarkerPauseList.clear();
                    //  清除开始点
                    if (null != mMarkerStart) {
                        mMarkerStart.remove();
                    }
                    //  清除停止点
                    if (null != mMarkerEnd) {
                        mMarkerEnd.remove();
                    }
                    //  清除运动点
                    if (null != mMarkerRun) {
                        mMarkerRun.remove();
                        mMarkerRun = null;
                    }
                    //  清除超速线
                    removeOverSpeedLine();
                    //  清除轨迹线
                    if (null != mPolyline) {
                        mPolyline.remove();
                    }
                    //  播放状态置空
                    mImageViewPlay.setSelected(false);
                    isPlaying = false;
                    isPause = false;
                    mTextViewTime.setText("");
                    mTextViewSpeed.setText("");

                    int size = mMapListBeanList.size();
                    mSeekBar.setMax(size - 1);
                    if (size > 1) {
                        PathBean.ObjBean.MaplistBean maplistBeanStart = mMapListBeanList.get(0);
                        if (null != maplistBeanStart) {
                            LatLng latLng = new LatLng(maplistBeanStart.getLatitudeF(), maplistBeanStart.getLongitudeF());
                            addMaker(latLng, MARKER_START, null);
                        }
                        PathBean.ObjBean.MaplistBean maplistBeanEnd = mMapListBeanList.get(size - 1);
                        if (null != maplistBeanEnd) {
                            LatLng latLng = new LatLng(maplistBeanEnd.getLatitudeF(), maplistBeanEnd.getLongitudeF());
                            addMaker(latLng, MARKER_END, null);
                        }
                        //  绘制轨迹
                        addLine();

                        //  绘制超速线
                        for (int i = 0; i < size; i++) {
                            PathBean.ObjBean.MaplistBean maplistBean = mMapListBeanList.get(i);
                            LatLng latLng = new LatLng(maplistBean.getLatitudeF(), maplistBean.getLongitudeF());
                            if (isOverSpeed(i)) {
                                mLatLngOverSpeedList.add(latLng);
                            } else if (mLatLngOverSpeedList.size() > 0) {
                                mLatLngOverSpeedList.add(latLng);
                                //  绘制
                                addOverSpeedLine();
                            }
                        }
                    }
                    break;
                }
                case Data.MSG_2: {
                    //  播放路径
                    if (mProgress < mMapListBeanList.size()) {
                        PathBean.ObjBean.MaplistBean maplistBean = mMapListBeanList.get(mProgress);
                        String time = "时间：" + maplistBean.getLocate_time();
                        String speed = maplistBean.getSpeed() + " km/h";
                        mTextViewTime.setText(time);
                        mTextViewSpeed.setText(speed);
                        LatLng latLng = new LatLng(maplistBean.getLatitudeF(), maplistBean.getLongitudeF());
                        moveRunMarker(latLng);
                        if (isOutScreen(latLng)) {
                            moveToCenter(latLng);
                        }
                        if (isPause(mProgress)) {
                            PathBean.ObjBean.MaplistBean maplistBeanNext = mMapListBeanList.get(mProgress + 1);
                            long start = TimeFormatU.dateToMillis(maplistBean.getLocate_time());
                            long end = TimeFormatU.dateToMillis(maplistBeanNext.getLocate_time());

                            Bundle bundle = new Bundle();
                            bundle.putString(KEY_STOP_TIME, TimeFormatU.millsToMinSec2(end - start));
                            bundle.putString(KEY_START_TIME, maplistBean.getLocate_time());
                            bundle.putString(KEY_END_TIME, maplistBeanNext.getLocate_time());
                            bundle.putDouble(KEY_LAT, latLng.latitude);
                            bundle.putDouble(KEY_LNG, latLng.longitude);
                            addMaker(latLng, MARKER_PAUSU, bundle);
                        }
                        mSeekBar.setProgress(mProgress);
                        mProgress++;
                        myHandler.sendEmptyMessageDelayed(Data.MSG_2, mSpeed);
                    } else {
                        isPlaying = false;
                        mImageViewPlay.setSelected(false);
                        if (mMapListBeanList.size() > 0) {
                            mTextViewTime.setText("回放结束");
                            mTextViewSpeed.setText("");
                        }
                    }
                    break;
                }
            }
        }
    }
}
