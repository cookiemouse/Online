package com.tianyigps.online.utils;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;

/**
 * Created by djc on 2017/7/20.
 */

public class GeoCoderU {

    private static final String TAG = "GeoCoderU";

    private GeoCoder mGeoCoder;

    private OnGetGeoCodeListener mOnGetGeoCodeListener;

    public GeoCoderU() {
        mGeoCoder = GeoCoder.newInstance();

        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {
                if (null == geoCodeResult || geoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //无结果
                } else {
                    //获取地理编码结果
                    LatLng latLng = geoCodeResult.getLocation();
                    if (null == mOnGetGeoCodeListener) {
                        throw new NullPointerException("OnGetGeoCodeListener is null");
                    }
                    mOnGetGeoCodeListener.onGetLatlng(latLng.latitude, latLng.longitude);
                }
            }

            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (null == reverseGeoCodeResult || reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    //无结果
                } else {
                    //获取反向地理编码结果
                    String address = reverseGeoCodeResult.getAddress() + reverseGeoCodeResult.getSematicDescription();
                    if (null == mOnGetGeoCodeListener) {
                        throw new NullPointerException("OnGetGeoCodeListener is null");
                    }
                    mOnGetGeoCodeListener.onGetAddress(address);
                }
            }
        });
    }

    public void searchAddress(double lat, double lng) {
        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(lat, lng)));
    }

    public void searchLatlng(String address) {
        mGeoCoder.geocode(new GeoCodeOption().address(address).city(""));
    }

    public interface OnGetGeoCodeListener {

        void onGetLatlng(double lat, double lng);

        void onGetAddress(String address);
    }

    public void setOnGetGeoGodeListener(OnGetGeoCodeListener listener) {
        this.mOnGetGeoCodeListener = listener;
    }
}
