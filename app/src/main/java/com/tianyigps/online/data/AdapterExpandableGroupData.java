package com.tianyigps.online.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookiemouse on 2017/9/20.
 */

public class AdapterExpandableGroupData {
    private String name;
    private int id;
    private boolean isExhibit;
    private List<AdapterExpandableChildData> mExpandableChildDatalist;

    public AdapterExpandableGroupData(String name, int id) {
        this.name = name;
        this.id = id;
        isExhibit = false;
        mExpandableChildDatalist = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isExhibit() {
        return isExhibit;
    }

    public void setExhibit(boolean exhibit) {
        isExhibit = exhibit;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<AdapterExpandableChildData> getExpandableChildDatalist() {
        return mExpandableChildDatalist;
    }

    public void setExpandableChildDatalist(List<AdapterExpandableChildData> mExpandableChildDatalist) {
        this.mExpandableChildDatalist = mExpandableChildDatalist;
    }
}
