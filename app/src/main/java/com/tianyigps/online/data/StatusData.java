package com.tianyigps.online.data;

/**
 * Created by cookiemouse on 2017/9/22.
 */

public class StatusData {
    private String status;
    private int statu;

    public StatusData(String status, int statu) {
        this.status = status;
        this.statu = statu;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatu() {
        return statu;
    }

    public void setStatu(int statu) {
        this.statu = statu;
    }
}
