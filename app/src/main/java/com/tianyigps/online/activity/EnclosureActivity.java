package com.tianyigps.online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.model.LatLng;
import com.google.gson.Gson;
import com.tianyigps.online.R;
import com.tianyigps.online.bean.EnclosureBean;
import com.tianyigps.online.bean.TerminalInfo4MapBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.interfaces.OnShowTerminalInfoForMapListener;
import com.tianyigps.online.interfaces.OnUnifenceInfoListener;
import com.tianyigps.online.interfaces.OnUnifenceUpsertListener;
import com.tianyigps.online.manager.LocateManager;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;

import java.util.ArrayList;
import java.util.List;

import static com.baidu.mapapi.BMapManager.getContext;

public class EnclosureActivity extends AppCompatActivity {

    private static final String TAG = "EnclosureActivity";

    private TextView mTextViewTitle;
    private ImageView mImageViewBack, mImageViewCycle, mImageViewPolygon;

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Overlay mOverlayMarker, mOverlayPolygon;

    private TextView mTextViewSatellite, mTextViewNormal;
    private ImageView mImageViewLocate;
    private LinearLayout mLinearLayoutCycle, mLinearLayoutPolygon;
    private ImageView mImageViewCycleReduce, mImageViewCyclePlus;
    private SeekBar mSeekBar;
    private Button mButtonCycleSet, mButtonPolygonReset, mButtonPolygonSet;
    private int mRadius = 0;

    private LocateManager mLocateManager;
    private boolean mIsToCenter = false;
    //  左下角定位，false = 定位手机，true = 定位车辆
    private boolean mIsLocateCar = false;
    private LatLng mLatLngSelf, mLatLngCar;

    private NetManager mNetManager;
    private SharedManager mSharedManager;
    private String mToken, mImei;
    private String mStringMessage;
    private int mIcon;
    private int mEnclosureType;
    private List<LatLng> mLatLngPolygon;

