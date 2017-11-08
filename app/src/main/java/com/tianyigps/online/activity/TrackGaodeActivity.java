package com.tianyigps.online.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.baidu.mapapi.map.BaiduMap;
import com.tianyigps.online.R;
import com.tianyigps.online.base.BaseActivity;

public class TrackGaodeActivity extends BaseActivity {

    private MapView mMapView;
    private AMap mGaodeMap;

    private ImageView mImageViewLocate;
    private TextView mTextViewNormal, mTextViewSatellite, mTextViewAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_gaode);

        init();

        setEventListener();

        mMapView.onSaveInstanceState(savedInstanceState);
    }

    private void init() {
        mMapView = (MapView) findViewById(R.id.mv_activity_track_gaode);
        mGaodeMap = mMapView.getMap();

        mImageViewLocate = (ImageView) findViewById(R.id.iv_activity_track_locate);
        mTextViewNormal = (TextView) findViewById(R.id.tv_activity_track_normal);
        mTextViewSatellite = (TextView) findViewById(R.id.tv_activity_track_satellite);
        mTextViewAddress = (TextView) findViewById(R.id.tv_activity_track_address);
    }

    private void setEventListener() {
//        mImageViewLocate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (mIsLocateCar) {
//                    // TODO: 2017/9/5 定准车辆
//                    if (null != mInfoLatLng) {
//                        moveToInfoCenter(mInfoLatLng);
//                        mImageViewLocate.setImageResource(R.drawable.ic_location_self);
//                    }
//                    mIsLocateCar = false;
//                    return;
//                }
//                if (null != mLatLngSelf) {
//                    moveToCenter(mLatLngSelf);
//                    mIsToCenter = false;
//                    mLocateManager.startLocate();
//                } else {
//                    mIsToCenter = true;
//                    mLocateManager.startLocate();
//                }
//                mIsLocateCar = true;
//                mImageViewLocate.setImageResource(R.drawable.ic_location_car);
//            }
//        });

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
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}
