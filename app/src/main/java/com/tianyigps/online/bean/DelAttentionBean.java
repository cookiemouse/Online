package com.tianyigps.online.bean;

/**
 * Created by cookiemouse on 2017/9/20.
 */

public class DelAttentionBean {
    /**
     * time : 0
     * msg : 删除关注成功
     * success : true
     */

    private int time;
    private String msg;
    private boolean success;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
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
