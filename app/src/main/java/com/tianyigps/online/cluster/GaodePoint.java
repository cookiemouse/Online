package com.tianyigps.online.cluster;

import com.amap.api.maps.model.LatLng;
import com.tianyigps.online.data.Data;

/**
 * Created by cookiemouse on 2017/11/20.
 */

public class GaodePoint {
    private LatLng mLatLng;
    private boolean isClustered;
    private int count;
    private int type;
    private int direction;
    private String imei;

    public GaodePoint(LatLng latLng) {
        this.mLatLng = latLng;
    }

    public GaodePoint(LatLng mLatLng, boolean isClustered, int type, int direction, String imei) {
        this.mLatLng = mLatLng;
        this.isClustered = isClustered;
        this.type = type;
        this.direction = direction;
        this.imei = imei;
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

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }
}
