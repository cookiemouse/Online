package com.tianyigps.online.data;

/**
 * Created by cookiemouse on 2017/9/20.
 */

public class AdapterExpandableChildData {
    private String name, terminalStatus, imei;
    private String margin;
    private boolean isAttention;
    private int attention_id;

    public AdapterExpandableChildData(String name, String terminalStatus, String imei, String margin, boolean isAttention, int attention_id) {
        this.name = name;
        this.terminalStatus = terminalStatus;
        this.imei = imei;
        this.margin = margin;
        this.isAttention = isAttention;
        this.attention_id = attention_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTerminalStatus() {
        return terminalStatus;
    }

    public void setTerminalStatus(String terminalStatus) {
        this.terminalStatus = terminalStatus;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getMargin() {
        return margin;
    }

    public void setMargin(String margin) {
        this.margin = margin;
    }

    public boolean isAttention() {
        return isAttention;
    }

    public void setAttention(boolean attention) {
        isAttention = attention;
    }

    public int getAttention_id() {
        return attention_id;
    }

    public void setAttention_id(int attention_id) {
        this.attention_id = attention_id;
    }
}
