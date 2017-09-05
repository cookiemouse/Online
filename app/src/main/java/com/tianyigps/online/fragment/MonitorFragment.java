package com.tianyigps.online.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.tianyigps.online.R;
import com.tianyigps.online.activity.FragmentContentActivity;
import com.tianyigps.online.manager.LocateManager;

/**
 * Created by cookiemouse on 2017/9/5.
 */

public class MonitorFragment extends Fragment {

    private static final String TAG = "MonitorFragment";

    private ImageView mImageViewTitle1, mImageViewTitle2, mImageViewTitle3;
    private ImageView mImageViewLocate, mImageViewLeft, mImageViewRight;
    private TextView mTextViewNormal, mTextViewSatellite;

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private LocateManager mLocateManager;

    //  左下角定位，false = 定位手机，true = 定位车辆
    private boolean mIsLocateCar = false;

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
    }

    @Override
    public void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mMapView.onDestroy();
        mLocateManager.stopLocate();
        super.onDestroy();
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

        mLocateManager = new LocateManager(getContext());
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
            }
        });
        mImageViewTitle3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        mImageViewLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mIsLocateCar) {
                    // TODO: 2017/9/5 定准车辆
                    return;
                }
                mLocateManager.startLocate();
            }
        });
        mImageViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
        mImageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    }

    //  地图移动到目标点
    private void moveToCenter(LatLng latLng) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng);
        MapStatus status = builder.build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        mBaiduMap.animateMapStatus(update);
    }
}
