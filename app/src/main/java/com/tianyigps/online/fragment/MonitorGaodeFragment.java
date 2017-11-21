package com.tianyigps.online.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Location;
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
import android.widget.ProgressBar;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.Projection;
import com.amap.api.maps.TextureMapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.gson.Gson;
import com.tianyigps.online.R;
import com.tianyigps.online.activity.FragmentContentActivity;
import com.tianyigps.online.activity.MoreActivity;
import com.tianyigps.online.activity.NavigationActivity;
import com.tianyigps.online.activity.PathGaodeActivity;
import com.tianyigps.online.activity.TrackGaodeActivity;
import com.tianyigps.online.bean.InfoWindowBean;
import com.tianyigps.online.bean.MonitorData;
import com.tianyigps.online.bean.StationBean;
import com.tianyigps.online.cluster.CookieClusterGaode;
import com.tianyigps.online.cluster.GaodePoint;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.data.StatusData;
import com.tianyigps.online.dialog.ConcernDialogFragment;
import com.tianyigps.online.dialog.OverviewDialogFragment;
import com.tianyigps.online.interfaces.OnGetStationInfoListener;
import com.tianyigps.online.interfaces.OnShowPointNewListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.BDTransU;
import com.tianyigps.online.utils.GeoCoderU;
import com.tianyigps.online.utils.RegularU;
import com.tianyigps.online.utils.StatusU;
import com.tianyigps.online.utils.TimeFormatU;
import com.tianyigps.online.utils.ToastU;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookiemouse on 2017/9/5.
 */

public class MonitorGaodeFragment extends Fragment implements AMap.InfoWindowAdapter {

    private static final String TAG = "MonitorGaode";

    private static final int FROM_CHOICE = 0;
    private static final int FROM_CONCERN = 1;
    private static final int FROM_OVERVIEW = 2;

    private int mFrom = 0;

    //  InfoWindow是否打开
    private boolean mIsOpenInfo = true;

    private ImageView mImageViewTitle1, mImageViewTitle2, mImageViewTitle3;
    private ImageView mImageViewLocate, mImageViewLeft, mImageViewRight;
    private TextView mTextViewNormal, mTextViewSatellite, mTextViewAddress;

    private TextureMapView mMapView;
    private AMap mGaodeMap;

    private Marker mMarkerVir;

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
    private String mChoiceImei = "";

    //  从ConcernDialogFragment传入的imei号，需存列表
    private List<String> mImeiList;

    //  从OverviewDialogFragment传入的cid
    private String mCidStr = "";

    private String mStringMessage;

    //  InfoWindow数据
    private String mInfoName, mInfoSpeed, mInfoLocateType, mInfoCurrentTime, mInfoLocateTime, mInfoElectricity, mInfoImei;
    private boolean mIsShowGetStation = false;
    private String mInfoStationCode = "";
    private StatusData mStatusData;
    private int mInfoDirection, mModel, mElectricity;
    private LatLng mInfoLatLng;
    //  设备是否已启用
    private boolean mIsActivation = true;

    //  获取基站，基站坐标
    private LatLng mLatLngStation;
    private boolean mIsStation = false;

    //  Marker
    private List<MonitorData> mMonitorDataList;
    private List<Marker> mMarkerList;

    private ToastU mToastU;

    //  屏幕
    private WindowManager mWindowManager;
    private int mWidth, mHeight;

    //  轮播
    private int mCarousel = 0;

    //  地址反编码
    private GeoCoderU mGeoCoderU;

    //  Cluster
    private List<GaodePoint> mGaodePointList, mGaodePointListShow;
    private CookieClusterGaode mCookieClusterGaode;

