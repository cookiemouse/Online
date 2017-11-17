package com.tianyigps.online.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DrivePath;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.DriveStep;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;
import com.baidu.mapapi.map.BaiduMap;
import com.google.gson.Gson;
import com.tianyigps.online.R;
import com.tianyigps.online.base.BaseActivity;
import com.tianyigps.online.bean.TerminalInfo4MapBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.data.StatusData;
import com.tianyigps.online.interfaces.OnShowTerminalInfoForMapListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.BDTransU;
import com.tianyigps.online.utils.GeoCoderU;
import com.tianyigps.online.utils.RegularU;
import com.tianyigps.online.utils.StatusU;
import com.tianyigps.online.utils.TimeFormatU;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.mapapi.BMapManager.getContext;

public class TrackGaodeActivity extends BaseActivity implements LocationSource.OnLocationChangedListener
        , AMap.InfoWindowAdapter, RouteSearch.OnRouteSearchListener {

    private static final String TAG = "TrackGaode";

    private MapView mMapView;
    private AMap mGaodeMap;

    private Marker mMarkerAdded;
    private Marker mMarkerVir;

    //  路径线
    private List<LatLng> mLatLngListLine;
    private Polyline mPolyline;

    private ImageView mImageViewLocate;
    private TextView mTextViewNormal, mTextViewSatellite, mTextViewAddress;

    private LatLng mLatLngSelf, mLatLngInfoBaidu, mLatLngInfoGaode;

    //  左下角定位，false = 定位手机，true = 定位车辆
    private boolean mIsLocateCar = false;

    private String mImei;

    private NetManager mNetManager;
    private SharedManager mSharedManager;
    private String mToken;
    private int mCid;
    private int mFlushInterval;

    private MyHandler myHandler;

    private String mStringMessage;

    //  InfoWindow数据
    private String mInfoName, mInfoSpeed, mInfoLocateType = "", mInfoCurrentTime, mInfoLocateTime = "", mInfoElectricity, mInfoImei;
    private StatusData mStatusData;
    private String mInfoStationCode = "";
    private int mInfoDirection, mModel, mElectricity;

    //  获取基站、获取GPS定位
    private boolean mWitchType = true;  //  true = gps, false = 基站定位的坐标
    private boolean mIsGPS = true;
    private boolean mIsShow = false;

    //  反编译地址
    private GeoCoderU mGeoCoderU;

    //  路线归划
    private RouteSearch mRouteSearch;
    //  是否需要缩放地图
    private boolean mZoomMap = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_gaode);

        init();

        setEventListener();

        mMapView.onCreate(savedInstanceState);
    }

    private void init() {
        this.setTitleText("车辆跟踪");

        mMapView = (MapView) findViewById(R.id.mv_activity_track_gaode);
        mGaodeMap = mMapView.getMap();

        UiSettings uiSettings = mGaodeMap.getUiSettings();
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);

        mImageViewLocate = (ImageView) findViewById(R.id.iv_activity_track_locate);
        mTextViewNormal = (TextView) findViewById(R.id.tv_activity_track_normal);
        mTextViewSatellite = (TextView) findViewById(R.id.tv_activity_track_satellite);
        mTextViewAddress = (TextView) findViewById(R.id.tv_activity_track_address);

        //  定位点
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        myLocationStyle.strokeColor(0x00000000);
        myLocationStyle.radiusFillColor(0x00000000);
        mGaodeMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mGaodeMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

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

        myHandler = new MyHandler();

        mGeoCoderU = new GeoCoderU();

        mRouteSearch = new RouteSearch(this);
        mLatLngListLine = new ArrayList<>();

        showPointNew();
    }

    private void setEventListener() {
        mGaodeMap.setInfoWindowAdapter(this);

        mRouteSearch.setRouteSearchListener(this);

        mImageViewLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsLocateCar) {
                    // TODO: 2017/9/5 定准车辆
                    if (null != mLatLngInfoGaode) {
                        moveToCenter(mLatLngInfoGaode);
                        mImageViewLocate.setImageResource(R.drawable.ic_location_self);
                    }
                    mIsLocateCar = false;
                    return;
                }
                Log.i(TAG, "onClick: mLatLngSelf-->" + mLatLngSelf);
                Location location = mGaodeMap.getMyLocation();
                mLatLngSelf = new LatLng(location.getLatitude(), location.getLongitude());
                moveToCenter(mLatLngSelf);
                mIsLocateCar = true;
                mImageViewLocate.setImageResource(R.drawable.ic_location_car);
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

        mGaodeMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i(TAG, "onMarkerClick: obj-->" + marker.getObject());
                if (marker.getObject().equals("marker")) {
                    addVirMaker(marker.getPosition());
                }
                return true;
            }
        });

        mGeoCoderU.setOnGetGeoGodeListener(new GeoCoderU.OnGetGeoCodeListener() {
            @Override
            public void onGetLatlng(double lat, double lng) {
            }

            @Override
            public void onGetAddress(String address) {
                mTextViewAddress.setText(address);
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

                mLatLngInfoBaidu = calculateLatlng(redisobjBean.getLocate_type()
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

                myHandler.sendEmptyMessageDelayed(Data.MSG_1, 400);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });
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

    //  高德地图定位回调
    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged: ");
        mLatLngSelf = new LatLng(location.getLatitude(), location.getLongitude());
    }

    @Override
    public View getInfoWindow(Marker marker) {
        View viewInfo = LayoutInflater.from(this).inflate(R.layout.view_map_info_window_track, null);

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
                if (null != mMarkerVir) {
                    mMarkerVir.hideInfoWindow();
                    mMarkerVir.remove();
                }
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

        return viewInfo;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
    }

    @Override
    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
        Log.i(TAG, "onDriveRouteSearched: ");
        mLatLngListLine.clear();
        List<DrivePath> drivePaths = driveRouteResult.getPaths();
        if (null != drivePaths && drivePaths.size() > 0) {
            DrivePath drivePath = drivePaths.get(0);
            if (null != drivePath) {
                List<DriveStep> driveSteps = drivePath.getSteps();
                if (null != driveSteps && driveSteps.size() > 0) {
                    for (DriveStep driveStep : driveSteps) {
                        List<LatLonPoint> latLonPoints = driveStep.getPolyline();
                        if (null != latLonPoints && latLonPoints.size() > 0) {
                            for (LatLonPoint latLonPoint : latLonPoints) {
                                mLatLngListLine.add(new LatLng(latLonPoint.getLatitude(), latLonPoint.getLongitude()));
                            }
                        }
                    }
                }
            }
        }

        //  绘制
        drawLine();
    }

    @Override
    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
    }

    @Override
    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
    }

    //  地图移动到目标点
    private void moveToCenter(LatLng latLng) {
        if (null == latLng) {
            return;
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, mGaodeMap.getCameraPosition().zoom, 0, 0));
        mGaodeMap.animateCamera(cameraUpdate);
    }

    //  改变地图层级
    private void changeZoom(LatLng... latLngs) {
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();//存放所有点的经纬度

        for (LatLng latLng : latLngs) {
            boundsBuilder.include(latLng);//把所有点都include进去（LatLng类型）
        }
        mGaodeMap.animateCamera(CameraUpdateFactory.newLatLngBounds(boundsBuilder.build(), 50));//第二个参数为四周留空宽度
        mZoomMap = false;
    }

    //  获取某台设备的信息，并显示infowindow
    private void showPointNew() {
        mNetManager.showTerminalInfo4Map(mToken, mImei);
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

    //  添加Marker
    private void addMarker(LatLng latLng, int type, int direction) {
        if (null == latLng) {
            return;
        }
        if (null != mMarkerAdded) {
            mMarkerAdded.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
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
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(viewMarker);
        markerOptions.position(latLng).icon(bitmapDescriptor).anchor(0.5f, 0.5f).rotateAngle(direction);

        mMarkerAdded = mGaodeMap.addMarker(markerOptions);
        mMarkerAdded.setObject("marker");
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
        View viewMarker = LayoutInflater.from(TrackGaodeActivity.this).inflate(R.layout.view_map_marker_vir, null);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(viewMarker);
        markerOptions.position(latLng).icon(bitmapDescriptor);

        mMarkerVir = mGaodeMap.addMarker(markerOptions);
        mMarkerVir.showInfoWindow();
        Log.i(TAG, "addVirMaker: ");
    }

    //  搜索路径
    private void searchDriving(LatLonPoint from, LatLonPoint to) {
        RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(from, to);
        RouteSearch.DriveRouteQuery query = new RouteSearch.DriveRouteQuery(fromAndTo, RouteSearch.DRIVING_SINGLE_DEFAULT, null, null, "");
        mRouteSearch.calculateDriveRouteAsyn(query);
    }

    //  绘制路径线
    private void drawLine() {
        if (null != mPolyline) {
            mPolyline.remove();
        }
        if (null != mLatLngListLine && mLatLngListLine.size() > 0) {
            PolylineOptions polylineOptions = new PolylineOptions();
            polylineOptions.addAll(mLatLngListLine);
            polylineOptions.width(8);
            polylineOptions.color(getResources().getColor(R.color.colorBlueTheme));
            mPolyline = mGaodeMap.addPolyline(polylineOptions);
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_MSG: {
                    TrackGaodeActivity.this.showToast(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    double[] latlng = BDTransU.bd2gcj(mLatLngInfoBaidu.latitude, mLatLngInfoBaidu.longitude);
                    mLatLngInfoGaode = new LatLng(latlng[0], latlng[1]);
                    addMarker(mLatLngInfoGaode, mStatusData.getStatu(), mInfoDirection);
                    addVirMaker(mLatLngInfoGaode);
                    mGeoCoderU.searchAddress(mLatLngInfoBaidu.latitude, mLatLngInfoBaidu.longitude);

                    if (null != mGaodeMap && null != mLatLngInfoGaode) {
                        Location location = mGaodeMap.getMyLocation();
                        if (null != location) {
                            mLatLngSelf = new LatLng(location.getLatitude(), location.getLongitude());
                        }

                        if (mZoomMap) {
                            changeZoom(mLatLngSelf, mLatLngInfoGaode);
                        }

                        LatLonPoint from = new LatLonPoint(mLatLngSelf.latitude, mLatLngSelf.longitude);
                        LatLonPoint to = new LatLonPoint(mLatLngInfoGaode.latitude, mLatLngInfoGaode.longitude);
                        searchDriving(from, to);
                    }
                    break;
                }
                case Data.MSG_2: {
                    break;
                }
            }
        }
    }
}
