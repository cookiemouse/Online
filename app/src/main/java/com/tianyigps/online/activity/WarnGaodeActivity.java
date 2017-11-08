package com.tianyigps.online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.baidu.mapapi.map.BaiduMap;
import com.tianyigps.online.R;
import com.tianyigps.online.base.BaseActivity;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.utils.BDTransU;
import com.tianyigps.online.utils.GeoCoderU;
import com.tianyigps.online.utils.WarnTypeU;

public class WarnGaodeActivity extends BaseActivity implements AMap.InfoWindowAdapter {

    private static final String TAG = "WarnGaode";

    private MapView mMapView;
    private AMap mGaodeMap;

    private Marker mMarkerAdded;
    private Marker mMarkerVir;

    private TextView mTextViewNormal, mTextViewSatellite;

    private String mTitle, mType, mDate, mAddress;
    private double mLatidude, mLongitude;
    private double mLatidudeG, mLongitudeG;
    private double mLatidudeB, mLongitudeB;

    private GeoCoderU mGeoCoderU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_warn_gaode);

        init();

        setEventListener();

        mMapView.onCreate(savedInstanceState);
    }

    private void init() {
        mMapView = (MapView) findViewById(R.id.mv_activity_warn_gaode);
        mGaodeMap = mMapView.getMap();

        mTextViewNormal = (TextView) findViewById(R.id.tv_activity_warn_normal);
        mTextViewSatellite = (TextView) findViewById(R.id.tv_activity_warn_satellite);

        Intent intent = getIntent();
        mTitle = intent.getStringExtra(Data.INTENT_NAME);
        mType = intent.getStringExtra(Data.INTENT_WARN_TYPE);
        mDate = intent.getStringExtra(Data.INTENT_DATE);
        mLatidude = intent.getDoubleExtra(Data.INTENT_LATITUDE, 0);
        mLongitude = intent.getDoubleExtra(Data.INTENT_LONGITUDE, 0);

        this.setTitleText(mTitle);

        double[] latlngG = BDTransU.wgs2gcj(mLatidude, mLongitude);
        double[] latlngB = BDTransU.wgs2bd(mLatidude, mLongitude);
        mLatidudeG = latlngG[0];
        mLongitudeG = latlngG[1];
        mLatidudeB = latlngB[0];
        mLongitudeB = latlngB[1];

        mGeoCoderU = new GeoCoderU();

        mGeoCoderU.searchAddress(mLatidudeB, mLongitudeB);
    }

    private void setEventListener() {

        mGaodeMap.setInfoWindowAdapter(this);

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

        mGeoCoderU.setOnGetGeoGodeListener(new GeoCoderU.OnGetGeoCodeListener() {
            @Override
            public void onGetLatlng(double lat, double lng) {
            }

            @Override
            public void onGetAddress(String address) {
                mAddress = "位置：" + address;
                LatLng latLng = new LatLng(mLatidudeG, mLongitudeG);
                showInfoWindow(latLng);
            }
        });

        mGaodeMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                addVirMaker(marker.getPosition());
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
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

    @Override
    public View getInfoWindow(Marker marker) {
        Log.i(TAG, "getInfoWindow: ");
        View viewInfo = LayoutInflater.from(WarnGaodeActivity.this).inflate(R.layout.view_map_info_window_warn, null);

        ImageView imageViewClose = viewInfo.findViewById(R.id.iv_view_info_window_close);
        TextView textViewTitle = viewInfo.findViewById(R.id.tv_view_info_window_title);
        TextView textViewType = viewInfo.findViewById(R.id.tv_view_info_window_warn_type);
        TextView textViewDate = viewInfo.findViewById(R.id.tv_view_info_window_warn_date);
        TextView textViewAddress = viewInfo.findViewById(R.id.tv_view_info_window_warn_address);

        textViewTitle.setText(mTitle);
        textViewType.setText(WarnTypeU.getType(mType));
        textViewDate.setText(mDate);
        textViewAddress.setText(mAddress);

        imageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != mMarkerVir) {
                    mMarkerVir.hideInfoWindow();
                    mMarkerVir.remove();
                }
            }
        });
        Log.i(TAG, "getInfoWindow: ");
        return viewInfo;
    }

    @Override
    public View getInfoContents(Marker marker) {
        Log.i(TAG, "getInfoContents: ");
        return null;
    }

    //  显示InfoWindow
    private void showInfoWindow(LatLng latLng) {
        addMarker(latLng);
        addVirMaker(latLng);
        Log.i(TAG, "showInfoWindow: mAddress-->" + mAddress);
    }

    //  添加Marker
    private void addMarker(LatLng latLng) {
        if (null == latLng) {
            return;
        }
        if (null != mMarkerAdded) {
            mMarkerAdded.remove();
        }
        MarkerOptions markerOptions = new MarkerOptions();
        View viewMarker = LayoutInflater.from(WarnGaodeActivity.this).inflate(R.layout.view_map_marker_pause, null);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(viewMarker);
        markerOptions.position(latLng).icon(bitmapDescriptor).anchor(0.5f, 0.5f);

        mMarkerAdded = mGaodeMap.addMarker(markerOptions);
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
        View viewMarker = LayoutInflater.from(WarnGaodeActivity.this).inflate(R.layout.view_map_marker_vir, null);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(viewMarker);
        markerOptions.position(latLng).icon(bitmapDescriptor);

        mMarkerVir = mGaodeMap.addMarker(markerOptions);
        moveToCenter(latLng);
        mMarkerVir.showInfoWindow();
        Log.i(TAG, "addVirMaker: ");
    }

    //  地图移动到目标点
    private void moveToCenter(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(new CameraPosition(latLng, 18, 0, 0));
        mGaodeMap.animateCamera(cameraUpdate);
    }
}