    //  刷新
    private int mFlushTime;
    //  是否移动到中心
    private boolean mIsToCenter = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_monitor_gaode, container, false);

        init(view);
        mMapView.onCreate(savedInstanceState);

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
            mIsOpenInfo = true;
            mIsToCenter = true;
            showPointNew(mChoiceImei);

            mImeiList.clear();
            mSharedManager.saveShowAttention(false);
            removeAllMarker();
            mImeiList.add(mChoiceImei);
        }

        mWindowManager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        mWidth = mWindowManager.getDefaultDisplay().getWidth();
        mHeight = mWindowManager.getDefaultDisplay().getHeight();

        myHandler.sendEmptyMessageDelayed(Data.MSG_5, mFlushTime);
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        myHandler.removeMessages(Data.MSG_5);
        super.onPause();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mFlushTime = mSharedManager.getFlushTime() * 1000;
            myHandler.sendEmptyMessageDelayed(Data.MSG_5, mFlushTime);
            Bundle bundle = getArguments();
            if (null != bundle) {
                mFrom = FROM_CHOICE;
                mChoiceImei = bundle.getString(Data.KEY_IMEI);
                mIsOpenInfo = true;
                mIsToCenter = true;
                Log.i(TAG, "onHiddenChanged: value-->" + mChoiceImei);
                showPointNew(mChoiceImei);
                mImeiList.clear();
                mSharedManager.saveShowAttention(false);
                removeAllMarker();
                mImeiList.add(mChoiceImei);
            }
        } else {
            myHandler.removeMessages(Data.MSG_5);
            if (mFrom == FROM_CONCERN) {
                if (mSharedManager.getShowAttention()) {
                    removeAllMarker();
                    mImeiList.clear();
                }
            }
        }
    }

    @Override
    public View getInfoWindow(Marker marker) {
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
        TextView tvGetStation = viewInfo.findViewById(R.id.tv_view_info_window_monitor_get_station);
        ImageView imageViewClose = viewInfo.findViewById(R.id.iv_view_info_window_monitor_close);
        ImageView ivElectricity = viewInfo.findViewById(R.id.iv_view_info_window_monitor_electricity);
        ProgressBar pbElectricity = viewInfo.findViewById(R.id.pb_view_map_info_window_monitor);

        if (mIsStation) {
            tvGetStation.setText("获取GPS");
        } else {
            tvGetStation.setText("获取基站");
        }

        if (mIsShowGetStation) {
            tvGetStation.setVisibility(View.VISIBLE);
        } else {
            tvGetStation.setVisibility(View.GONE);
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
        Log.i(TAG, "showInfoWindow: mStatusData-->" + mStatusData);
        Log.i(TAG, "showInfoWindow: mStatusData.getStatus-->" + mStatusData.getStatus());
        Log.i(TAG, "showInfoWindow: mStatusData.getStatu-->" + mStatusData.getStatu());
        tvStatus.setText(mStatusData.getStatus());
        tvSpeed.setText(mInfoSpeed);
        tvLocateType.setText(mInfoLocateType);
        tvCurrentTime.setText(mInfoCurrentTime);
        tvLocateTime.setText(mInfoLocateTime);
        pbElectricity.setProgress(mElectricity);

        tvGetStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/10/13 获取基站
                Log.i(TAG, "onClick: mInfoStationCode-->" + mInfoStationCode);
                if (mIsStation) {
                    showPointNew(mChoiceImei);
                    mIsStation = false;
                } else {
                    mNetManager.getStationInfo(mInfoStationCode);
                }
//                mWitchType = !mWitchType;
            }
        });

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIsOpenInfo = false;
                if (null != mMarkerVir) {
                    mMarkerVir.hideInfoWindow();
                    mMarkerVir.remove();
                }
            }
        });

        tvTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toTrackActivity(mInfoImei);
            }
        });

        tvPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toPathActivity(mInfoImei, mInfoName);
            }
        });

        tvNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toNaviActivity();
            }
        });

        tvMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toMoreActivity(mInfoImei, mInfoName);
            }
        });
        return viewInfo;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void init(View view) {
        mMapView = view.findViewById(R.id.mv_fragment_monitor_gaode);
        mGaodeMap = mMapView.getMap();
        UiSettings uiSettings = mGaodeMap.getUiSettings();
        uiSettings.setRotateGesturesEnabled(false);
        uiSettings.setTiltGesturesEnabled(false);
        //  开启地位图层
        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);
        myLocationStyle.strokeColor(0x00000000);
        myLocationStyle.radiusFillColor(0x00000000);
        mGaodeMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mGaodeMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        mImageViewTitle1 = view.findViewById(R.id.iv_fragment_monitor_1_gaode);
        mImageViewTitle2 = view.findViewById(R.id.iv_fragment_monitor_2_gaode);
        mImageViewTitle3 = view.findViewById(R.id.iv_fragment_monitor_3_gaode);

        mImageViewLocate = view.findViewById(R.id.iv_layout_map_control_locate);
        mImageViewLeft = view.findViewById(R.id.iv_layout_map_control_left);
        mImageViewRight = view.findViewById(R.id.iv_layout_map_control_right);

        mTextViewNormal = view.findViewById(R.id.tv_layout_map_control_normal);
        mTextViewSatellite = view.findViewById(R.id.tv_layout_map_control_satellite);
        mTextViewAddress = view.findViewById(R.id.tv_layout_map_control_address);

        mMarkerList = new ArrayList<>();
        mMonitorDataList = new ArrayList<>();

        mImeiList = new ArrayList<>();

        mToastU = new ToastU(getContext());

        mConcernDialogFragment = new ConcernDialogFragment();
        mOverviewDialogFragment = new OverviewDialogFragment();

        mNetManager = new NetManager();
        mSharedManager = new SharedManager(getContext());
        mCid = mSharedManager.getCid();
        mToken = mSharedManager.getToken();
        mFlushTime = mSharedManager.getFlushTime() * 1000;

        mGeoCoderU = new GeoCoderU();

        mGaodePointList = new ArrayList<>();
        mGaodePointListShow = new ArrayList<>();
        mCookieClusterGaode = new CookieClusterGaode(mGaodePointList);

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

        mGaodeMap.setInfoWindowAdapter(this);

        mImageViewLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsLocateCar) {
                    Log.i(TAG, "onClick: mImageViewLocate");
                    if (null != mInfoLatLng) {
                        moveToCenter(mInfoLatLng);
                        mImageViewLocate.setImageResource(R.drawable.ic_location_self);
                    }
                    mIsLocateCar = false;
                    return;
                }
                Location location = mGaodeMap.getMyLocation();
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                moveToCenter(latLng);
                mIsLocateCar = true;
                mImageViewLocate.setImageResource(R.drawable.ic_location_car);
            }
        });
        mImageViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = mMonitorDataList.size();
                if (size > 0) {
                    mIsOpenInfo = true;
                    if (mCarousel <= 0) {
                        mCarousel = size - 1;
                    } else {
                        mCarousel--;
                    }
                    mIsToCenter = true;
                    String imei = mMonitorDataList.get(mCarousel).getImei();
                    mChoiceImei = imei;
                    myHandler.sendEmptyMessage(Data.MSG_2);
                }
            }
        });
        mImageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int size = mMonitorDataList.size();
                if (size > 0) {
                    mIsOpenInfo = true;
                    if (mCarousel >= size - 1) {
                        mCarousel = 0;
                    } else {
                        mCarousel++;
                    }
                    mIsToCenter = true;
                    String imei = mMonitorDataList.get(mCarousel).getImei();
                    mChoiceImei = imei;
                    myHandler.sendEmptyMessage(Data.MSG_2);
                }
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

        mGeoCoderU.setOnGetGeoGodeListener(new GeoCoderU.OnGetGeoCodeListener() {
            @Override
            public void onGetLatlng(double lat, double lng) {
            }

            @Override
            public void onGetAddress(String address) {
                mTextViewAddress.setText(address);
            }
        });

        mGaodeMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Log.i(TAG, "onMarkerClick: obj-->" + marker.getObject());
                Bundle bundle = (Bundle) marker.getObject();
                int type = bundle.getInt(Data.KEY_TYPE);
                mChoiceImei = bundle.getString(Data.KEY_IMEI);
                getInfoByImei(mChoiceImei);
                if (!mIsActivation) {
                    myHandler.sendEmptyMessage(Data.MSG_4);
                    return true;
                }
                if (type != Data.MARKER_CLUSTER) {
                    addVirMaker(marker.getPosition());
                }
                return true;
            }
        });

        mGaodeMap.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
            }

            @Override
            public void onCameraChangeFinish(CameraPosition cameraPosition) {
                clustMarkerAndShow();
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
                if (null == infoWindowBean.getObj()) {
                    return;
                }
                mMonitorDataList.clear();
                for (InfoWindowBean.ObjBean objBean : infoWindowBean.getObj()) {
                    MonitorData monitorData = new MonitorData(objBean.getImei()
                            , objBean.getName()
                            , objBean.getModel()
                            , objBean.getIcon()
                            , infoWindowBean.getTime()
                            , objBean.getRedisobj());
                    mMonitorDataList.add(monitorData);
                }

                //  转换坐标系
                transType();

                myHandler.sendEmptyMessage(Data.MSG_1);


            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });

        mNetManager.setOnGetStationInfoListener(new OnGetStationInfoListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                StationBean stationBean = gson.fromJson(result, StationBean.class);
                StationBean.LocationBean lcLocationBean = stationBean.getLocation();
                mLatLngStation = new LatLng(lcLocationBean.getLatitude(), lcLocationBean.getLongitude());

                myHandler.obtainMessage(Data.MSG_6).sendToTarget();
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });
    }

    //  添加marker基础方法
    private void addMarker(LatLng latLng, int type, int direction, String imei, int count) {

        //定义Maker坐标点
        if (null == latLng) {
            return;
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
            case Data.MARKER_CLUSTER: {
                viewMarker = LayoutInflater.from(getContext()).inflate(R.layout.view_map_marker_cluster, null);
                TextView tvCount = viewMarker.findViewById(R.id.tv_view_map_marker_cluster_count);
                tvCount.setText("" + count);
                break;
            }
            default: {
                viewMarker = LayoutInflater.from(getContext()).inflate(R.layout.view_map_marker_car_gray, null);
                Log.i(TAG, "addMarker: locate_type.default-->" + type);
            }
        }
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(viewMarker);
        markerOptions.position(latLng).icon(bitmapDescriptor).anchor(0.5f, 0.5f).rotateAngle(direction);
        //构建MarkerOption，用于在地图上添加Marker
        Bundle bundle = new Bundle();
        bundle.putString(Data.KEY_IMEI, imei);
        bundle.putInt(Data.KEY_TYPE, type);

        Marker mMarkerAdded = mGaodeMap.addMarker(markerOptions);
        mMarkerAdded.setObject(bundle);
        mMarkerList.add(mMarkerAdded);
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
        View viewMarker = LayoutInflater.from(getContext()).inflate(R.layout.view_map_marker_vir, null);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(viewMarker);
        markerOptions.position(latLng).icon(bitmapDescriptor);

        mMarkerVir = mGaodeMap.addMarker(markerOptions);
        mMarkerVir.showInfoWindow();
        Log.i(TAG, "addVirMaker: ");
        //  反编码地址
        getAddress(latLng);
    }

    //  隐藏infoWindow
    private void hideInfoWindow() {
        if (null != mMarkerVir) {
            mMarkerVir.hideInfoWindow();
            mMarkerVir.remove();
        }
        mTextViewAddress.setText(null);
    }

    //  清除Marker
    private void removeAllMarker() {
//        mTextViewAddress.setText(null);
        for (Marker marker : mMarkerList) {
            marker.remove();
        }
    }

    //  地图移动到目标点
    private void moveToCenter(LatLng latLng) {
        if (null == latLng) {
            return;
        }
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, mGaodeMap.getCameraPosition().zoom, 0, 0));
        mGaodeMap.animateCamera(cameraUpdate);
        mIsToCenter = false;
    }

    //  获取某台设备的信息，并显示infowindow，公开给外部使用
    public void showPointNew(String imeiStr) {
//        boolean attention = mSharedManager.getShowAttention();
        mNetManager.showPointNewPost(mToken, mCid, "", imeiStr, false);
    }

    //  获取关注列表的设备信息，并添加Marker
    public void showPointNew() {
        mNetManager.showPointNewPost(mToken, mCid, "", "", true);
    }

    //  刷新页面设备
    private void showPointNewFlush() {
        String imeiStr = "";
        for (String str : mImeiList) {
            imeiStr += (str + ",");
        }
        boolean attention = mSharedManager.getShowAttention();
        mNetManager.showPointNewPost(mToken, mCid, mCidStr, imeiStr, attention);
    }

    //  获取帐户下的设备信息，并添加Marker
    public void showPointNew(String cidStr, boolean attention) {
        mNetManager.showPointNewPost(mToken, mCid, cidStr, "", attention);
    }

    //  跳转到跟踪页面
    private void toTrackActivity(String imei) {
        Intent intent = new Intent(getContext(), TrackGaodeActivity.class);
        intent.putExtra(Data.KEY_IMEI, imei);
        startActivity(intent);
    }

    //  跳转到回放页面
    private void toPathActivity(String imei, String name) {
        Intent intent = new Intent(getContext(), PathGaodeActivity.class);
        intent.putExtra(Data.INTENT_IMEI, imei);
        intent.putExtra(Data.INTENT_NAME, name);
        startActivity(intent);
    }

    //  跳转到导航页面
    private void toNaviActivity() {
        Intent intent = new Intent(getContext(), NavigationActivity.class);
        intent.putExtra(Data.KEY_IMEI, mInfoImei);
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
        mIsOpenInfo = true;
        mIsToCenter = true;
        boolean added = false;
        for (String str : mImeiList) {
            if (str.equals(imei)) {
                added = true;
                break;
            }
        }
        if (added) {
            myHandler.sendEmptyMessage(Data.MSG_2);
            return;
        }
        mImeiList.add(imei);
        String imeiStr = "";
        for (String str : mImeiList) {
            imeiStr += (str + ",");
        }
        mNetManager.showPointNewPost(mToken, mCid, "", imeiStr, false);
    }

    //  由OverviewDialogFragment调用，显示关注车辆
    public void showAttentionDevices() {
        mFrom = FROM_OVERVIEW;
        hideInfoWindow();
        mIsOpenInfo = false;
        if (mSharedManager.getShowAttention()) {
            showPointNew();
        } else {
            removeAllMarker();
            mImeiList.clear();
            mGaodePointList.clear();
            mGaodePointListShow.clear();
        }
    }

    //  由OverviewDialogFragment调用，显示帐户下的车辆
    public void showCompleteDevices(String cidStr) {
        mFrom = FROM_OVERVIEW;
        Log.i(TAG, "showCompleteDevices: cidStr-->" + cidStr);
        this.mCidStr = cidStr;
        showPointNew(cidStr, mSharedManager.getShowAttention());
    }

    //  计算并获取某个设备的详细信息
    private void getInfoByImei(String imei) {
        MonitorData monitorData = null;
        for (MonitorData data : mMonitorDataList) {
            if (imei.equals(data.getImei())) {
                monitorData = data;
                break;
            }
        }
        if (null == monitorData) {
            return;
        }

        InfoWindowBean.ObjBean.RedisobjBean redisobjBean = monitorData.getRedisobjBean();

        mInfoName = monitorData.getName();
        mModel = Integer.valueOf(monitorData.getModel());
        if (mIsStation && null != mLatLngStation) {
            mInfoLatLng = mLatLngStation;
        } else {
            mInfoLatLng = new LatLng(redisobjBean.getLatitudeF(), redisobjBean.getLongitudeF());
        }

        if ("3".equals(redisobjBean.getScene())) {
            mIsActivation = false;
            return;
        } else {
            mIsActivation = true;
        }
        mInfoStationCode = redisobjBean.getStation_code();
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

        calculateIsGetStation(redisobjBean.getLocate_type(), redisobjBean.getScene());
        mInfoCurrentTime = redisobjBean.getCurrent_time();
        mInfoLocateTime = redisobjBean.getLocate_time();
        mInfoDirection = Integer.valueOf(redisobjBean.getDirection());
        mInfoElectricity = redisobjBean.getDianliang() + "%";
        mElectricity = redisobjBean.getDianliang();
        mInfoImei = monitorData.getImei();

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

        mStatusData = StatusU.getStatus(Integer.valueOf(monitorData.getModel())
                , monitorData.getTime()
                , currentTime
                , locateTime
                , parkTime
                , speed);
    }

    //  计算所有的Marker状态
    private void culculateAllMarker() {
        mGaodePointList.clear();
        if (null != mMonitorDataList) {
            for (int i = 0; i < mMonitorDataList.size(); i++) {
                MonitorData monitorData = mMonitorDataList.get(i);

                InfoWindowBean.ObjBean.RedisobjBean redisobjBean = monitorData.getRedisobjBean();
                LatLng latLng = new LatLng(redisobjBean.getLatitudeF(), redisobjBean.getLongitudeF());

                boolean isActivivation = !"3".equals(redisobjBean.getScene());

                if (isActivivation) {
                    int direction = Integer.valueOf(redisobjBean.getDirection());

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

                    Log.i(TAG, "culculateAllMarker: status-->" + StatusU.getStatus(mModel
                            , monitorData.getTime()
                            , currentTime
                            , locateTime
                            , parkTime
                            , speed));
                    mStatusData = StatusU.getStatus(Integer.valueOf(monitorData.getModel())
                            , monitorData.getTime()
                            , currentTime
                            , locateTime
                            , parkTime
                            , speed);
                    mGaodePointList.add(new GaodePoint(latLng
                            , false
                            , mStatusData.getStatu()
                            , direction
                            , monitorData.getImei()));
                } else {
                    mGaodePointList.add(new GaodePoint(latLng, false, Data.STATUS_OTHER, 0, monitorData.getImei()));
                }
            }
        }
    }

    //  聚合Marker，并打在地图上
    private void clustMarkerAndShow() {
        removeAllMarker();
        if (null != mGaodeMap) {
            CameraPosition cameraPosition = mGaodeMap.getCameraPosition();
            int zoom = (int) cameraPosition.zoom;
            Point point1 = new Point(-(mWidth / 5), -(mHeight / 5));
            Point point2 = new Point(mWidth * 6 / 5, mHeight * 6 / 5);
            Projection mProjection = mGaodeMap.getProjection();
            LatLng one = mProjection.fromScreenLocation(point1);
            LatLng two = mProjection.fromScreenLocation(point2);
            mGaodePointListShow = mCookieClusterGaode.getClusterList(zoom, one, two);
            for (GaodePoint gaodePoint : mGaodePointListShow) {
                Log.i(TAG, "handleMessage: addMaker");
                addMarker(gaodePoint.getLatLng(), gaodePoint.getType(), gaodePoint.getDirection(), gaodePoint.getImei(), gaodePoint.getCount());
            }
        }
    }

    //  计算定位类型，以及是否显示获取基站
    private void calculateIsGetStation(String locateType, String scene) {
        mIsShowGetStation = false;
        if (!RegularU.isEmpty(locateType) && "1".equals(locateType)) {
            mInfoLocateType = "GPS";
        } else if (!RegularU.isEmpty(locateType) && "2".equals(locateType)) {
            mInfoLocateType = "GPS定位中";
        } else if (!RegularU.isEmpty(locateType) && "3".equals(locateType)) {
            mInfoLocateType = "WiFi";
        } else if (!RegularU.isEmpty(scene) && "1".equals(scene)) {
            mInfoLocateType = "GPS定位中";
            mIsShowGetStation = true;
        } else {
            mInfoLocateType = "基站定位";
        }
    }

    //  将bd09坐标转为gcj
    private void transType() {
        if (null != mMonitorDataList) {
            for (MonitorData monitorData : mMonitorDataList) {
                InfoWindowBean.ObjBean.RedisobjBean redisobjBean = monitorData.getRedisobjBean();
                double[] latlng = BDTransU.bd2gcj(redisobjBean.getLatitudeF(), redisobjBean.getLongitudeF());
                redisobjBean.setLatitudeF(latlng[0]);
                redisobjBean.setLongitudeF(latlng[1]);
            }
        }
    }

    //  反编码地址
    public void getAddress(LatLng latLng) {
        double[] latlngT = BDTransU.gcj2bd(latLng.latitude, latLng.longitude);
        mGeoCoderU.searchAddress(latlngT[0], latlngT[1]);
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
                    //  addMarker
                    if (null != mMonitorDataList) {
                        culculateAllMarker();
                        clustMarkerAndShow();
                        for (MonitorData monitorData : mMonitorDataList) {
                            mImeiList.add(monitorData.getImei());
                        }
                    }
                    if (mIsOpenInfo) {
                        myHandler.sendEmptyMessage(Data.MSG_2);
                    }
                    break;
                }
                case Data.MSG_2: {
                    //  showInfo
                    hideInfoWindow();
                    getInfoByImei(mChoiceImei);
                    if (!mIsActivation) {
                        myHandler.sendEmptyMessage(Data.MSG_4);
                        break;
                    }
                    addVirMaker(mInfoLatLng);
                    if (mIsToCenter) {
                        moveToCenter(mInfoLatLng);
                    }
                    break;
                }
                case Data.MSG_3: {
                    //  显示InfoWindow
                    if (mIsOpenInfo) {
//                        showInfoWindow(mInfoLatLng);
                    }
                    break;
                }
                case Data.MSG_4: {
                    //  设备未启用（未激活）
                    addMarker(mInfoLatLng, Data.STATUS_OTHER, 0, mChoiceImei, 1);
                    moveToCenter(mInfoLatLng);
                    String str = "";
                    str += mInfoName;
                    if (mModel == 1) {
                        str += "有线：";
                    } else {
                        str += "无线：";
                    }
                    str += "设备未启用";
                    mTextViewAddress.setText(str);
                    break;
                }
                case Data.MSG_5: {
                    //  延时及刷新页面
                    Log.i(TAG, "handleMessage: MSG_5");
                    myHandler.removeMessages(Data.MSG_5);
                    showPointNewFlush();
                    myHandler.sendEmptyMessageDelayed(Data.MSG_5, mFlushTime);
                    break;
                }
                case Data.MSG_6: {
                    //  获取基站数据
//                    for (Overlay overlay : mMarkerList) {
//                        Marker marker = (Marker) overlay;
//                        if (marker.getExtraInfo().getString(Data.KEY_IMEI).equals(mChoiceImei)) {
//                            mIsStation = true;
//                            marker.setPosition(mLatLngStation);
//                        }
//                    }
//                    for (MarkerData markerData : mMarkerDataList) {
//                        Log.i(TAG, "handleMessage: imei-->" + markerData.getImei());
//                        if (markerData.getImei().equals(mChoiceImei)) {
////                            markerData.setLatLng(mLatLngStation);
//                            mIsStation = true;
//                            showInfoWindow(mLatLngStation);
//                        }
//                    }
                    for (GaodePoint gaodePoint : mGaodePointList) {
                        if (gaodePoint.getImei().equals(mChoiceImei)) {
//                            baiduPoint.setLatLng(mLatLngStation);
                            mIsStation = true;
//                            showInfoWindow(mLatLngStation);
                        }
                    }

                    break;
                }
            }
        }
    }
}
