package com.tianyigps.online.bean;

/**
 * Created by cookiemouse on 2017/9/19.
 */

public class NumberBean {
    /**
     * time : 0
     * obj : {"offLine":1,"online":0}
     * msg : 查询成功
     * success : true
     */

    private int time;
    private ObjBean obj;
    private String msg;
    private boolean success;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
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

    public static class ObjBean {
        /**
         * offLine : 1
         * online : 0
         */

        private int offLine;
        private int online;

        public int getOffLine() {
            return offLine;
        }

        public void setOffLine(int offLine) {
            this.offLine = offLine;
        }

        public int getOnline() {
            return online;
        }

        public void setOnline(int online) {
            this.online = online;
        }
    }
}
