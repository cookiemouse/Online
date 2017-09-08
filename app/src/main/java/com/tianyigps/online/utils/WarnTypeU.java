package com.tianyigps.online.utils;

/**
 * Created by cookiemouse on 2017/9/8.
 */

public class WarnTypeU {
    public static final String WARN_1 = "01";   //  断电报警
    public static final String WARN_2 = "02";   //  SOS报警
    public static final String WARN_3 = "03";   //  电池低电报警
    public static final String WARN_4 = "04";   //  振动报警
    public static final String WARN_5 = "05";   //  位移报警
    public static final String WARN_6 = "06";   //  进入盲区报警
    public static final String WARN_7 = "07";   //  离开盲区报警
    public static final String WARN_8 = "08";   //  GPS天线开路报警
    public static final String WARN_9 = "09";   //  GPS天线短路报警
    public static final String WARN_81 = "81";   //  低速报警
    public static final String WARN_82 = "82";   //  超速报警
    public static final String WARN_83 = "83";   //  入围栏报警
    public static final String WARN_84 = "84";   //  出围栏报警
    public static final String WARN_85 = "85";   //  碰撞报警
    public static final String WARN_86 = "86";   //  跌落报警
    public static final String WARN_93 = "93";   //  手机围栏报警
    public static final String WARN_94 = "94";   //  共享风险点出报警
    public static final String WARN_95 = "95";   //  共享风险点入报警
    public static final String WARN_96 = "96";   //  停车超时报警
    public static final String WARN_97 = "97";   //  出风险点报警
    public static final String WARN_98 = "98";   //  入风险点报警
    public static final String WARN_99 = "99";   //  光感报警

    public static final String getType(String type) {
        switch (type) {
            case WARN_1: {
                type = "断电报警";
                break;
            }
            case WARN_2: {
                type = "SOS报警";
                break;
            }
            case WARN_3: {
                type = "电池低电报警";
                break;
            }
            case WARN_4: {
                type = "振动报警";
                break;
            }
            case WARN_5: {
                type = "位移报警";
                break;
            }
            case WARN_6: {
                type = "进入盲区报警";
                break;
            }
            case WARN_7: {
                type = "离开盲区报警";
                break;
            }
            case WARN_8: {
                type = "GPS天线开路报警";
                break;
            }
            case WARN_9: {
                type = "GPS天线短路报警";
                break;
            }
            case WARN_81: {
                type = "低速报警";
                break;
            }
            case WARN_82: {
                type = "超速报警";
                break;
            }
            case WARN_83: {
                type = "入围栏报警";
                break;
            }
            case WARN_84: {
                type = "出围栏报警";
                break;
            }
            case WARN_85: {
                type = "碰撞报警";
                break;
            }
            case WARN_86: {
                type = "跌落报警";
                break;
            }
            case WARN_93: {
                type = "手机围栏报警";
                break;
            }
            case WARN_94: {
                type = "共享风险点出报警";
                break;
            }
            case WARN_95: {
                type = "共享风险点入报警";
                break;
            }
            case WARN_96: {
                type = "停车超时报警";
                break;
            }
            case WARN_97: {
                type = "出风险点报警";
                break;
            }
            case WARN_98: {
                type = "入风险点报警";
                break;
            }
            case WARN_99: {
                type = "光感报警";
                break;
            }
        }
        return type;
    }
}
