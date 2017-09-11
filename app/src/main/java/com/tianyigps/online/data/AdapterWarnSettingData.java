package com.tianyigps.online.data;

/**
 * Created by cookiemouse on 2017/9/11.
 */

public class AdapterWarnSettingData {
    private String type;
    private boolean open;

    public AdapterWarnSettingData(String type, boolean open) {
        this.type = type;
        this.open = open;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
