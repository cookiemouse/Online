package com.tianyigps.online.data;

/**
 * Created by cookiemouse on 2017/9/12.
 */

public class AdapterFlushTimeData {
    private int time;
    private boolean open;

    public AdapterFlushTimeData(int time, boolean open) {
        this.time = time;
        this.open = open;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
