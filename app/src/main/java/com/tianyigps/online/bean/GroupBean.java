package com.tianyigps.online.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/9/19.
 */

public class GroupBean {
    /**
     * time : 0
     * obj : [{"id":422,"name":"默认组"},{"id":1439,"name":"12"}]
     * msg : 查询成功
     * success : true
     */

    private int time;
    private String msg;
    private boolean success;
    private List<ObjBean> obj;

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

    public List<ObjBean> getObj() {
        return obj;
    }

    public void setObj(List<ObjBean> obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * id : 422
         * name : 默认组
         */

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