    private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //透明状态栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_enclosure);

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
        mLocateManager.stopLocate();
        super.onDestroy();
    }

    private void init() {
        mTextViewTitle = (TextView) findViewById(R.id.tv_activity_enclosure_title);
        mImageViewBack = (ImageView) findViewById(R.id.iv_activity_enclosure_1);
        mImageViewCycle = (ImageView) findViewById(R.id.iv_activity_enclosure_2);
        mImageViewPolygon = (ImageView) findViewById(R.id.iv_activity_enclosure_3);

        mTextViewTitle.setText("电子围栏");

        mMapView = (MapView) findViewById(R.id.mv_activity_enclosure);
        mBaiduMap = mMapView.getMap();
        //  开启地位图层
        mBaiduMap.setMyLocationEnabled(true);

        mLatLngPolygon = new ArrayList<>();

        mTextViewSatellite = (TextView) findViewById(R.id.tv_activity_enclosure_satellite);
        mTextViewNormal = (TextView) findViewById(R.id.tv_activity_enclosure_normal);
        mImageViewLocate = (ImageView) findViewById(R.id.iv_activity_enclosure_locate);
        mLinearLayoutCycle = (LinearLayout) findViewById(R.id.ll_layout_enclosure_bottom_cycle);
        mLinearLayoutPolygon = (LinearLayout) findViewById(R.id.ll_layout_enclosure_bottom_polygon);

        mImageViewCycleReduce = (ImageView) findViewById(R.id.iv_layout_enclosure_bottom_cycle_reduce);
        mImageViewCyclePlus = (ImageView) findViewById(R.id.iv_layout_enclosure_bottom_cycle_plus);
        mSeekBar = (SeekBar) findViewById(R.id.sb_layout_enclosure_bottom_cycle);
        mButtonCycleSet = (Button) findViewById(R.id.btn_layout_enclosure_bottom_cycle);
        mButtonPolygonSet = (Button) findViewById(R.id.btn_layout_enclosure_bottom_polygon_set);
        mButtonPolygonReset = (Button) findViewById(R.id.btn_layout_enclosure_bottom_polygon_reset);

        myHandler = new MyHandler();

        mNetManager = new NetManager();
        mSharedManager = new SharedManager(this);
        mToken = mSharedManager.getToken();
        Intent intent = getIntent();
        mImei = intent.getStringExtra(Data.INTENT_IMEI);

        mLocateManager = new LocateManager(this);
        mIsToCenter = false;
        mLocateManager.startLocate();

        getTerminalInfo();

        getEnclosure();
    }

    private void setEventListener() {
        mImageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        mImageViewCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageViewCycle.setSelected(true);
                mLinearLayoutCycle.setVisibility(View.VISIBLE);
                mImageViewPolygon.setSelected(false);
                mLinearLayoutPolygon.setVisibility(View.GONE);
            }
        });

        mImageViewPolygon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mImageViewCycle.setSelected(false);
                mLinearLayoutCycle.setVisibility(View.GONE);
                mImageViewPolygon.setSelected(true);
                mLinearLayoutPolygon.setVisibility(View.VISIBLE);
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

        mImageViewLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsLocateCar) {
                    // TODO: 2017/9/5 定准车辆
                    if (null != mLatLngCar) {
                        moveToCenter(mLatLngCar);
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

        mImageViewCycleReduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRadius > 0) {
                    mRadius--;
                }
                mSeekBar.setProgress(mRadius);
            }
        });

        mImageViewCyclePlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRadius < mSeekBar.getMax()){
                    mRadius++;
                }
                mSeekBar.setProgress(mRadius);
            }
        });

        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mRadius = i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        mButtonCycleSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/9/26 设置圆形围栏
            }
        });

        mButtonPolygonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/9/26 重置多边形围栏
            }
        });

        mButtonPolygonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/9/26 设置多边形围栏
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
                mLatLngCar = new LatLng(redisobjBean.getLatitudeF(), redisobjBean.getLongitudeF());
                mIcon = objBean.getIcon();

                myHandler.sendEmptyMessage(Data.MSG_1);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });

        mNetManager.setOnUnifenceInfoListener(new OnUnifenceInfoListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                EnclosureBean enclosureBean = gson.fromJson(result, EnclosureBean.class);
                if (!enclosureBean.isSuccess()) {
                    mStringMessage = Data.DEFAULT_MESSAGE;
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                EnclosureBean.ObjBean objBean = enclosureBean.getObj();
                mEnclosureType = objBean.getType();
                if (2 == mEnclosureType) {
                    //  多边形
                    mLatLngPolygon.clear();
                    for (List<Double> doubleList : objBean.getPoints()) {
                        mLatLngPolygon.add(new LatLng(doubleList.get(1), doubleList.get(0)));
                    }
                }
                myHandler.sendEmptyMessage(Data.MSG_2);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });

        mNetManager.setOnUnifenceUpsertListener(new OnUnifenceUpsertListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });
    }

    //  获取设备信息
    private void getTerminalInfo() {
        mNetManager.showTerminalInfo4Map(mToken, mImei);
    }

    //  获取围栏信息
    private void getEnclosure() {
        mNetManager.unifenceInfo(mImei);
    }

    //  设置围栏
    private void setEnclosure() {
        mNetManager.unifenceUpsert(mImei, 1, "[118.77854995271579,31.95968201507062]", 1000, "");
    }

    //  计算圆上的点

    //  地图移动到目标点
    private void moveToCenter(LatLng latLng) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng);
        MapStatus status = builder.build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        mBaiduMap.animateMapStatus(update);
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
        OverlayOptions option = new MarkerOptions()
                .position(latLng)
                .icon(bitmap)
                .rotate(direction)
                .anchor(0.5f, 0.5f);
        //在地图上添加Marker，并显示
        mOverlayMarker = mBaiduMap.addOverlay(option);
    }

    //  显示多边形围栏
    private void showPolygon() {
        if (null != mOverlayPolygon) {
            mOverlayPolygon.remove();
        }
        Log.i(TAG, "showPolygon: ");
        //  计算中心点
        double lat = 0, lng = 0;
        for (LatLng latLng : mLatLngPolygon) {
            lat += latLng.latitude;
            lng += latLng.longitude;
        }
        int size = mLatLngPolygon.size();
        LatLng latLng = new LatLng(lat / size, lng / size);
        moveToCenter(latLng);
        OverlayOptions polygonOption = new PolygonOptions().points(mLatLngPolygon).fillColor(0xa03cabfa);
        mOverlayPolygon = mBaiduMap.addOverlay(polygonOption);
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
                    break;
                }
                case Data.MSG_NOTHING: {
                    break;
                }
                case Data.MSG_1: {
                    //  获取设备信息
                    addMarker(mLatLngCar, mIcon, 0);
                    break;
                }
                case Data.MSG_2: {
                    //  获取围栏信息
                    if (2 == mEnclosureType) {
                        showPolygon();
                        mLinearLayoutPolygon.setVisibility(View.VISIBLE);
                        mLinearLayoutCycle.setVisibility(View.GONE);
                    } else {
                        mLinearLayoutPolygon.setVisibility(View.GONE);
                        mLinearLayoutCycle.setVisibility(View.VISIBLE);
                    }
                    break;
                }
                case Data.MSG_3: {
                    //  设置围栏
                    break;
                }
            }
        }
    }
}
