package com.tianyigps.online.utils;

/**
 * Created by cookiemouse on 2017/9/22.
 */

public class LocateTypeU {
    public static String getLocateType(String type) {
        String locateType;
        switch (type) {
            case "0": {
                locateType = "基站定位";
                break;
            }
            case "1": {
                locateType = "GPS";
                break;
            }
            case "2": {
                locateType = "上线不定位";
                break;
            }
            default: {
                locateType = "";
            }
        }
        return locateType;
    }
}
