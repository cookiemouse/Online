package com.tianyigps.online.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
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
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.tianyigps.online.R;
import com.tianyigps.online.activity.FragmentContentActivity;
import com.tianyigps.online.activity.MoreActivity;
import com.tianyigps.online.activity.NavigationActivity;
import com.tianyigps.online.activity.PathActivity;
import com.tianyigps.online.activity.TrackActivity;
import com.tianyigps.online.bean.InfoWindowBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.data.MarkerData;
import com.tianyigps.online.data.StatusData;
import com.tianyigps.online.dialog.ConcernDialogFragment;
import com.tianyigps.online.dialog.OverviewDialogFragment;
import com.tianyigps.online.interfaces.OnShowPointNewListener;
import com.tianyigps.online.manager.LocateManager;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.CoordinateConverterU;
import com.tianyigps.online.utils.GeoCoderU;
import com.tianyigps.online.utils.LocateTypeU;
import com.tianyigps.online.utils.RegularU;
import com.tianyigps.online.utils.StatusU;
import com.tianyigps.online.utils.TimeFormatU;
import com.tianyigps.online.utils.ToastU;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookiemouse on 2017/9/5.
 */

public class MonitorFragment extends Fragment {

    private static final String TAG = "MonitorFragment";

    private static final int FROM_CHOICE = 0;
    private static final int FROM_CONCERN = 1;
    private static final int FROM_OVERVIEW = 2;

    private int mFrom = 0;
    private boolean mOnlyInfo = false;

    private ImageView mImageViewTitle1, mImageViewTitle2, mImageViewTitle3;
    private ImageView mImageViewLocate, mImageViewLeft, mImageViewRight;
    private TextView mTextViewNormal, mTextViewSatellite, mTextViewAddress;

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private LocateManager mLocateManager;

    //  左下角定位，false = 定位手机，true = 定位车辆
    private boolean mIsLocateCar = false;

    //  已关注Fragment
    private ConcernDialogFragment mConcernDialogFragment;
    //  车辆概览Fragment
    private OverviewDialogFragment mOverviewDialogFragment;

    private NetManager mNetManager;
    private SharedManager mSharedManager;
    private MyHandler myHandler;
    private int mCid;
    private String mToken;

    //  从ChoiceCarFragment中传入的imei
    private String mChoiceImei;

    //  从ConcernDialogFragment传入的imei号，需存列表
    private List<String> mImeiList;

    private String mStringMessage;

    //  InfoWindow数据
    private String mInfoName, mInfoSpeed, mInfoLocateType, mInfoCurrentTime, mInfoLocateTime, mInfoElectricity, mInfoImei;
    private StatusData mStatusData;
    private int mInfoDirection, mModel;
    private LatLng mInfoLatLng;

    //  Marker
    private List<MarkerData> mMarkerDataList;
    private List<Overlay> mOverlayList;
    private Overlay mOverlayMarker;

    private ToastU mToastU;

    //  屏幕
    private WindowManager mWindowManager;
    private int mWidth, mHeight;

    //  轮播
    private int mCarousel = 0;

