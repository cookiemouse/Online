package com.tianyigps.online.bean;

/**
 * Created by cookiemouse on 2017/11/2.
 */

public class UnifenceResultBean {
    /**
     * success : false
     * msg : 不支持此种类型的多边形
     */

    private boolean success;
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
