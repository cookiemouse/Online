package com.tianyigps.online.bean;

import java.util.List;

/**
 * Created by cookiemouse on 2017/10/18.
 */

public class UnifenceSetBean {
    /**
     * imei : 863014530523464
     * type : 2
     * point : []
     * radius : 5000
     * points : [[118.78684,32.023927138164225],[118.80057293874715,32.022394954161314],[118.81337000041344,32.017902817960305],[118.82435908639447,32.0107568612197],[118.83279130864089,32.00144406908211],[118.8380920251416,31.990599092941615],[118.83990000082686,31.978961],[118.8380920251416,31.96732290705839],[118.83279130864089,31.956477930917888],[118.82435908639447,31.947165138780306],[118.81337000041344,31.940019182039702],[118.80057293874715,31.935527045838693],[118.78684,31.933994861835775],[118.77310706125284,31.935527045838693],[118.76030999958655,31.940019182039702],[118.74932091360554,31.947165138780306],[118.7408886913591,31.956477930917888],[118.73558797485839,31.96732290705839],[118.73377999917312,31.978961],[118.73558797485839,31.990599092941615],[118.7408886913591,32.00144406908211],[118.74932091360554,32.0107568612197],[118.76030999958655,32.017902817960305],[118.77310706125284,32.022394954161314],[118.78684,32.023927138164225]]
     */

    private String imei;
    private int type;
    private String radius;
    private List<Double> point;
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

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public List<Double> getPoint() {
        return point;
    }

    public void setPoint(List<Double> point) {
        this.point = point;
    }

    public List<List<Double>> getPoints() {
        return points;
    }

    public void setPoints(List<List<Double>> points) {
        this.points = points;
    }
}
