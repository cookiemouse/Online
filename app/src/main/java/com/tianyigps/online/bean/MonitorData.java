package com.tianyigps.online.bean;

/**
 * Created by cookiemouse on 2017/11/17.
 */

public class MonitorData {
    private String imei;
    private String name;
    private String model;
    private int icon;
    private long time;

    private InfoWindowBean.ObjBean.RedisobjBean redisobjBean;

    public MonitorData(String imei, String name, String model, int icon, long time, InfoWindowBean.ObjBean.RedisobjBean redisobjBean) {
        this.imei = imei;
        this.name = name;
        this.model = model;
        this.icon = icon;
        this.time = time;
        this.redisobjBean = redisobjBean;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public InfoWindowBean.ObjBean.RedisobjBean getRedisobjBean() {
        return redisobjBean;
    }

    public void setRedisobjBean(InfoWindowBean.ObjBean.RedisobjBean redisobjBean) {
        this.redisobjBean = redisobjBean;
    }
}
