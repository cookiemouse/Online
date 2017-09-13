package com.tianyigps.online.data;

/**
 * Created by cookiemouse on 2017/9/13.
 */

public class AdapterConcernData {
    private String name, imei;
    private boolean open;

    public AdapterConcernData(String name, String imei) {
        this.name = name;
        this.imei = imei;
        this.open = true;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
