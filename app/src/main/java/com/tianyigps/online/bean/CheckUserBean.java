package com.tianyigps.online.bean;

/**
 * Created by cookiemouse on 2017/9/4.
 */

public class CheckUserBean {
    /**
     * time : 0
     * obj : {"data":{"contactPhone":"17700001111","token":"456ee2cd38fd02b1","contactName":"1111123","name":"天易根客户1","contactAddr":"cesada","path":"#1#","cid":1}}
     * msg : 验证通过
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
         * data : {"contactPhone":"17700001111","token":"456ee2cd38fd02b1","contactName":"1111123","name":"天易根客户1","contactAddr":"cesada","path":"#1#","cid":1}
         */

        private DataBean data;

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * contactPhone : 17700001111
             * token : 456ee2cd38fd02b1
             * contactName : 1111123
             * name : 天易根客户1
             * contactAddr : cesada
             * path : #1#
             * cid : 1
             */

            private String contactPhone;
            private String token;
            private String contactName;
            private String name;
            private String contactAddr;
            private String path;
            private int cid;

            public String getContactPhone() {
                return contactPhone;
            }

            public void setContactPhone(String contactPhone) {
                this.contactPhone = contactPhone;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }

            public String getContactName() {
                return contactName;
            }

            public void setContactName(String contactName) {
                this.contactName = contactName;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getContactAddr() {
                return contactAddr;
            }

            public void setContactAddr(String contactAddr) {
                this.contactAddr = contactAddr;
            }

            public String getPath() {
                return path;
            }

            public void setPath(String path) {
                this.path = path;
            }

            public int getCid() {
                return cid;
            }

            public void setCid(int cid) {
                this.cid = cid;
            }
        }
    }
}
