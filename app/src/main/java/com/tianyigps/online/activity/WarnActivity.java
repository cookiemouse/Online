package com.tianyigps.online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
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
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.tianyigps.online.R;
import com.tianyigps.online.base.BaseActivity;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.utils.BDTransU;
import com.tianyigps.online.utils.GeoCoderU;
import com.tianyigps.online.utils.WarnTypeU;

public class WarnActivity extends BaseActivity {

    private static final String TAG = "WarnActivity";

    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private Overlay mOverlayMarker;

    private TextView mTextViewNormal, mTextViewSatellite;

    private String mTitle, mType, mDate, mAddress;
    private double mLatidude, mLongitude;

    private GeoCoderU mGeoCoderU;

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
        mMapView = (MapView) findViewById(R.id.mv_activity_warn);
        mBaiduMap = mMapView.getMap();

        mTextViewNormal = (TextView) findViewById(R.id.tv_activity_warn_normal);
        mTextViewSatellite = (TextView) findViewById(R.id.tv_activity_warn_satellite);


        Intent intent = getIntent();
        mTitle = intent.getStringExtra(Data.INTENT_NAME);
        mType = intent.getStringExtra(Data.INTENT_WARN_TYPE);
        mDate = intent.getStringExtra(Data.INTENT_DATE);
        mLatidude = intent.getDoubleExtra(Data.INTENT_LATITUDE, 0);
        mLongitude = intent.getDoubleExtra(Data.INTENT_LONGITUDE, 0);

        double[] doubles = BDTransU.wgs2bd(mLatidude, mLongitude);
        mLatidude = doubles[0];
        mLongitude = doubles[1];

        this.setTitleText(mTitle);

        mGeoCoderU = new GeoCoderU();

        mGeoCoderU.searchAddress(mLatidude, mLongitude);
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

        mGeoCoderU.setOnGetGeoGodeListener(new GeoCoderU.OnGetGeoCodeListener() {
            @Override
            public void onGetLatlng(double lat, double lng) {
            }

            @Override
            public void onGetAddress(String address) {
                mAddress = "位置：" + address;
                LatLng latLng = new LatLng(mLatidude, mLongitude);
                showInfoWindow(latLng, mTitle, WarnTypeU.getType(mType), mDate, mAddress);
            }
        });

        mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // TODO: 2017/9/11 显示InfoWindow
                LatLng latLng = new LatLng(mLatidude, mLongitude);
                showInfoWindow(latLng, mTitle, WarnTypeU.getType(mType), mDate, mAddress);
                return true;
            }
        });
    }

    //  显示InfoWindow
    private void showInfoWindow(LatLng latLng, String title, String type, String date, String address) {
        View viewInfo = LayoutInflater.from(WarnActivity.this).inflate(R.layout.view_map_info_window_warn, null);

        ImageView imageViewClose = viewInfo.findViewById(R.id.iv_view_info_window_close);
        TextView textViewTitle = viewInfo.findViewById(R.id.tv_view_info_window_title);
        TextView textViewType = viewInfo.findViewById(R.id.tv_view_info_window_warn_type);
        TextView textViewDate = viewInfo.findViewById(R.id.tv_view_info_window_warn_date);
        TextView textViewAddress = viewInfo.findViewById(R.id.tv_view_info_window_warn_address);

        textViewTitle.setText(title);
        textViewType.setText(type);
        textViewDate.setText(date);
        textViewAddress.setText(address);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBaiduMap.hideInfoWindow();
            }
        });

        InfoWindow mInfoWindow = new InfoWindow(viewInfo, latLng, 0);
        //显示InfoWindow
        mBaiduMap.showInfoWindow(mInfoWindow);

        addMarker(latLng);
        moveToCenter(latLng);
    }

    //  地图移动到目标点
    private void moveToCenter(LatLng latLng) {
        MapStatus.Builder builder = new MapStatus.Builder();
        builder.target(latLng);
        MapStatus status = builder.build();
        MapStatusUpdate update = MapStatusUpdateFactory.newMapStatus(status);
        mBaiduMap.animateMapStatus(update);
    }

    //  添加marker
    private void addMarker(LatLng latLng) {
        //定义Maker坐标点
        if (null == latLng) {
            return;
        }
        if (null != mOverlayMarker) {
            mOverlayMarker.remove();
        }
        //构建Marker图标
        View viewMarker = LayoutInflater.from(WarnActivity.this).inflate(R.layout.view_map_marker_car_red, null);
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromView(viewMarker);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions().position(latLng).icon(bitmap).anchor(0.5f, 0.5f);
        //在地图上添加Marker，并显示
        mOverlayMarker = mBaiduMap.addOverlay(option);
    }
}
