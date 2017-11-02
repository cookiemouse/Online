package com.tianyigps.online.utils;

import com.tianyigps.online.data.Data;
import com.tianyigps.online.data.StatusData;

/**
 * Created by cookiemouse on 2017/9/22.
 */

public class StatusU {
    //  计算设备状态
    private static final long SEC_35 = 1000 * 35;
    private static final long MIN_5 = 1000 * 60 * 5;
    private static final long MIN_30 = 1000 * 60 * 30;
    private static final long HOUR_24 = 1000 * 60 * 60 * 24;

    public static StatusData getStatus(int locateType, long systemTime, long currentTime, long locateTime, long parkTime, int speed) {
        String status;
        int statu;
        if (locateType == 1) {
            if (MIN_30 >= (systemTime - currentTime)) {
                //  在线
                statu = Data.STATUS_ON;
                if (0 != parkTime) {
                    //  静止，时长为 systemTime - parkTime
                    status = "静止(" + TimeFormatU.millisToClock(systemTime - parkTime) + ")";
                } else {
                    if (0 != locateTime) {
                        if (SEC_35 < (systemTime - currentTime)) {
                            //  静止，时长为 systemTime - locateTime
                            status = "静止(" + TimeFormatU.millisToClock(systemTime - locateTime) + ")";
                        } else {
                            if (speed > 0) {
                                //  行驶
                                statu = Data.STATUS_RUNNING;
                                status = "行驶";
                            } else {
                                //  静止，时长为 systemTime - locateTime
                                status = "静止(" + TimeFormatU.millisToClock(systemTime - locateTime) + ")";
                            }
                        }
                    } else {
                        //  静止
                        status = "静止";
                    }
                }
            } else {
                //  离线，时长为 systemTime - currentTime - 30分钟
                status = "离线(" + TimeFormatU.millisToClock(systemTime - currentTime - MIN_30) + ")";
                statu = Data.STATUS_OFF;
            }
        } else {
            if (MIN_5 >= (systemTime - currentTime)) {
                // 在线
                status = "在线";
                statu = Data.STATUS_ON;
            } else if ((systemTime - currentTime) > HOUR_24) {
                //  离线，时长为 systemTime - currentTime
                status = "离线(" + TimeFormatU.millisToClock(systemTime - currentTime) + ")";
                statu = Data.STATUS_OFF;
            } else {
                //  休眠，时长为 systemTime - currentTime - 5分钟
                status = "休眠(" + TimeFormatU.millisToClock(systemTime - currentTime - MIN_5) + ")";
                statu = Data.STATUS_OTHER;
            }
        }
        return new StatusData(status, statu);
    }
}
