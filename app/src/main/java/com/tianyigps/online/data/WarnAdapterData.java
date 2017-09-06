package com.tianyigps.online.data;

/**
 * Created by cookiemouse on 2017/9/6.
 */

public class WarnAdapterData {
    private String name, type, date;

    public WarnAdapterData(String name, String type, String date) {
        this.name = name;
        this.type = type;
        this.date = date;
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
}
