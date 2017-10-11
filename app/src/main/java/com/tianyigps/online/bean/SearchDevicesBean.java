package com.tianyigps.online.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/10/10.
 */

public class SearchDevicesBean {
    /**
     * time : 0
     * obj : [{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-005566","imei":"10000005566","cid":17128,"online":"N"},{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-015566","imei":"10000015566","cid":17129,"online":"N"},{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-025566","imei":"10000025566","cid":17130,"online":"N"},{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-035566","imei":"10000035566","cid":17132,"online":"N"},{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-045566","imei":"10000045566","cid":17134,"online":"N"},{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-055566","imei":"10000055566","cid":17136,"online":"N"},{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-055660","imei":"10000055660","cid":17136,"online":"N"},{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-055661","imei":"10000055661","cid":17136,"online":"N"},{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-055662","imei":"10000055662","cid":17136,"online":"N"},{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-055663","imei":"10000055663","cid":17136,"online":"N"},{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-055664","imei":"10000055664","cid":17136,"online":"N"},{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-055665","imei":"10000055665","cid":17136,"online":"N"},{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-055666","imei":"10000055666","cid":17136,"online":"N"},{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-055667","imei":"10000055667","cid":17136,"online":"N"},{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-055668","imei":"10000055668","cid":17136,"online":"N"},{"isAttention":false,"terminalStatus":"未启用","model":"2","name":"Q3-055669","imei":"10000055669","cid":17136,"online":"N"}]
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
         * isAttention : false
         * terminalStatus : 未启用
         * model : 2
         * attention_id : 1
         * name : Q3-005566
         * imei : 10000005566
         * cid : 17128
         * margin : 18164077
         * online : N
         */

        private boolean isAttention;
        private String terminalStatus;
        private String model;
        private int attention_id;
        private String name;
        private String imei;
        private int cid;
        private double margin;
        private String online;

        public boolean isIsAttention() {
            return isAttention;
        }

        public void setIsAttention(boolean isAttention) {
            this.isAttention = isAttention;
        }

        public String getTerminalStatus() {
            return terminalStatus;
        }

        public void setTerminalStatus(String terminalStatus) {
            this.terminalStatus = terminalStatus;
        }

        public String getModel() {
            return model;
        }

        public void setModel(String model) {
            this.model = model;
        }

        public int getAttention_id() {
            return attention_id;
        }

        public void setAttention_id(int attention_id) {
            this.attention_id = attention_id;
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

        public double getMargin() {
            return margin;
        }

        public void setMargin(double margin) {
            this.margin = margin;
        }

        public int getCid() {
            return cid;
        }

        public void setCid(int cid) {
            this.cid = cid;
        }

        public String getOnline() {
            return online;
        }

        public void setOnline(String online) {
            this.online = online;
        }
    }
}
