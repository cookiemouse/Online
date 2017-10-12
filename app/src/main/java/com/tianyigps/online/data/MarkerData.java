package com.tianyigps.online.data;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by cookiemouse on 2017/10/12.
 */

public class MarkerData {
    private LatLng latLng;
    private int type;
    private int direction;
    private String imei;

    public MarkerData(LatLng latLng, int type, int direction, String imei) {
        this.latLng = latLng;
        this.type = type;
        this.direction = direction;
        this.imei = imei;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDirection() {
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
