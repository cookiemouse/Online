package com.tianyigps.online.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/9/25.
 */

public class EnclosureBean {
    /**
     * success : true
     * obj : {"imei":"863014530145883","type":2,"points":[[118.7694539023626,31.96436548813935],[118.7904970170684,31.96507764433353],[118.7909147335168,31.95221171538162],[118.7724302082686,31.95238973935926],[118.7694539023626,31.96436548813935]]}
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
         * imei : 863014530145883
         * type : 2
         * points : [[118.7694539023626,31.96436548813935],[118.7904970170684,31.96507764433353],[118.7909147335168,31.95221171538162],[118.7724302082686,31.95238973935926],[118.7694539023626,31.96436548813935]]
         */

        private String imei;
        private int type;
        private List<List<Double>> points;

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public List<List<Double>> getPoints() {
            return points;
        }

        public void setPoints(List<List<Double>> points) {
            this.points = points;
        }
    }
}
