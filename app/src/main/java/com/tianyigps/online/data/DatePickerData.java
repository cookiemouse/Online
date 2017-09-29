package com.tianyigps.online.data;

/**
 * Created by cookiemouse on 2017/9/29.
 */

public class DatePickerData {
    private String start, end;

    public DatePickerData(String start, String end) {
        this.start = start;
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
