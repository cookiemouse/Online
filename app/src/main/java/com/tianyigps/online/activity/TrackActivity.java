package com.tianyigps.online.activity;


import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.google.gson.Gson;
import com.tianyigps.online.R;
import com.tianyigps.online.base.BaseActivity;
import com.tianyigps.online.bean.TerminalInfo4MapBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.data.StatusData;
import com.tianyigps.online.interfaces.OnShowTerminalInfoForMapListener;
import com.tianyigps.online.manager.LocateManager;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.GeoCoderU;
import com.tianyigps.online.utils.RegularU;
import com.tianyigps.online.utils.StatusU;
import com.tianyigps.online.utils.TimeFormatU;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.mapapi.BMapManager.getContext;

public class TrackActivity extends BaseActivity {

    private static final String TAG = "TrackActivity";

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Overlay mOverlayMarker;
    //  地图路径
    private Overlay mOverlayLine;

    private ImageView mImageViewLocate;
    private TextView mTextViewNormal, mTextViewSatellite, mTextViewAddress;

    //  InfoWindow数据
    private String mInfoName, mInfoSpeed, mInfoLocateType = "", mInfoCurrentTime, mInfoLocateTime = "", mInfoElectricity, mInfoImei;
    private StatusData mStatusData;
    private String mInfoStationCode = "";
    private int mInfoDirection, mModel, mElectricity;
    private LatLng mInfoLatLng, mLatLngSelf;

    private LocateManager mLocateManager;
    private boolean mIsToCenter = false;
    //  左下角定位，false = 定位手机，true = 定位车辆
    private boolean mIsLocateCar = false;

    private GeoCoderU mGeoCoderU;

    //  路径规划
    private RoutePlanSearch mRoutePlanSearch;
    private boolean isFirst = true;

    private NetManager mNetManager;
    private SharedManager mSharedManager;
    private String mToken;
    private int mCid;
    private int mFlushInterval;

    private MyHandler myHandler;
    private String mStringMessage;

    private String mImei;

    //  屏幕
    private WindowManager mWindowManager;
    private int mWidth, mHeight;

    //  获取基站、获取GPS定位
    private boolean mWitchType = true;  //  true = gps, false = 基站定位的坐标
    private boolean mIsGPS = true;
    private boolean mIsShow = false;

