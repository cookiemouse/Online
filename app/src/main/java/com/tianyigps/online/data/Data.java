package com.tianyigps.online.data;

/**
 * Created by cookiemouse on 2017/8/21.
 */

public class Data {

    //  Handler分类
    public final static int MSG_ERO = 0x9999;
    public final static int MSG_MSG = 0x8888;
    public final static int MSG_NOTHING = 0x7777;
    public final static int MSG_1 = 1;
    public final static int MSG_2 = 2;
    public final static int MSG_3 = 3;
    public final static int MSG_4 = 4;
    public final static int MSG_5 = 5;
    public final static int MSG_6 = 6;
    public final static int MSG_7 = 7;
    public final static int MSG_8 = 8;
    public final static int MSG_9 = 9;
    public final static int MSG_10 = 10;


    //  网络错误
    public static final String DEFAULT_MESSAGE = "网络请求失败，请检查网络！";

    //  SharedPreferences
    public final static String DATA_SHAREDPREFERENCES = "online_sharedpreferences";

    public static final String CHECK_USER_CID = "check_user_cid";
    public static final String CHECK_USER_PATH = "check_user_path";
    public static final String CHECK_USER_CONTACT_ADDR = "check_user_contact_addr";
    public static final String CHECK_USER_CONTACT_PHONE = "check_user_contact_phone";
    public static final String CHECK_USER_CONTACT_NAME = "check_user_contact_name";
    public static final String CHECK_USER_NAME = "check_user_name";
    public static final String CHECK_USER_TOKEN = "check_user_token";
    public static final String CHECK_USER_ACCOUNT = "check_user_account";
    public static final String CHECK_USER_PASSWORD = "check_user_password";

    public static final String LOGIN_REMEMBER_PASSWORD = "login_remember_password";
    public static final String LOGIN_AUTO = "login_auto";

    public static final String IS_WARN = "is_warn";
    public static final String WARN_TYPE = "warn_type";

    public static final String FLUSH_TIME = "flush_time";

    public static final String SHOW_ATTENTION = "show_attention";

    public static final String MAIN_PAGE = "main_page";
    public static final String DATE = "date";

    //  延时时间
    public static final int DELAY_200 = 200;
    public static final int DELAY_500 = 500;
    public static final int DELAY_1000 = 1000;
    public static final int DELAY_2000 = 2000;
    public static final int DELAY_4000 = 4000;

    //  数据库，表
    public final static String DATA_DB_NAME = "tianyi_online_database_db.db";
    public final static String TAB_ACCOUNT = "tab_account";

    //  Intent
    public final static String INTENT_IMEI = "intent_imei";

    public final static String INTENT_WARN_TYPE = "intent_warn_type";
    public final static String INTENT_DATE = "intent_date";
    public final static String INTENT_NAME = "intent_name";
    public final static String INTENT_LATITUDE = "intent_latitude";
    public final static String INTENT_LONGITUDE = "intent_longitude";

    //  BundleKey
    public final static String KEY_CID = "key_cid";
    public final static String KEY_IMEI = "key_imei";
    public final static String KEY_SPEED = "key_speed";
    public final static String KEY_TYPE = "key_type";

    //  定位类型
    public final static int LOCATE_BASE = 0;
    public final static int LOCATE_GPS = 1;
    public final static int LOCATE_NO = 2;

    //  设备状态
    public final static int STATUS_OTHER = 0;
    public final static int STATUS_ON = 1;
    public final static int STATUS_OFF = 2;
    public final static int MARKER_CLUSTER = 3;
    public final static int STATUS_RUNNING = 4;

    //  轨迹回放速度
    public final static int SPEED_10 = 5000 / 250;  //  5000 / 250
    public final static int SPEED_50 = 5000 / 200;  //  5000 / 200
    public final static int SPEED_100 = 5000 / 150; //  5000 / 150
    public final static int SPEED_200 = 5000 / 100; //  5000 / 100
    public final static int SPEED_400 = 5000 / 50;  //  5000 / 50

    //  埋点信息
    public final static String BURIED_POINT_1_N = "-1";
    public final static String BURIED_POINT_2_N = "-2";
    public final static String BURIED_POINT_1 = "1";
    public final static String BURIED_POINT_2 = "2";
    public final static String BURIED_POINT_3 = "3";
    public final static String BURIED_POINT_4 = "4";
}
