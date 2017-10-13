package com.tianyigps.online.cluster;

import com.baidu.mapapi.model.LatLng;
import com.tianyigps.online.data.Data;

/**
 * Created by cookiemouse on 2017/10/13.
 */

public class BaiduPoint {
    private LatLng mLatLng;
    private boolean isClustered;
    private int count;
    private int type;
    private int direction;
    private String imei;

    public BaiduPoint(LatLng latLng) {
        this.mLatLng = latLng;
        this.isClustered = false;
        this.count = 0;
    }

    public BaiduPoint(LatLng mLatLng, boolean isClustered, String imei, int type, int direction) {
        this.mLatLng = mLatLng;
        this.isClustered = isClustered;
        this.imei = imei;
        this.type = type;
        this.direction = direction;
    }

    public LatLng getLatLng() {
        return mLatLng;
    }

    public void setLatLng(LatLng mLatLng) {
        this.mLatLng = mLatLng;
    }

    public boolean isClustered() {
        return isClustered;
    }

    public void setClustered(boolean clustered) {
        isClustered = clustered;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getType() {
        if (count > 1) {
            return Data.MARKER_CLUSTER;
        }
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDirection() {
        if (count > 1) {
            return 0;
        }
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
