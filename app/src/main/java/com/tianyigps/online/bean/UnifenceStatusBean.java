package com.tianyigps.online.bean;

/**
 * Created by cookiemouse on 2017/9/25.
 */

public class UnifenceStatusBean {
    /**
     * success : true
     * obj : {"status":"1"}
     */

    private boolean success;
    private ObjBean obj;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * status : 1
         */

        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
