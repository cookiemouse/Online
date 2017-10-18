package com.tianyigps.online.utils;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookiemouse on 2017/10/17.
 */

public class BaiduCircleU {

    //999.8685507876875
    //999.6427770124872

    private static final String TAG = "BaiduCircleU";

    private double mDistanceX = 1, mDistanceY = 1;
    private double mDegree = 10;
    private double mAtomDegreeX = 1, mAtomDegreeY = 1;

    public BaiduCircleU() {
        LatLng latLngS = new LatLng(0, 0);
        LatLng latLngX = new LatLng(10, 0);
        LatLng latLngY = new LatLng(0, 10);
        mDistanceX = DistanceUtil.getDistance(latLngS, latLngX);
        mDistanceY = DistanceUtil.getDistance(latLngS, latLngY);
        mAtomDegreeX = mDegree / mDistanceX;
        mAtomDegreeY = mDegree / mDistanceY;
    }

    public LatLng getDistanceLatlng(LatLng latLngCenter, int degree, int distance) {
        double lat = latLngCenter.latitude;
        double lng = latLngCenter.longitude;
        double distance_x = distance * Math.cos(degree * Math.PI / 180);
        double distance_y = distance * Math.sin(degree * Math.PI / 180) * 1.18;
        double lat_x = lat + mAtomDegreeX * distance_x;
        double lat_y = lng + mAtomDegreeY * distance_y;
        double distanceTest = DistanceUtil.getDistance(latLngCenter, new LatLng(lat_x, lat_y));
        Log.i(TAG, "BaiduCircleU: distenceTest-->" + distanceTest);
        return new LatLng(lat_x, lat_y);
    }

    public List<LatLng> getCircleLatlng(LatLng latLngCenter, int radius) {
        List<LatLng> latLngList = new ArrayList<>();
        for (int i = 0; i <= 360; i += 15) {
            latLngList.add(getDistanceLatlng(latLngCenter, i, radius));
        }
        return latLngList;
    }
}
