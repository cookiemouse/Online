package com.tianyigps.online.bean;

/**
 * Created by cookiemouse on 2017/10/26.
 */

public class CheckVersionBean {
    /**
     * time : 0
     * errCode :
     * obj : 2.0
     * msg : 操作成功
     * success : true
     */

    private int time;
    private String errCode;
    private String obj;
    private String msg;
    private boolean success;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getObj() {
        return obj;
    }

    public void setObj(String obj) {
        this.obj = obj;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
