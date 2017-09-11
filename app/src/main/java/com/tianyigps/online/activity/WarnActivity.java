package com.tianyigps.online.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.tianyigps.online.R;
import com.tianyigps.online.base.BaseActivity;

public class WarnActivity extends BaseActivity {

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    private TextView mTextViewNormal, mTextViewSatellite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn);

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
        this.setTitleText("沪KZ7555荣威123124124");

        mMapView = (MapView) findViewById(R.id.mv_activity_warn);
        mBaiduMap = mMapView.getMap();

        mTextViewNormal = (TextView) findViewById(R.id.tv_activity_warn_normal);
        mTextViewSatellite = (TextView) findViewById(R.id.tv_activity_warn_satellite);
    }

    private void setEventListener() {

        mTextViewNormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                mTextViewNormal.setBackgroundResource(R.drawable.bg_map_control_select);
                mTextViewSatellite.setBackgroundResource(R.drawable.bg_map_control);
            }
        });

        mTextViewSatellite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                mTextViewNormal.setBackgroundResource(R.drawable.bg_map_control);
                mTextViewSatellite.setBackgroundResource(R.drawable.bg_map_control_select);
            }
        });
    }

    //  显示InfoWindow
    private void showInfoWindow() {
    }
}
