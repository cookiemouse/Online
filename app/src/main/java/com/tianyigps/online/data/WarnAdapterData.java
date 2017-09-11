package com.tianyigps.online.data;

/**
 * Created by cookiemouse on 2017/9/6.
 */

public class WarnAdapterData {
    private String name, type, date;
    private String imei;
    private double latitude, longitude;

    public WarnAdapterData(String name, String type, String date, String imei, double latitude, double longitude) {
        this.name = name;
        this.type = type;
        this.date = date;
        this.imei = imei;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
