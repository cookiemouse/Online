package com.tianyigps.online.data;

/**
 * Created by cookiemouse on 2017/9/4.
 */

public class Urls {

    private static final String IP = "http://sit.tianyigps.cn/app-service/app";
//    private static final String IP = "http://www.tianyigps.cn/app";

    public static final String CHECK_USER = IP + "/loginController.do?checkuser&";
    public static final String CHECK_VERSION = IP + "/loginController.do?checkVersion&";
    public static final String SHOW_CUSTOMERS = IP + "/customerController.do?showCustomers&";
    public static final String ATTENTION_LIST = IP + "/customerController.do?listAttention&";
    public static final String ADD_ATTENTION = IP + "/customerController.do?addAttention&";
    public static final String DEL_ATTENTION = IP + "/customerController.do?delAttention&";
    public static final String FEEDBACK = IP + "/customerController.do?feedback&";
    public static final String SHOW_TERMINAL_INFO = IP + "/terminalController.do?showTerminalInfo&";
    public static final String FIND_HIS_POINTS = IP + "/terminalController.do?findHisPoints&";
    public static final String GET_NUM_WITH_STATUS = IP + "/terminalController.do?queryNumWithStatus&";
    public static final String GET_GROUP = IP + "/terminalController.do?queryGroup&";
    public static final String GET_TERMINAL_BY_GROUP_ID = IP + "/terminalController.do?queryTerminalByGroupid&";
    public static final String REFRESH_TERMINAL_LIST = IP + "/terminalController.do?refreshTerminalList&";
    public static final String SHOW_POINT_NEW = IP + "/terminalController.do?showPonitNew&";
    public static final String SEARCH_TERMINAL_WITH_STATUS = IP + "/terminalController.do?searchTerminalWithStatus&";
    public static final String SHOW_TERMINAL_INFO_FOR_MAP = IP + "/terminalController.do?showTerminalInfo4mapNew&";
    public static final String GET_WARN_LIST = IP + "/warnController.do?queryWarnList&";

    public static final String UNIFENCE_STATUS = IP + "/geofenceapi/unifencestatus?";
    public static final String UNIFENCE_OPR = IP + "/geofenceapi/unifenceopr?";
    public static final String UNIFENCE_INFO= IP + "/geofenceapi/unifenceinfo?";
    public static final String UNIFENCE_UPSERT= IP + "/geofenceapi/unifenceupsert?";
}
