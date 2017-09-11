package com.tianyigps.online.bean;

/**
 * Created by cookiemouse on 2017/9/11.
 */

public class TerminalInfo4MapBean {
    /**
     * time : 1505116865969
     * obj : {"group_id":1,"group_name":"默认组","terminal_id":"4028818c5dcf1df5015dcf3c76610000","name":"洗啊111","imei":"352544072110852","model":"1","icon":2,"redisobj":{"gps_time":"2017/09/09 21:45:27","speed":-1,"station_code":"01CC000018C9003F1F","direction":0,"imei":"352544072110852","current_time":"2017/09/09 22:27:07","defaultLocZJB":false,"expire_time":"2018/09/09 22:27:07","terminal_type":"TY01","park_time":"2017/09/04 18:03:23","longitudeF":121.4329153440613,"latitudeF":31.33340568404483,"locate_time":"2017/09/09 22:27:06","distance":0,"latitudeGDZJB":31.347651346827913,"longitudeGDZJB":121.44113940909904,"locate_type":0,"longitude":121.430072,"latitude":31.343103,"acc_status":"01","parkwarn":"null","lbs_time":"2017/09/09 22:27:06"}}
     * msg : 查询成功
     * success : true
     */

    private long time;
    private ObjBean obj;
    private String msg;
    private boolean success;

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
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
         * group_id : 1
         * group_name : 默认组
         * terminal_id : 4028818c5dcf1df5015dcf3c76610000
         * name : 洗啊111
         * imei : 352544072110852
         * model : 1
         * icon : 2
         * redisobj : {"gps_time":"2017/09/09 21:45:27","speed":-1,"station_code":"01CC000018C9003F1F","direction":0,"imei":"352544072110852","current_time":"2017/09/09 22:27:07","defaultLocZJB":false,"expire_time":"2018/09/09 22:27:07","terminal_type":"TY01","park_time":"2017/09/04 18:03:23","longitudeF":121.4329153440613,"latitudeF":31.33340568404483,"locate_time":"2017/09/09 22:27:06","distance":0,"latitudeGDZJB":31.347651346827913,"longitudeGDZJB":121.44113940909904,"locate_type":0,"longitude":121.430072,"latitude":31.343103,"acc_status":"01","parkwarn":"null","lbs_time":"2017/09/09 22:27:06"}
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
             * gps_time : 2017/09/09 21:45:27
             * speed : -1
             * station_code : 01CC000018C9003F1F
             * direction : 0
             * imei : 352544072110852
             * current_time : 2017/09/09 22:27:07
             * defaultLocZJB : false
             * expire_time : 2018/09/09 22:27:07
             * terminal_type : TY01
             * park_time : 2017/09/04 18:03:23
             * longitudeF : 121.4329153440613
             * latitudeF : 31.33340568404483
             * locate_time : 2017/09/09 22:27:06
             * distance : 0
             * latitudeGDZJB : 31.347651346827913
             * longitudeGDZJB : 121.44113940909904
             * locate_type : 0
             * longitude : 121.430072
             * latitude : 31.343103
             * acc_status : 01
             * parkwarn : null
             * lbs_time : 2017/09/09 22:27:06
             */

            private String gps_time;
            private int speed;
            private String station_code;
            private int direction;
            private String imei;
            private String current_time;
            private boolean defaultLocZJB;
            private String expire_time;
            private String terminal_type;
            private String park_time;
            private double longitudeF;
            private double latitudeF;
            private String locate_time;
            private int distance;
            private double latitudeGDZJB;
            private double longitudeGDZJB;
            private int locate_type;
            private double longitude;
            private double latitude;
            private String acc_status;
            private String parkwarn;
            private String lbs_time;

            public String getGps_time() {
                return gps_time;
            }

            public void setGps_time(String gps_time) {
                this.gps_time = gps_time;
            }

            public int getSpeed() {
                return speed;
            }

            public void setSpeed(int speed) {
                this.speed = speed;
            }

            public String getStation_code() {
                return station_code;
            }

            public void setStation_code(String station_code) {
                this.station_code = station_code;
            }

            public int getDirection() {
                return direction;
            }

            public void setDirection(int direction) {
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

            public boolean isDefaultLocZJB() {
                return defaultLocZJB;
            }

            public void setDefaultLocZJB(boolean defaultLocZJB) {
                this.defaultLocZJB = defaultLocZJB;
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

            public int getDistance() {
                return distance;
            }

            public void setDistance(int distance) {
                this.distance = distance;
            }

            public double getLatitudeGDZJB() {
                return latitudeGDZJB;
            }

            public void setLatitudeGDZJB(double latitudeGDZJB) {
                this.latitudeGDZJB = latitudeGDZJB;
            }

            public double getLongitudeGDZJB() {
                return longitudeGDZJB;
            }

            public void setLongitudeGDZJB(double longitudeGDZJB) {
                this.longitudeGDZJB = longitudeGDZJB;
            }

            public int getLocate_type() {
                return locate_type;
            }

            public void setLocate_type(int locate_type) {
                this.locate_type = locate_type;
            }

            public double getLongitude() {
                return longitude;
            }

            public void setLongitude(double longitude) {
                this.longitude = longitude;
            }

            public double getLatitude() {
                return latitude;
            }

            public void setLatitude(double latitude) {
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

            public String getLbs_time() {
                return lbs_time;
            }

            public void setLbs_time(String lbs_time) {
                this.lbs_time = lbs_time;
            }
        }
    }
}
