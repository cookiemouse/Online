package com.tianyigps.online.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/9/13.
 */

public class AttentionBean {
    /**
     * time : 0
     * obj : [{"model":"1","status":"离线","name":"306551有线1","imei":"354430015035968","cid":17043},{"model":"1","status":"离线","name":"苏C123456有线","imei":"868120140733178","cid":11684},{"model":"2","status":"离线","name":"003802无线","imei":"14141892116","cid":667},{"model":"2","status":"休眠","name":"沪A12345无线7495","imei":"14380027495","cid":11653},{"model":"1","status":"离线","name":"176550有线1","imei":"352544072111017","cid":17043},{"model":"2","status":"未启用","name":"ZRA5","imei":"xh1209ZRA500051","cid":1},{"model":"2","status":"未启用","name":"ZR300","imei":"xh1212ZR3000064","cid":1},{"model":"1","status":"离线","name":"沪A124567有线","imei":"352544071797816","cid":11684},{"model":"1","status":"静止","name":"沪BDN999大通车","imei":"863014530007390","cid":762},{"model":"1","status":"离线","name":"863014530183538","imei":"863014530183538","cid":358},{"model":"1","status":"离线","name":"沪G658093有线","imei":"863014530055706","cid":17044},{"model":"1","status":"离线","name":"184847有线","imei":"863014530145837","cid":1},{"model":"1","status":"静止","name":"洗啊11","imei":"32160516150069","cid":1},{"model":"1","status":"离线","name":"沪A101010有线2","imei":"866686022663636","cid":17041},{"model":"1","status":"离线","name":"沪A101010有线1","imei":"354188047675744","cid":17041},{"model":"1","status":"静止","name":"小李1","imei":"863014530145883","cid":1},{"model":"2","status":"离线","name":"沪C66666无线","imei":"14470154034","cid":1},{"model":"1","status":"离线","name":"洗啊111","imei":"352544072110852","cid":1},{"model":"1","status":"静止","name":"863014530523464","imei":"863014530523464","cid":1}]
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
         * model : 1
         * status : 离线
         * name : 306551有线1
         * imei : 354430015035968
         * cid : 17043
         */

        private String model;
        private String status;
        private String name;
        private String imei;
        private int cid;

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }
    }
}