    //  地址反编码
    private GeoCoderU mGeoCoderU;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor, container, false);

        init(view);

        setEventListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
        Bundle bundle = getArguments();
        if (null != bundle) {
            mFrom = FROM_CHOICE;
            mChoiceImei = bundle.getString(Data.KEY_IMEI);
            Log.i(TAG, "onHiddenChanged: value-->" + mChoiceImei);
            showPointNew(mChoiceImei);

            mImeiList.clear();
            mMarkerDataList.clear();
            mSharedManager.saveShowAttention(false);
            removeAllMarker();
            mBaiduMap.hideInfoWindow();
            mImeiList.add(mChoiceImei);
        }

        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mWidth = mWindowManager.getDefaultDisplay().getWidth();
        mHeight = mWindowManager.getDefaultDisplay().getHeight();
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        if (null != mInfoLatLng) {
            moveToCenter(mInfoLatLng);
        }
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        mLocateManager.stopLocate();
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            Bundle bundle = getArguments();
            if (null != bundle) {
                mChoiceImei = bundle.getString(Data.KEY_IMEI);
                Log.i(TAG, "onHiddenChanged: value-->" + mChoiceImei);
                showPointNew(mChoiceImei);
            }
        } else {
            if (null != mInfoLatLng) {
                moveToCenter(mInfoLatLng);
            }
            if (mFrom == FROM_CONCERN) {
                mBaiduMap.hideInfoWindow();
                removeAllMarker();
                mImeiList.clear();
            }
        }
    }

    private void init(View view) {
        mMapView = view.findViewById(R.id.mv_fragment_monitor);
        mBaiduMap = mMapView.getMap();
        //  开启地位图层
        mBaiduMap.setMyLocationEnabled(true);

        mImageViewTitle1 = view.findViewById(R.id.iv_fragment_monitor_1);
        mImageViewTitle2 = view.findViewById(R.id.iv_fragment_monitor_2);
        mImageViewTitle3 = view.findViewById(R.id.iv_fragment_monitor_3);

        mImageViewLocate = view.findViewById(R.id.iv_layout_map_control_locate);
        mImageViewLeft = view.findViewById(R.id.iv_layout_map_control_left);
        mImageViewRight = view.findViewById(R.id.iv_layout_map_control_right);

        mTextViewNormal = view.findViewById(R.id.tv_layout_map_control_normal);
        mTextViewSatellite = view.findViewById(R.id.tv_layout_map_control_satellite);
        mTextViewAddress = view.findViewById(R.id.tv_layout_map_control_address);

        mLocateManager = new LocateManager(getContext());

        mOverlayList = new ArrayList<>();
        mMarkerDataList = new ArrayList<>();

        mImeiList = new ArrayList<>();

        mToastU = new ToastU(getContext());

        mConcernDialogFragment = new ConcernDialogFragment();
        mOverviewDialogFragment = new OverviewDialogFragment();

        mNetManager = new NetManager();
        mSharedManager = new SharedManager(getContext());
        mCid = mSharedManager.getCid();
        mToken = mSharedManager.getToken();

        mGeoCoderU = new GeoCoderU();

        if (mSharedManager.getShowAttention()) {
            showAttentionDevices();
        }

        myHandler = new MyHandler();
    }

    private void setEventListener() {
        mImageViewTitle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Activity activity = getActivity();
                FragmentContentActivity fragmentContentActivity = (FragmentContentActivity) activity;
                fragmentContentActivity.showChoiceCar();
            }
        });
        mImageViewTitle2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOverviewDialogFragment.show(getChildFragmentManager(), "车辆概览");
            }
        });
        mImageViewTitle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mConcernDialogFragment.show(getChildFragmentManager(), "关注列表");
            }
        });
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
                mLocateManager.startLocate();
            }
        });
        mImageViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = mImeiList.size();
                if (size > 0) {
                    if (mCarousel <= 0) {
                        mCarousel = size;
                    } else {
                        mCarousel--;
                    }
                    String imei = mImeiList.get(mCarousel);
                    mChoiceImei = imei;
                    showPointNew(imei);
                    mOnlyInfo = true;
                }
            }
        });
        mImageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = mImeiList.size();
                if (size > 0) {
                    if (mCarousel >= size - 1) {
                        mCarousel = 0;
                    } else {
                        mCarousel++;
                    }
                    String imei = mImeiList.get(mCarousel);
                    mChoiceImei = imei;
                    showPointNew(imei);
                    mOnlyInfo = true;
                }
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

        mLocateManager.setOnReceiveLocationListener(new LocateManager.OnReceiveLocationListener() {
            @Override
            public void onReceive(LatLng latLng) {
                Log.i(TAG, "onReceive: latitude-->" + latLng.latitude + ", longitude-->" + latLng.longitude);

                MyLocationData locData = new MyLocationData.Builder()
                        .accuracy(0)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                        .direction(100)
                        .latitude(latLng.latitude)
                        .longitude(latLng.longitude)
                        .build();
                // 设置定位数据
                mBaiduMap.setMyLocationData(locData);

                moveToCenter(latLng);
                mImageViewLocate.setImageResource(R.drawable.ic_location_car);
                mIsLocateCar = true;
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

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mOnlyInfo = true;
                Bundle bundle = marker.getExtraInfo();
                String imei = bundle.getString(Data.KEY_IMEI, "");
                if (RegularU.isEmpty(imei)) {
                    return false;
                }
                mChoiceImei = imei;
                showPointNew(imei);
                return false;
            }
        });

        mNetManager.setOnShowPointNewListener(new OnShowPointNewListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                InfoWindowBean infoWindowBean = gson.fromJson(result, InfoWindowBean.class);
                if (!infoWindowBean.isSuccess()) {
                    mStringMessage = infoWindowBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                if (mOnlyInfo) {
                    for (InfoWindowBean.ObjBean objBean : infoWindowBean.getObj()) {
                        if (objBean.getImei().equals(mChoiceImei)) {
                            InfoWindowBean.ObjBean.RedisobjBean redisobjBean = objBean.getRedisobj();
                            Log.i(TAG, "onSuccess: name-->" + objBean.getName());
                            Log.i(TAG, "onSuccess: speed-->" + redisobjBean.getSpeed());
                            Log.i(TAG, "onSuccess: currentTime-->" + redisobjBean.getCurrent_time());
                            Log.i(TAG, "onSuccess: locateTime-->" + redisobjBean.getLocate_time());
                            Log.i(TAG, "onSuccess: locateType-->" + redisobjBean.getLocate_type());
                            Log.i(TAG, "onSuccess: status-->" + redisobjBean.getAcc_status());

                            mInfoName = objBean.getName();
                            String locateType = redisobjBean.getLocate_type();
                            if (null == locateType) {
                                locateType = "2";
                            }
                            if (locateType.equals("1")) {
                                int speedT = Integer.valueOf(redisobjBean.getSpeed());
                                if (speedT < 0) {
                                    mInfoSpeed = "0km/h";
                                } else {
                                    mInfoSpeed = redisobjBean.getSpeed() + "km/h";
                                }
                            } else {
                                mInfoSpeed = "-";
                            }
                            mInfoLocateType = LocateTypeU.getLocateType(redisobjBean.getLocate_type());
                            mInfoCurrentTime = redisobjBean.getCurrent_time();
                            mInfoLocateTime = redisobjBean.getLocate_time();
                            mInfoDirection = Integer.valueOf(redisobjBean.getDirection());
                            mInfoElectricity = redisobjBean.getDianliang() + "%";
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
                            if (!RegularU.isEmpty(redisobjBean.getSpeed())) {
                                speed = Integer.valueOf(redisobjBean.getSpeed());
                            }

                            mInfoLatLng = new LatLng(redisobjBean.getLatitudeF(), redisobjBean.getLongitudeF());
                            Log.i(TAG, "onSuccess: status-->" + StatusU.getStatus(mModel
                                    , infoWindowBean.getTime()
                                    , currentTime
                                    , locateTime
                                    , parkTime
                                    , speed));
                            mStatusData = StatusU.getStatus(Integer.valueOf(objBean.getModel())
                                    , infoWindowBean.getTime()
                                    , currentTime
                                    , locateTime
                                    , parkTime
                                    , speed);
                        }
                    }

                    myHandler.sendEmptyMessage(Data.MSG_3);
                    return;
                }
                switch (mFrom) {
                    case FROM_OVERVIEW: {
                        for (InfoWindowBean.ObjBean objBean : infoWindowBean.getObj()) {
                            InfoWindowBean.ObjBean.RedisobjBean redisobjBean = objBean.getRedisobj();
                            LatLng latLng = new LatLng(redisobjBean.getLatitudeF(), redisobjBean.getLongitudeF());
                            int direction = Integer.valueOf(redisobjBean.getDirection());
                            String imei = objBean.getImei();

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
                            if (!RegularU.isEmpty(redisobjBean.getSpeed())) {
                                speed = Integer.valueOf(redisobjBean.getSpeed());
                            }
                            StatusData statusData = StatusU.getStatus(Integer.valueOf(objBean.getModel())
                                    , infoWindowBean.getTime()
                                    , currentTime
                                    , locateTime
                                    , parkTime
                                    , speed);

                            mMarkerDataList.add(new MarkerData(latLng, statusData.getStatu(), direction, imei));
                        }
                        myHandler.sendEmptyMessage(Data.MSG_2);
                        break;
                    }
                    case FROM_CONCERN: {
                    }
                    case FROM_CHOICE: {
                        for (InfoWindowBean.ObjBean objBean : infoWindowBean.getObj()) {
                            if (objBean.getImei().equals(mChoiceImei)) {
                                InfoWindowBean.ObjBean.RedisobjBean redisobjBean = objBean.getRedisobj();
                                Log.i(TAG, "onSuccess: name-->" + objBean.getName());
                                Log.i(TAG, "onSuccess: speed-->" + redisobjBean.getSpeed());
                                Log.i(TAG, "onSuccess: currentTime-->" + redisobjBean.getCurrent_time());
                                Log.i(TAG, "onSuccess: locateTime-->" + redisobjBean.getLocate_time());
                                Log.i(TAG, "onSuccess: locateType-->" + redisobjBean.getLocate_type());
                                Log.i(TAG, "onSuccess: status-->" + redisobjBean.getAcc_status());

                                mInfoName = objBean.getName();
                                String locateType = redisobjBean.getLocate_type();
                                if (null == locateType) {
                                    locateType = "2";
                                }
                                if (locateType.equals("1")) {
                                    int speedT = Integer.valueOf(redisobjBean.getSpeed());
                                    if (speedT < 0) {
                                        mInfoSpeed = "0km/h";
                                    } else {
                                        mInfoSpeed = redisobjBean.getSpeed() + "km/h";
                                    }
                                } else {
                                    mInfoSpeed = "-";
                                }
                                mInfoLocateType = LocateTypeU.getLocateType(redisobjBean.getLocate_type());
                                mInfoCurrentTime = redisobjBean.getCurrent_time();
                                mInfoLocateTime = redisobjBean.getLocate_time();
                                mInfoDirection = Integer.valueOf(redisobjBean.getDirection());
                                mInfoElectricity = redisobjBean.getDianliang() + "%";
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
                                if (!RegularU.isEmpty(redisobjBean.getSpeed())) {
                                    speed = Integer.valueOf(redisobjBean.getSpeed());
                                }

                                mInfoLatLng = new LatLng(redisobjBean.getLatitudeF(), redisobjBean.getLongitudeF());
                                Log.i(TAG, "onSuccess: status-->" + StatusU.getStatus(mModel
                                        , infoWindowBean.getTime()
                                        , currentTime
                                        , locateTime
                                        , parkTime
                                        , speed));
                                mStatusData = StatusU.getStatus(Integer.valueOf(objBean.getModel())
                                        , infoWindowBean.getTime()
                                        , currentTime
                                        , locateTime
                                        , parkTime
                                        , speed);
                            }
                        }
                        myHandler.sendEmptyMessage(Data.MSG_1);
                        break;
                    }
                }

            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });
    }

    //  添加marker
    private void addMarker(LatLng latLng, int type, int direction, String imei) {
        //定义Maker坐标点
        if (null == latLng) {
            return;
        }
        if (null != mOverlayMarker && mFrom == FROM_CHOICE) {
            mOverlayMarker.remove();
        }
        //构建Marker图标
        View viewMarker;
        switch (type) {
            case Data.STATUS_OFF: {
                viewMarker = LayoutInflater.from(getContext()).inflate(R.layout.view_map_marker_car_red, null);
                break;
            }
            case Data.STATUS_ON: {
                viewMarker = LayoutInflater.from(getContext()).inflate(R.layout.view_map_marker_car_green, null);
                break;
            }
            case Data.STATUS_OTHER: {
                viewMarker = LayoutInflater.from(getContext()).inflate(R.layout.view_map_marker_car_gray, null);
                break;
            }
            default: {
                viewMarker = LayoutInflater.from(getContext()).inflate(R.layout.view_map_marker_car_red, null);
                Log.i(TAG, "addMarker: locate_type.default-->" + type);
            }
        }
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(viewMarker);
        //构建MarkerOption，用于在地图上添加Marker
        Bundle bundle = new Bundle();
        bundle.putString(Data.KEY_IMEI, imei);
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap)
                .rotate(direction)
                .anchor(0.5f, 0.5f)
                .extraInfo(bundle);
        //在地图上添加Marker，并显示
        mOverlayMarker = mBaiduMap.addOverlay(option);
        mOverlayList.add(mOverlayMarker);
    }

    //  清除Marker
    private void removeAllMarker() {
        for (Overlay overlay : mOverlayList) {
            overlay.remove();
        }
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

    //  显示infoWindow
    private void showInfoWindow(LatLng latLng) {
        View viewInfo = LayoutInflater.from(getContext()).inflate(R.layout.view_map_info_window_monitor, null);

        TextView tvName = viewInfo.findViewById(R.id.tv_view_info_window_monitor_title);
        TextView tvStatus = viewInfo.findViewById(R.id.tv_view_info_window_monitor_status_content);
        TextView tvSpeed = viewInfo.findViewById(R.id.tv_view_info_window_monitor_speed_content);
        TextView tvLocateType = viewInfo.findViewById(R.id.tv_view_info_window_monitor_type_content);
        TextView tvCurrentTime = viewInfo.findViewById(R.id.tv_view_info_window_monitor_current_time_content);
        TextView tvLocateTime = viewInfo.findViewById(R.id.tv_view_info_window_monitor_locate_time_content);
        TextView tvElectricity = viewInfo.findViewById(R.id.tv_view_info_window_monitor_electricity);
        TextView tvTrack = viewInfo.findViewById(R.id.tv_view_info_window_monitor_track);
        TextView tvPath = viewInfo.findViewById(R.id.tv_view_info_window_monitor_path);
        TextView tvNavigation = viewInfo.findViewById(R.id.tv_view_info_window_monitor_navigation);
        TextView tvMore = viewInfo.findViewById(R.id.tv_view_info_window_monitor_more);
        ImageView imageViewClose = viewInfo.findViewById(R.id.iv_view_info_window_monitor_close);
        ImageView ivElectricity = viewInfo.findViewById(R.id.iv_view_info_window_monitor_electricity);

        if (mModel == 1) {
            tvElectricity.setVisibility(View.GONE);
            ivElectricity.setVisibility(View.GONE);
        } else {
            tvElectricity.setText(mInfoElectricity);
            tvElectricity.setVisibility(View.VISIBLE);
            ivElectricity.setVisibility(View.VISIBLE);
        }

        tvName.setText(mInfoName);
        Log.i(TAG, "showInfoWindow: mStatusData-->" + mStatusData);
        Log.i(TAG, "showInfoWindow: mStatusData.getStatus-->" + mStatusData.getStatus());
        Log.i(TAG, "showInfoWindow: mStatusData.getStatu-->" + mStatusData.getStatu());
        tvStatus.setText(mStatusData.getStatus());
        tvSpeed.setText(mInfoSpeed);
        tvLocateType.setText(mInfoLocateType);
        tvCurrentTime.setText(mInfoCurrentTime);
        tvLocateTime.setText(mInfoLocateTime);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.hideInfoWindow();
            }
        });

        tvTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToCenter(mInfoLatLng);
                toTrackActivity(mInfoImei);
            }
        });

        tvPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToCenter(mInfoLatLng);
                toPathActivity(mInfoImei, mInfoName);
            }
        });

        tvNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToCenter(mInfoLatLng);
                toNaviActivity();
            }
        });

        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveToCenter(mInfoLatLng);
                toMoreActivity(mInfoImei, mInfoName);
            }
        });

        InfoWindow mInfoWindow = new InfoWindow(viewInfo, latLng, 0);
        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);

        //  反编码地址
        getAddress(latLng);

        moveToInfoCenter(latLng);
    }

    //  获取某台设备的信息，并显示infowindow，公开给外部使用
    public void showPointNew(String imeiStr) {
//        boolean attention = mSharedManager.getShowAttention();
        mNetManager.showPointNew(mToken, mCid, "", imeiStr, false);
    }

    //  获取关注列表的设备信息，并添加Marker
    public void showPointNew() {
        mNetManager.showPointNew(mToken, mCid, "", "", true);
    }

    //  跳转到跟踪页面
    private void toTrackActivity(String imei) {
        Intent intent = new Intent(getContext(), TrackActivity.class);
        intent.putExtra(Data.KEY_IMEI, imei);
        startActivity(intent);
    }

    //  跳转到回放页面
    private void toPathActivity(String imei, String name) {
        Intent intent = new Intent(getContext(), PathActivity.class);
        intent.putExtra(Data.INTENT_IMEI, imei);
        intent.putExtra(Data.INTENT_NAME, name);
        startActivity(intent);
    }

    //  跳转到导航页面
    private void toNaviActivity() {
        Intent intent = new Intent(getContext(), NavigationActivity.class);
        com.amap.api.maps.model.LatLng latLng = CoordinateConverterU.baiduToGaodeLatlng(getContext(), mInfoLatLng);
        intent.putExtra(Data.INTENT_LATITUDE, latLng.latitude);
        intent.putExtra(Data.INTENT_LONGITUDE, latLng.longitude);
        startActivity(intent);
    }

    //  跳转到更多页面
    private void toMoreActivity(String imei, String name) {
        Intent intent = new Intent(getContext(), MoreActivity.class);
        intent.putExtra(Data.INTENT_IMEI, imei);
        intent.putExtra(Data.INTENT_NAME, name);
        startActivity(intent);
    }

    //  从ConcernDialogFragment传入imei，并存入数组
    public void showDevices(String imei) {
        mFrom = FROM_CONCERN;
        mChoiceImei = imei;
        boolean added = false;
        for (String str : mImeiList) {
            if (str.equals(imei)) {
                added = true;
                break;
            }
        }
        showPointNew(mChoiceImei);
        if (added) {
            return;
        }
        mImeiList.add(imei);
    }

    //  由OverviewDialogFragment调用，显示关注车辆
    public void showAttentionDevices() {
        mFrom = FROM_OVERVIEW;
        if (mSharedManager.getShowAttention()) {
            showPointNew();
        } else {
            removeAllMarker();
            mImeiList.clear();
            mMarkerDataList.clear();
            mBaiduMap.hideInfoWindow();
        }
    }

    //  反编码地址
    public void getAddress(LatLng latLng) {
        mGeoCoderU.searchAddress(latLng.latitude, latLng.longitude);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_ERO: {
                    break;
                }
                case Data.MSG_NOTHING: {
                    break;
                }
                case Data.MSG_MSG: {
                    mToastU.showToast(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    //  showPointNew单个
                    addMarker(mInfoLatLng, mStatusData.getStatu(), mInfoDirection, mChoiceImei);
                    showInfoWindow(mInfoLatLng);
                    break;
                }
                case Data.MSG_2: {
                    //  showPointNew多个
                    if (null != mMarkerDataList) {
                        for (MarkerData markerData : mMarkerDataList) {
                            mImeiList.add(markerData.getImei());
                            addMarker(markerData.getLatLng(), markerData.getType(), markerData.getDirection(), markerData.getImei());
                        }
                    }
                    break;
                }
                case Data.MSG_3: {
                    //  显示InfoWindow
                    showInfoWindow(mInfoLatLng);
                    mOnlyInfo = false;
                    break;
                }
            }
        }
    }
}
