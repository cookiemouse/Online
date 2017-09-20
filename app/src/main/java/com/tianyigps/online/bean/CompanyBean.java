package com.tianyigps.online.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/9/18.
 */

public class CompanyBean {
    /**
     * time : 0
     * obj : [{"id":"11690","name":"1","leaf":true},{"id":"303","name":"1李滟文测试","leaf":false},{"id":"17070","name":"glzh1","leaf":false},{"id":"411","name":"S9测试","leaf":true},{"id":"1119","name":"SQI时代之光","leaf":true},{"id":"161","name":"test45","leaf":true},{"id":"17041","name":"zpe公司","leaf":true},{"id":"17067","name":"宝新测试","leaf":false},{"id":"628","name":"北京移动测试","leaf":true},{"id":"762","name":"标准接口测试","leaf":false},{"id":"2","name":"测试客户001","leaf":false},{"id":"14","name":"测试客户7","leaf":true},{"id":"1146","name":"测试组","leaf":true},{"id":"1145","name":"陈硕","leaf":false},{"id":"1126","name":"创富TEST0-shanghai(宝山)001","leaf":true},{"id":"11652","name":"东莞团贷网","leaf":false},{"id":"17014","name":"非凡贷遇","leaf":false},{"id":"17029","name":"服务主任","leaf":false},{"id":"288","name":"工程车","leaf":true},{"id":"17071","name":"关联账号","leaf":false},{"id":"17023","name":"黄义攀","leaf":true},{"id":"17060","name":"灰度测试","leaf":true},{"id":"766","name":"技术测试账号","leaf":false},{"id":"353","name":"建元广联测试","leaf":false},{"id":"11660","name":"建元资本（中国）融资租赁有限公司 ","leaf":false},{"id":"17034","name":"交银租赁","leaf":false},{"id":"17036","name":"坤鹏融资租赁","leaf":false},{"id":"11653","name":"联调接口用于测试","leaf":false},{"id":"10477","name":"六六顺","leaf":false},{"id":"848","name":"绿地集团","leaf":true},{"id":"667","name":"南京开发测试用","leaf":true},{"id":"17047","name":"派工2.0测试","leaf":true},{"id":"17052","name":"派工接口对接","leaf":true},{"id":"542","name":"庞大测试","leaf":true},{"id":"564","name":"萍水相逢","leaf":true},{"id":"17055","name":"奇瑞测试（误删）","leaf":false},{"id":"163","name":"融信","leaf":true},{"id":"17017","name":"睿本金融","leaf":true},{"id":"481","name":"上海移动测试","leaf":true},{"id":"736","name":"搜易贷","leaf":false},{"id":"1118","name":"苏州沪众汽车销售服务有限公司","leaf":true},{"id":"279","name":"唐世宣（测试）1","leaf":true},{"id":"563","name":"唐世宣测试（勿动）","leaf":true},{"id":"899","name":"天翼1","leaf":false},{"id":"921","name":"天翼23","leaf":false},{"id":"996","name":"天翼99","leaf":false},{"id":"11651","name":"团贷网","leaf":false},{"id":"17004","name":"王捷","leaf":false},{"id":"17046","name":"王捷1","leaf":false},{"id":"17040","name":"微巴科技","leaf":false},{"id":"763","name":"微贷网测试","leaf":false},{"id":"314","name":"享元金融","leaf":false},{"id":"292","name":"新909测试","leaf":false},{"id":"11662","name":"许凤娇测试","leaf":true},{"id":"822","name":"研发指令测试","leaf":false},{"id":"1122","name":"盐城经济技术开发区悦泰汽车服务有限公司 ","leaf":false},{"id":"11661","name":"优信测试","leaf":false},{"id":"17008","name":"暂不用客户（暂存不删）","leaf":false},{"id":"11693","name":"张佩恩","leaf":true},{"id":"774","name":"张显超cz","leaf":true},{"id":"1136","name":"支洋测试","leaf":true},{"id":"1144","name":"中进汽贸","leaf":false},{"id":"17054","name":"中资联","leaf":false},{"id":"11657","name":"重庆华本融资租赁有限公司","leaf":true},{"id":"309","name":"周杰测试","leaf":true},{"id":"17069","name":"转移","leaf":false}]
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
         * id : 11690
         * name : 1
         * leaf : true
         */

        private String id;
        private String name;
        private boolean leaf;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public boolean isLeaf() {
            return leaf;
        }

        public void setLeaf(boolean leaf) {
            this.leaf = leaf;
        }
    }
}