    //  是否需要缩放地图
    private boolean mZoomMap = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track);

        init();

        setEventListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mWidth = mWindowManager.getDefaultDisplay().getWidth();
        mHeight = mWindowManager.getDefaultDisplay().getHeight();
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        myHandler.removeMessages(Data.MSG_2);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mMapView.onDestroy();
        mRoutePlanSearch.destroy();
        mLocateManager.stopLocate();
        super.onDestroy();
    }

    private void init() {
        this.setTitleText("车辆跟踪");

        mMapView = (MapView) findViewById(R.id.mv_activity_track);
        mBaiduMap = mMapView.getMap();
        //  开启地位图层
        mBaiduMap.setMyLocationEnabled(true);
        UiSettings uiSettings = mBaiduMap.getUiSettings();
        uiSettings.setCompassEnabled(false);
        uiSettings.setOverlookingGesturesEnabled(false);
        uiSettings.setRotateGesturesEnabled(false);

        mImageViewLocate = (ImageView) findViewById(R.id.iv_activity_track_locate);
        mTextViewNormal = (TextView) findViewById(R.id.tv_activity_track_normal);
        mTextViewSatellite = (TextView) findViewById(R.id.tv_activity_track_satellite);
        mTextViewAddress = (TextView) findViewById(R.id.tv_activity_track_address);

        Intent intent = getIntent();
        mImei = intent.getStringExtra(Data.KEY_IMEI);
        if (RegularU.isEmpty(mImei)) {
            mImei = "";
            Log.i(TAG, "init: imei号为空");
        }

        mNetManager = new NetManager();
        mSharedManager = new SharedManager(this);
        mToken = mSharedManager.getToken();
        mCid = mSharedManager.getCid();
        mFlushInterval = mSharedManager.getFlushTime() * 1000;

        mGeoCoderU = new GeoCoderU();

        mRoutePlanSearch = RoutePlanSearch.newInstance();

        myHandler = new MyHandler();

        mLocateManager = new LocateManager(this);
        mIsToCenter = false;
        mLocateManager.startLocate();

        showPointNew();
    }

    private void setEventListener() {
        mImageViewLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsLocateCar) {
                    // TODO: 2017/9/5 定准车辆
                    if (null != mInfoLatLng) {
                        moveToInfoCenter(mInfoLatLng);
                        mImageViewLocate.setImageResource(R.drawable.ic_location_self);
                    }
                    mIsLocateCar = false;
                    return;
                }
                if (null != mLatLngSelf) {
                    moveToCenter(mLatLngSelf);
                    mIsToCenter = false;
                    mLocateManager.startLocate();
                } else {
                    mIsToCenter = true;
                    mLocateManager.startLocate();
                }
                mIsLocateCar = true;
                mImageViewLocate.setImageResource(R.drawable.ic_location_car);
            }
        });

        mTextViewNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewNormal.setBackgroundResource(R.drawable.bg_map_control_select);
                mTextViewSatellite.setBackgroundResource(R.drawable.bg_map_control);
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
            }
        });

        mTextViewSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewNormal.setBackgroundResource(R.drawable.bg_map_control);
                mTextViewSatellite.setBackgroundResource(R.drawable.bg_map_control_select);
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                showPointNew();
                return false;
            }
        });

        mLocateManager.setOnReceiveLocationListener(new LocateManager.OnReceiveLocationListener() {
            @Override
            public void onReceive(LatLng latLng) {
                Log.i(TAG, "onReceive: latitude-->" + latLng.latitude + ", longitude-->" + latLng.longitude);

                mLatLngSelf = latLng;
                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(0)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100)
                        .latitude(latLng.latitude)
                        .longitude(latLng.longitude)
                        .build();
                // 设置定位数据
                mBaiduMap.setMyLocationData(locData);

                if (mIsToCenter) {
                    moveToCenter(latLng);
                }
                if (null != mInfoLatLng && isFirst) {
                    isFirst = false;
                    searchDriving(mLatLngSelf, mInfoLatLng);
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
                mTextViewAddress.setText(address);
            }
        });

        mRoutePlanSearch.setOnGetRoutePlanResultListener(new OnGetRoutePlanResultListener() {
            @Override
            public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
                Log.i(TAG, "onGetDrivingRouteResult: 1");
                mRoutePlanSearch.destroy();
            }

            @Override
            public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
                Log.i(TAG, "onGetDrivingRouteResult: 2");
                mRoutePlanSearch.destroy();
            }

            @Override
            public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {
                Log.i(TAG, "onGetDrivingRouteResult: 3");
                mRoutePlanSearch.destroy();
            }

            @Override
            public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
                Log.i(TAG, "onGetDrivingRouteResult: 4");
                if (null != mLatLngSelf && null != mInfoLatLng && mZoomMap) {
                    changeZoom(mLatLngSelf, mInfoLatLng);
                    mZoomMap = false;
                }
                if (null == drivingRouteResult || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //  未找到路线
                    Log.i(TAG, "onGetDrivingRouteResult: 未找到路线");
                    mStringMessage = "未找到路线";
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }

                List<LatLng> latLngList = new ArrayList<>();
                if (null != drivingRouteResult.getRouteLines()) {
                    DrivingRouteLine drivingRouteLine = drivingRouteResult.getRouteLines().get(0);
                    if (null != drivingRouteLine) {
                        if (null != drivingRouteLine.getAllStep()) {
                            for (DrivingRouteLine.DrivingStep drivingStep : drivingRouteLine.getAllStep()) {
                                if (null != drivingStep) {
                                    for (LatLng latlng : drivingStep.getWayPoints()) {
                                        latLngList.add(latlng);
                                    }
                                }
                            }
                        }
                    }
                }

                if (null != mOverlayLine) {
                    mOverlayLine.remove();
                }
                if (latLngList.size() > 2) {
                    PolylineOptions polylineOptions = new PolylineOptions();
                    polylineOptions.color(getResources().getColor(R.color.colorBlueTheme));
                    polylineOptions.width(8);
                    polylineOptions.points(latLngList);
                    mOverlayLine = mBaiduMap.addOverlay(polylineOptions);
                }
            }

            @Override
            public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
                Log.i(TAG, "onGetDrivingRouteResult: 5");
                mRoutePlanSearch.destroy();
            }

            @Override
            public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
                Log.i(TAG, "onGetDrivingRouteResult: 6");
                mRoutePlanSearch.destroy();
            }
        });

        mNetManager.setOnShowTerminalInfoForMapListener(new OnShowTerminalInfoForMapListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                TerminalInfo4MapBean terminalInfo4MapBean = gson.fromJson(result, TerminalInfo4MapBean.class);
                if (!terminalInfo4MapBean.isSuccess()) {
                    mStringMessage = terminalInfo4MapBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                TerminalInfo4MapBean.ObjBean objBean = terminalInfo4MapBean.getObj();
                TerminalInfo4MapBean.ObjBean.RedisobjBean redisobjBean = objBean.getRedisobj();

                Log.i(TAG, "onSuccess: name-->" + objBean.getName());
                Log.i(TAG, "onSuccess: speed-->" + redisobjBean.getSpeed());
                Log.i(TAG, "onSuccess: currentTime-->" + redisobjBean.getCurrent_time());
                Log.i(TAG, "onSuccess: locateTime-->" + redisobjBean.getLocate_time());
                Log.i(TAG, "onSuccess: locateType-->" + redisobjBean.getLocate_type());
                Log.i(TAG, "onSuccess: status-->" + redisobjBean.getAcc_status());
                Log.i(TAG, "onSuccess: stationCode-->" + redisobjBean.getStation_code());
                mInfoStationCode = redisobjBean.getStation_code();

                mInfoName = objBean.getName();
                if (redisobjBean.getLocate_type() == 1) {
                    int speedT = redisobjBean.getSpeed();
                    if (speedT < 0) {
                        mInfoSpeed = "0km/h";
                    } else {
                        mInfoSpeed = redisobjBean.getSpeed() + "km/h";
                    }
                } else {
                    mInfoSpeed = "-";
                }
                mInfoCurrentTime = redisobjBean.getCurrent_time();
                mInfoDirection = Integer.valueOf(redisobjBean.getDirection());
                mInfoElectricity = redisobjBean.getDianliang() + "%";
                mElectricity = redisobjBean.getDianliang();
                mInfoImei = objBean.getImei();

                mModel = Integer.valueOf(objBean.getModel());

                //  计算状态
                long currentTime = 0;
                long locateTime = 0;
                long parkTime = 0;
                int speed = 0;
                if (!RegularU.isEmpty(redisobjBean.getCurrent_time())) {
                    currentTime = TimeFormatU.dateToMillis2(redisobjBean.getCurrent_time());
                }
                if (!RegularU.isEmpty(redisobjBean.getLocate_time())) {
                    locateTime = TimeFormatU.dateToMillis2(redisobjBean.getLocate_time());
                }
                if (!RegularU.isEmpty(redisobjBean.getPark_time())) {
                    parkTime = TimeFormatU.dateToMillis2(redisobjBean.getPark_time());
                }
                speed = redisobjBean.getSpeed();

//                mInfoLatLng = new LatLng(redisobjBean.getLatitudeF(), redisobjBean.getLongitudeF());

                mInfoLatLng = calculateLatlng(redisobjBean.getLocate_type()
                        , redisobjBean.getLatitudeF()
                        , redisobjBean.getLongitudeF()
                        , redisobjBean.getLatitudeGDZJB()
                        , redisobjBean.getLongitudeGDZJB()
                        , redisobjBean.getGps_time()
                        , redisobjBean.getLocate_time()
                        , redisobjBean.getLbs_time());

                Log.i(TAG, "onSuccess: status-->" + StatusU.getStatus(mModel
                        , terminalInfo4MapBean.getTime()
                        , currentTime
                        , locateTime
                        , parkTime
                        , speed));
                mStatusData = StatusU.getStatus(Integer.valueOf(objBean.getModel())
                        , terminalInfo4MapBean.getTime()
                        , currentTime
                        , locateTime
                        , parkTime
                        , speed);

                myHandler.sendEmptyMessageDelayed(Data.MSG_1, 200);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });
    }

    //  地图移动到目标点
    private void moveToCenter(LatLng latLng) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng);
        MapStatus status = builder.build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        mBaiduMap.animateMapStatus(update);
    }

    //  地图移动到目标点
    private void moveToInfoCenter(LatLng latLng) {
        MapStatus.Builder builder = new MapStatus.Builder();
        Point point = new Point(mWidth / 2, mHeight / 2);
        builder.target(latLng).targetScreen(point);
        MapStatus status = builder.build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        mBaiduMap.animateMapStatus(update);
    }

    //  搜索路径
    private void searchDriving(LatLng from, LatLng to) {
        PlanNode planNodeFrom = PlanNode.withLocation(from);
        PlanNode planNodeTo = PlanNode.withLocation(to);
        mRoutePlanSearch.drivingSearch(new DrivingRoutePlanOption()
                .from(planNodeFrom)
                .to(planNodeTo)
                .policy(DrivingRoutePlanOption.DrivingPolicy.ECAR_DIS_FIRST));
    }

    //  添加marker
    private void addMarker(LatLng latLng, int type, int direction) {
        //定义Maker坐标点
        if (null == latLng) {
            return;
        }
        if (null != mOverlayMarker) {
            mOverlayMarker.remove();
        }
        //构建Marker图标
        View viewMarker;
        switch (type) {
            case Data.STATUS_RUNNING: {
                viewMarker = LayoutInflater.from(getContext()).inflate(R.layout.view_map_marker_car_green, null);
                break;
            }
            case Data.STATUS_OFF: {
                viewMarker = LayoutInflater.from(getContext()).inflate(R.layout.view_map_marker_car_gray, null);
                break;
            }
            case Data.STATUS_ON: {
                viewMarker = LayoutInflater.from(getContext()).inflate(R.layout.view_map_marker_car_red, null);
                break;
            }
            case Data.STATUS_OTHER: {
                viewMarker = LayoutInflater.from(getContext()).inflate(R.layout.view_map_marker_car_gray, null);
                break;
            }
            default: {
                viewMarker = LayoutInflater.from(getContext()).inflate(R.layout.view_map_marker_car_gray, null);
                Log.i(TAG, "addMarker: locate_type.default-->" + type);
            }
        }
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(viewMarker);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap)
                .rotate(direction)
                .anchor(0.5f, 0.5f);
        //在地图上添加Marker，并显示
        mOverlayMarker = mBaiduMap.addOverlay(option);
    }

    //  显示infoWindow
    private void showInfoWindow(final LatLng latLng) {
        View viewInfo = LayoutInflater.from(getContext()).inflate(R.layout.view_map_info_window_track, null);

        TextView tvName = viewInfo.findViewById(R.id.tv_view_info_window_track_title);
        TextView tvStatus = viewInfo.findViewById(R.id.tv_view_info_window_track_status_content);
        TextView tvSpeed = viewInfo.findViewById(R.id.tv_view_info_window_track_speed_content);
        TextView tvLocateType = viewInfo.findViewById(R.id.tv_view_info_window_track_type_content);
        TextView tvCurrentTime = viewInfo.findViewById(R.id.tv_view_info_window_track_current_time_content);
        TextView tvLocateTime = viewInfo.findViewById(R.id.tv_view_info_window_track_locate_time_content);
        TextView tvElectricity = viewInfo.findViewById(R.id.tv_view_info_window_track_electricity);
        TextView tvGetStation = viewInfo.findViewById(R.id.tv_view_info_window_track_get_station);
        ImageView imageViewClose = viewInfo.findViewById(R.id.iv_view_info_window_track_close);
        ImageView ivElectricity = viewInfo.findViewById(R.id.iv_view_info_window_track_electricity);
        ProgressBar pbElectricity = viewInfo.findViewById(R.id.pb_view_map_info_window_track);

        Log.i(TAG, "showInfoWindow: mIsShow-->" + mIsShow);
        if (mIsShow) {
            tvGetStation.setVisibility(View.VISIBLE);
        } else {
            tvGetStation.setVisibility(View.GONE);
        }

        if (mIsGPS) {
            tvGetStation.setText("获取GPS");
        } else {
            tvGetStation.setText("获取基站");
        }

        if (mModel == 1) {
            tvElectricity.setVisibility(View.GONE);
            ivElectricity.setVisibility(View.GONE);
            pbElectricity.setVisibility(View.GONE);
        } else {
            tvElectricity.setText(mInfoElectricity);
            tvElectricity.setVisibility(View.VISIBLE);
            ivElectricity.setVisibility(View.VISIBLE);
            pbElectricity.setVisibility(View.VISIBLE);
        }

        tvName.setText(mInfoName);
        tvStatus.setText(mStatusData.getStatus());
        tvSpeed.setText(mInfoSpeed);
        tvLocateType.setText(mInfoLocateType);
        tvCurrentTime.setText(mInfoCurrentTime);
        tvLocateTime.setText(mInfoLocateTime);
        pbElectricity.setProgress(mElectricity);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.hideInfoWindow();
            }
        });

        tvGetStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/10/13 获取基站
                Log.i(TAG, "onClick: mInfoStationCode-->" + mInfoStationCode);
                mWitchType = !mWitchType;
                showPointNew();
            }
        });

        InfoWindow mInfoWindow = new InfoWindow(viewInfo, latLng, 0);
        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);

        addMarker(latLng, mStatusData.getStatu(), mInfoDirection);
