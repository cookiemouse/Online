package com.tianyigps.online.cluster;

import android.util.Log;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookiemouse on 2017/10/13.
 */

public class CookieCluster {

    private static final String TAG = "CookieCluster";

    private static int DIC_DISTANCE_25 = 0;
    private static int DIC_DISTANCE_50 = 1;
    private static int DIC_DISTANCE_100 = 2;
    private static int DIC_DISTANCE_200 = 3;
    private static int DIC_DISTANCE_500 = 4;
    private static int DIC_DISTANCE_1000 = 5;
    private static int DIC_DISTANCE_2000 = 6;
    private static int DIC_DISTANCE_5000 = 7;
    private static int DIC_DISTANCE_10000 = 8;
    private static int DIC_DISTANCE_20000 = 9;
    private static int DIC_DISTANCE_25000 = 10;

    private static double COORDINATE_RANGE_0 = 0;
    private static double COORDINATE_RANGE_5 = 0.000032;
    private static double COORDINATE_RANGE_10 = 0.000064;
    private static double COORDINATE_RANGE_20 = 0.000127;
    private static double COORDINATE_RANGE_25 = 0.00016;
    private static double COORDINATE_RANGE_50 = 0.00032;
    private static double COORDINATE_RANGE_100 = 0.00063;
    private static double COORDINATE_RANGE_200 = 0.00127;
    private static double COORDINATE_RANGE_500 = 0.00318;
    private static double COORDINATE_RANGE_1000 = 0.00635;
    private static double COORDINATE_RANGE_2000 = 0.01270;
    private static double COORDINATE_RANGE_5000 = 0.03176;
    private static double COORDINATE_RANGE_10000 = 0.06352;
    private static double COORDINATE_RANGE_20000 = 0.12704;
    private static double COORDINATE_RANGE_25000 = 0.15880;
    private static double COORDINATE_RANGE_50000 = 0.32000;
    private static double COORDINATE_RANGE_100000 = 0.63000;
    private static double COORDINATE_RANGE_200000 = 1.27000;
    private static double COORDINATE_RANGE_500000 = 3.17600;
    private static double COORDINATE_RANGE_1000000 = 6.35000;
    private static double COORDINATE_RANGE_2000000 = 12.7040;

    private LatLng latLngOne, latLngTwo;
    private List<BaiduPoint> mBaiduPointListIn, mBaiduPointListOut, mBaiduPointMiddle;

    public CookieCluster(List<BaiduPoint> mLatlngPointBaiduListIn) {
        this.mBaiduPointListIn = mLatlngPointBaiduListIn;
        mBaiduPointListOut = new ArrayList<>();
        mBaiduPointMiddle = new ArrayList<>();
    }

    private void calculateCluster(double distance) {
        Log.i(TAG, "calculateCluster: distance-->" + distance);
        Log.i(TAG, "calculateCluster: size-->" + mBaiduPointListIn.size());
        mBaiduPointListOut.clear();
        mBaiduPointMiddle.clear();

        for (BaiduPoint baiduPoint : mBaiduPointListIn) {
            LatLng latLng = baiduPoint.getLatLng();
            if (latLng.latitude < latLngOne.latitude
                    && latLng.longitude > latLngOne.longitude
                    && latLng.latitude > latLngTwo.latitude
                    && latLng.longitude < latLngTwo.longitude) {
                mBaiduPointMiddle.add(baiduPoint);
            }
        }

        for (BaiduPoint baiduPointFirst : mBaiduPointMiddle) {
            int countNumber = 1;
            if (!baiduPointFirst.isClustered()) {
                for (BaiduPoint latlngPointBaiduSecond : mBaiduPointMiddle) {
                    if (baiduPointFirst != latlngPointBaiduSecond && !latlngPointBaiduSecond.isClustered()) {
                        LatLng latLngFirst = baiduPointFirst.getLatLng();
                        LatLng latLngSecond = latlngPointBaiduSecond.getLatLng();
                        if (Math.abs(latLngFirst.latitude - latLngSecond.latitude) < distance
                                && Math.abs(latLngFirst.longitude - latLngSecond.longitude) < distance) {
                            latlngPointBaiduSecond.setClustered(true);
                            countNumber++;
                        }
                    }
                }
                baiduPointFirst.setCount(countNumber);
                mBaiduPointListOut.add(baiduPointFirst);
            }
        }
        for (BaiduPoint baiduPoint : mBaiduPointListIn) {
            baiduPoint.setClustered(false);
        }
    }

    public List<BaiduPoint> getClusterList(int zoom, LatLng leftUp, LatLng rightDown) {
        this.latLngOne = leftUp;
        this.latLngTwo = rightDown;

        switch (zoom) {
            case 3: {
                calculateCluster(COORDINATE_RANGE_2000000);
                break;
            }
            case 4: {
                calculateCluster(COORDINATE_RANGE_1000000);
                break;
            }
            case 5: {
                calculateCluster(COORDINATE_RANGE_500000);
                break;
            }
            case 6: {
                calculateCluster(COORDINATE_RANGE_200000);
                break;
            }
            case 7: {
                calculateCluster(COORDINATE_RANGE_100000);
                break;
            }
            case 8: {
                calculateCluster(COORDINATE_RANGE_50000);
                break;
            }
            case 9: {
                latLngOne = new LatLng(latLngOne.latitude + COORDINATE_RANGE_25000, latLngOne.longitude - COORDINATE_RANGE_25000);
                latLngTwo = new LatLng(latLngTwo.latitude - COORDINATE_RANGE_25000, latLngTwo.longitude + COORDINATE_RANGE_25000);
                calculateCluster(COORDINATE_RANGE_25000);
                break;
            }
            case 10: {
                calculateCluster(COORDINATE_RANGE_20000);
                break;
            }
            case 11: {
                calculateCluster(COORDINATE_RANGE_10000);
                break;
            }
            case 12: {
                calculateCluster(COORDINATE_RANGE_5000);
                break;
            }
            case 13: {
                calculateCluster(COORDINATE_RANGE_2000);
                break;
            }
            case 14: {
                calculateCluster(COORDINATE_RANGE_1000);
                break;
            }
            case 15: {
                calculateCluster(COORDINATE_RANGE_500);
                break;
            }
            case 16: {
                calculateCluster(COORDINATE_RANGE_200);
                break;
            }
            case 17: {
                calculateCluster(COORDINATE_RANGE_100);
                break;
            }
            case 18: {
                calculateCluster(COORDINATE_RANGE_50);
                break;
            }
            case 19: {
                calculateCluster(COORDINATE_RANGE_20);
                break;
            }
            case 20: {
                calculateCluster(COORDINATE_RANGE_5);
                break;
            }
            case 21: {
                calculateCluster(COORDINATE_RANGE_0);
                break;
            }
            default: {
                Log.e("Tag", "CookieClusterManager.default");
                calculateCluster(COORDINATE_RANGE_0);
                break;
            }
        }
        return mBaiduPointListOut;
    }
}
