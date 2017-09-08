package com.tianyigps.online.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/9/8.
 */

public class WarnListBean {
    /**
     * time : 0
     * obj : [{"id":"4a7e12431ca448c6b10fb022339843bd","locate_type":0,"station_code":"01CC000025FB000E77","imei":"861358033470543","name":"M5-001","locate_time":"2017/09/07 21:31:34","receive_time":"2017/09/07 21:31:41","speed":0,"direction":341,"latitude":22.59845,"longitude":113.86824,"warn_type":"01"},{"id":"f4494b1523ad404380aa32e9d0860494","locate_type":0,"station_code":"01CC000025FB000E77","imei":"861358033470543","name":"M5-001","locate_time":"2017/09/07 21:30:14","receive_time":"2017/09/07 21:30:18","speed":0,"direction":341,"latitude":22.59845,"longitude":113.86824,"warn_type":"01"}]
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
         * id : 4a7e12431ca448c6b10fb022339843bd
         * locate_type : 0
         * station_code : 01CC000025FB000E77
         * imei : 861358033470543
         * name : M5-001
         * locate_time : 2017/09/07 21:31:34
         * receive_time : 2017/09/07 21:31:41
         * speed : 0
         * direction : 341
         * latitude : 22.59845
         * longitude : 113.86824
         * warn_type : 01
         */

        private String id;
        private int locate_type;
        private String station_code;
        private String imei;
        private String name;
        private String locate_time;
        private String receive_time;
        private int speed;
        private int direction;
        private double latitude;
        private double longitude;
        private String warn_type;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public int getLocate_type() {
            return locate_type;
        }

        public void setLocate_type(int locate_type) {
            this.locate_type = locate_type;
        }

        public String getStation_code() {
            return station_code;
        }

        public void setStation_code(String station_code) {
            this.station_code = station_code;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLocate_time() {
            return locate_time;
        }

        public void setLocate_time(String locate_time) {
            this.locate_time = locate_time;
        }

        public String getReceive_time() {
            return receive_time;
        }

        public void setReceive_time(String receive_time) {
            this.receive_time = receive_time;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        public int getDirection() {
            return direction;
        }

        public void setDirection(int direction) {
            this.direction = direction;
        }

        public double getLatitude() {
            return latitude;
        }

        public void setLatitude(double latitude) {
            this.latitude = latitude;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
        }

        public String getWarn_type() {
            return warn_type;
        }

        public void setWarn_type(String warn_type) {
            this.warn_type = warn_type;
        }
    }
}
