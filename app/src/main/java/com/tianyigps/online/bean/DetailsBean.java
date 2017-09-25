package com.tianyigps.online.bean;

/**
 * Created by cookiemouse on 2017/9/25.
 */

public class DetailsBean {
    /**
     * time : 0
     * obj : {"id":"4028818c5e5ba833015e60ccca570003","imei":"863014530523464","model":"1","sim":"898607B0101770625467","name":"863014530523464","car_no":"","car_type":"","create_time":1504862587000,"sale_time":1504800000000,"start_date":1504800000000,"end_date":1504800000000,"alarm_speed":0,"contact_name":"","contact_phone":"","icon":2,"remark":"","group_id":1,"customer_id":1,"car_brand":"","install_location":"","install_person":"","model_type_detail":"24","online_time":1506012969000,"offline_time":1505232870000,"cus_remark":"","car_vin":"","lst_opr_time":1504800000000,"idCard":"","sync_status":0,"update_time":1504862587000,"rvr_imei":"464325035410368","model_name":"M5"}
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
         * id : 4028818c5e5ba833015e60ccca570003
         * imei : 863014530523464
         * model : 1
         * sim : 898607B0101770625467
         * name : 863014530523464
         * car_no :
         * car_type :
         * create_time : 1504862587000
         * sale_time : 1504800000000
         * start_date : 1504800000000
         * end_date : 1504800000000
         * alarm_speed : 0
         * contact_name :
         * contact_phone :
         * icon : 2
         * remark :
         * group_id : 1
         * customer_id : 1
         * car_brand :
         * install_location :
         * install_person :
         * model_type_detail : 24
         * online_time : 1506012969000
         * offline_time : 1505232870000
         * cus_remark :
         * car_vin :
         * lst_opr_time : 1504800000000
         * idCard :
         * sync_status : 0
         * update_time : 1504862587000
         * rvr_imei : 464325035410368
         * model_name : M5
         */

        private String id;
        private String imei;
        private String model;
        private String sim;
        private String name;
        private String car_no;
        private String car_type;
        private long create_time;
        private long sale_time;
        private long start_date;
        private long end_date;
        private int alarm_speed;
        private String contact_name;
        private String contact_phone;
        private int icon;
        private String remark;
        private int group_id;
        private int customer_id;
        private String car_brand;
        private String install_location;
        private String install_person;
        private String model_type_detail;
        private long online_time;
        private long offline_time;
        private String cus_remark;
        private String car_vin;
        private long lst_opr_time;
        private String idCard;
        private int sync_status;
        private long update_time;
        private String rvr_imei;
        private String model_name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getSim() {
            return sim;
        }

        public void setSim(String sim) {
            this.sim = sim;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getCar_no() {
            return car_no;
        }

        public void setCar_no(String car_no) {
            this.car_no = car_no;
        }

        public String getCar_type() {
            return car_type;
        }

        public void setCar_type(String car_type) {
            this.car_type = car_type;
        }

        public long getCreate_time() {
            return create_time;
        }

        public void setCreate_time(long create_time) {
            this.create_time = create_time;
        }

        public long getSale_time() {
            return sale_time;
        }

        public void setSale_time(long sale_time) {
            this.sale_time = sale_time;
        }

        public long getStart_date() {
            return start_date;
        }

        public void setStart_date(long start_date) {
            this.start_date = start_date;
        }

        public long getEnd_date() {
            return end_date;
        }

        public void setEnd_date(long end_date) {
            this.end_date = end_date;
        }

        public int getAlarm_speed() {
            return alarm_speed;
        }

        public void setAlarm_speed(int alarm_speed) {
            this.alarm_speed = alarm_speed;
        }

        public String getContact_name() {
            return contact_name;
        }

        public void setContact_name(String contact_name) {
            this.contact_name = contact_name;
        }

        public String getContact_phone() {
            return contact_phone;
        }

        public void setContact_phone(String contact_phone) {
            this.contact_phone = contact_phone;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public int getGroup_id() {
            return group_id;
        }

        public void setGroup_id(int group_id) {
            this.group_id = group_id;
        }

        public int getCustomer_id() {
            return customer_id;
        }

        public void setCustomer_id(int customer_id) {
            this.customer_id = customer_id;
        }

        public String getCar_brand() {
            return car_brand;
        }

        public void setCar_brand(String car_brand) {
            this.car_brand = car_brand;
        }

        public String getInstall_location() {
            return install_location;
        }

        public void setInstall_location(String install_location) {
            this.install_location = install_location;
        }

        public String getInstall_person() {
            return install_person;
        }

        public void setInstall_person(String install_person) {
            this.install_person = install_person;
        }

        public String getModel_type_detail() {
            return model_type_detail;
        }

        public void setModel_type_detail(String model_type_detail) {
            this.model_type_detail = model_type_detail;
        }

        public long getOnline_time() {
            return online_time;
        }

        public void setOnline_time(long online_time) {
            this.online_time = online_time;
        }

        public long getOffline_time() {
            return offline_time;
        }

        public void setOffline_time(long offline_time) {
            this.offline_time = offline_time;
        }

        public String getCus_remark() {
            return cus_remark;
        }

        public void setCus_remark(String cus_remark) {
            this.cus_remark = cus_remark;
        }

        public String getCar_vin() {
            return car_vin;
        }

        public void setCar_vin(String car_vin) {
            this.car_vin = car_vin;
        }

        public long getLst_opr_time() {
            return lst_opr_time;
        }

        public void setLst_opr_time(long lst_opr_time) {
            this.lst_opr_time = lst_opr_time;
        }

        public String getIdCard() {
            return idCard;
        }

        public void setIdCard(String idCard) {
            this.idCard = idCard;
        }

        public int getSync_status() {
            return sync_status;
        }

        public void setSync_status(int sync_status) {
            this.sync_status = sync_status;
        }

        public long getUpdate_time() {
            return update_time;
        }

        public void setUpdate_time(long update_time) {
            this.update_time = update_time;
        }

        public String getRvr_imei() {
            return rvr_imei;
        }

        public void setRvr_imei(String rvr_imei) {
            this.rvr_imei = rvr_imei;
        }

        public String getModel_name() {
            return model_name;
        }

        public void setModel_name(String model_name) {
            this.model_name = model_name;
        }
    }
}
