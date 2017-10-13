package com.tianyigps.online.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/9/21.
 */

public class InfoWindowBean {
    /**
     * time : 1505967288604
     * obj : [{"group_id":1,"group_name":"默认组","terminal_id":"4028818c5a4b078d015a4ee2872c0011","name":"184847有线","imei":"863014530145837","model":"1","icon":2,"redisobj":{"gps_time":"2017/09/20 09:13:42","speed":"-1","direction":"316","imei":"863014530145837","current_time":"2017/09/20 09:14:04","expire_time":"2018/09/20 09:13:43","terminal_type":"GT220","park_time":"2017/09/20 09:04:00","longitudeF":121.44189832971755,"latitudeF":31.347684093291807,"locate_time":"2017/09/20 09:13:42","distance":"0","locate_type":"1","longitude":"121.430833","latitude":"31.343133","acc_status":"01","parkwarn":"0"}}]
     * msg : 操作成功
     * success : true
     */

    private long time;
    private String msg;
    private boolean success;
    private List<ObjBean> obj;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
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
         * group_id : 1
         * group_name : 默认组
         * terminal_id : 4028818c5a4b078d015a4ee2872c0011
         * name : 184847有线
         * imei : 863014530145837
         * model : 1
         * icon : 2
         * redisobj : {"gps_time":"2017/09/20 09:13:42","speed":"-1","direction":"316","imei":"863014530145837","current_time":"2017/09/20 09:14:04","expire_time":"2018/09/20 09:13:43","terminal_type":"GT220","park_time":"2017/09/20 09:04:00","longitudeF":121.44189832971755,"latitudeF":31.347684093291807,"locate_time":"2017/09/20 09:13:42","distance":"0","locate_type":"1","longitude":"121.430833","latitude":"31.343133","acc_status":"01","parkwarn":"0"}
         */

        private int group_id;
        private String group_name;
        private String terminal_id;
        private String name;
        private String imei;
        private String model;
        private int icon;
        private RedisobjBean redisobj;

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public String getGroup_name() {
            return group_name;
        }

        public void setGroup_name(String group_name) {
            this.group_name = group_name;
        }

        public String getTerminal_id() {
            return terminal_id;
        }

        public void setTerminal_id(String terminal_id) {
            this.terminal_id = terminal_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImei() {
            return imei;
        }

        public void setImei(String imei) {
            this.imei = imei;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public RedisobjBean getRedisobj() {
            return redisobj;
        }

        public void setRedisobj(RedisobjBean redisobj) {
            this.redisobj = redisobj;
        }

        public static class RedisobjBean {
            /**
             * gps_time : 2017/09/20 09:13:42
             * speed : -1
             * direction : 316
             * imei : 863014530145837
             * station_code : 01CC000018C9000213
             * current_time : 2017/09/20 09:14:04
             * expire_time : 2018/09/20 09:13:43
             * terminal_type : GT220
             * park_time : 2017/09/20 09:04:00
             * longitudeF : 121.44189832971755
             * latitudeF : 31.347684093291807
             * locate_time : 2017/09/20 09:13:42
             * distance : 0
             * locate_type : 1
             * longitude : 121.430833
             * latitude : 31.343133
             * acc_status : 01
             * parkwarn : 0
             */

            private String gps_time;
            private String speed;
            private String direction;
            private String imei;
            private String station_code;
            private String current_time;
            private String expire_time;
            private String terminal_type;
            private String park_time;
            private double longitudeF;
            private double latitudeF;
            private String locate_time;
            private String distance;
            private String locate_type;
            private String longitude;
            private String latitude;
            private String acc_status;
            private String parkwarn;
            private int dianliang;

            public String getGps_time() {
                return gps_time;
            }

            public void setGps_time(String gps_time) {
                this.gps_time = gps_time;
            }

            public String getSpeed() {
                return speed;
            }

            public void setSpeed(String speed) {
                this.speed = speed;
            }

            public String getDirection() {
                return direction;
            }

            public void setDirection(String direction) {
                this.direction = direction;
            }

            public String getImei() {
                return imei;
            }

            public void setImei(String imei) {
                this.imei = imei;
            }

            public String getCurrent_time() {
                return current_time;
            }

            public void setCurrent_time(String current_time) {
                this.current_time = current_time;
            }

            public String getExpire_time() {
                return expire_time;
            }

            public void setExpire_time(String expire_time) {
                this.expire_time = expire_time;
            }

            public String getTerminal_type() {
                return terminal_type;
            }

            public void setTerminal_type(String terminal_type) {
                this.terminal_type = terminal_type;
            }

            public String getPark_time() {
                return park_time;
            }

            public void setPark_time(String park_time) {
                this.park_time = park_time;
            }

            public double getLongitudeF() {
                return longitudeF;
            }

            public void setLongitudeF(double longitudeF) {
                this.longitudeF = longitudeF;
            }

            public double getLatitudeF() {
                return latitudeF;
            }

            public void setLatitudeF(double latitudeF) {
                this.latitudeF = latitudeF;
            }

            public String getLocate_time() {
                return locate_time;
            }

            public void setLocate_time(String locate_time) {
                this.locate_time = locate_time;
            }

            public String getDistance() {
                return distance;
            }

            public void setDistance(String distance) {
                this.distance = distance;
            }

            public String getLocate_type() {
                return locate_type;
            }

            public void setLocate_type(String locate_type) {
                this.locate_type = locate_type;
            }

            public String getLongitude() {
                return longitude;
            }

            public void setLongitude(String longitude) {
                this.longitude = longitude;
            }

            public String getLatitude() {
                return latitude;
            }

            public void setLatitude(String latitude) {
                this.latitude = latitude;
            }

            public String getAcc_status() {
                return acc_status;
            }

            public void setAcc_status(String acc_status) {
                this.acc_status = acc_status;
            }

            public String getParkwarn() {
                return parkwarn;
            }

            public void setParkwarn(String parkwarn) {
                this.parkwarn = parkwarn;
            }

            public int getDianliang() {
                return dianliang;
            }

            public void setDianliang(int dianliang) {
                this.dianliang = dianliang;
            }

            public String getStation_code() {
                return station_code;
            }

            public void setStation_code(String station_code) {
                this.station_code = station_code;
            }
        }
    }
}