//        moveToInfoCenter(latLng);
    }

    //  获取某台设备的信息，并显示infowindow
    private void showPointNew() {
        mNetManager.showTerminalInfo4Map(mToken, mImei, 1);
    }

    //  改变地图zoom
    private void changeZoom(LatLng... latLngList) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngList) {
            builder.include(latLng);
        }
        LatLngBounds latLngBounds = builder.build();
        MapStatusUpdate update = MapStatusUpdateFactory.newLatLngBounds(latLngBounds);
        mBaiduMap.setMapStatus(update);
        Log.i(TAG, "changeZoom:");
        if (null != mInfoLatLng) {
            moveToCenter(mInfoLatLng);
        }
    }

    //  基站与GPS位置
    private LatLng calculateLatlng(int locateType, double latitudeF, double longitudeF
            , double latitudeG, double longitudeG
            , String gpsTime, String locateTime, String lbsTime) {
        LatLng latLng = new LatLng(latitudeF, longitudeF);

        Log.i(TAG, "calculateLatlng: mModel-->" + mModel);
        Log.i(TAG, "calculateLatlng: mIsGPS-->" + mIsGPS);
        Log.i(TAG, "calculateLatlng: locateType-->" + locateType);

        mIsGPS = mWitchType;

        if (1 == mModel) {
            //  有线
            if (mIsGPS) {
                mInfoLocateType = "GPS";
                if (0 != latitudeG && 0 != longitudeG) {
                    latLng = new LatLng(latitudeG, longitudeG);
                } else {
                    latLng = new LatLng(latitudeF, longitudeF);
                }
                if (1 == locateType) {
                    mIsShow = false;
                } else {
                    mIsShow = true;
                    mIsGPS = false;
                }
                if (!RegularU.isEmpty(gpsTime)) {
                    mInfoLocateTime = gpsTime;
                } else {
                    if (1 == locateType) {
                        mInfoLocateTime = locateTime;
                    } else {
                        mInfoLocateTime = "-";
                    }
                }
            } else {
                mInfoLocateType = "基站定位";
                latLng = new LatLng(latitudeF, longitudeF);
                if (0 != latitudeG && 0 != longitudeG) {
                    mIsShow = true;
                    mIsGPS = true;
                } else {
                    mIsShow = false;
                }
                if (!RegularU.isEmpty(lbsTime)) {
                    mInfoLocateTime = lbsTime;
                } else {
                    if (1 != locateType) {
                        mInfoLocateTime = locateTime;
                    } else {
                        mInfoLocateTime = "-";
                    }
                }
            }
        } else {
            //  无线
            latLng = new LatLng(latitudeF, longitudeF);
            mInfoLocateTime = locateTime;
            if (1 == locateType) {
                mInfoLocateType = "GPS";
            } else if (3 == locateType) {
                mInfoLocateType = "WiFi";
            } else {
                mInfoLocateType = "基站定位";
            }
        }
        return latLng;
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_MSG: {
                    TrackActivity.this.showToast(mStringMessage);
                    break;
                }
                case Data.MSG_ERO: {
                    break;
                }
                case Data.MSG_NOTHING: {
                    break;
                }
                case Data.MSG_1: {
                    //  获取到设备信息
                    myHandler.removeMessages(Data.MSG_2);
                    showInfoWindow(mInfoLatLng);
                    moveToCenter(mInfoLatLng);
                    mGeoCoderU.searchAddress(mInfoLatLng.latitude, mInfoLatLng.longitude);
                    if (null != mLatLngSelf) {
                        searchDriving(mLatLngSelf, mInfoLatLng);
                    }
                    Log.i(TAG, "handleMessage: mFlushInterval-->" + mFlushInterval);
                    myHandler.sendEmptyMessageDelayed(Data.MSG_2, mFlushInterval);
                    Log.i(TAG, "handleMessage: mInfoLatLng.lat-->" + mInfoLatLng.latitude);
                    Log.i(TAG, "handleMessage: mInfoLatLng.lng-->" + mInfoLatLng.longitude);
                    break;
                }
                case Data.MSG_2: {
                    //  刷新
                    showPointNew();
                }
            }
        }
    }
}
