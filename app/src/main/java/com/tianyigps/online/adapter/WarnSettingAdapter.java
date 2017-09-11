package com.tianyigps.online.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.tianyigps.online.R;
import com.tianyigps.online.data.AdapterWarnSettingData;
import com.tianyigps.online.utils.WarnTypeU;

import java.util.List;

/**
 * Created by cookiemouse on 2017/9/11.
 */

public class WarnSettingAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterWarnSettingData> mDatas;

    public WarnSettingAdapter(Context context, List<AdapterWarnSettingData> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        AdapterWarnSettingData data = mDatas.get(i);
        ViewHolder viewHolder = null;
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.item_warn_setting, viewGroup, false);
            viewHolder = new ViewHolder();

            viewHolder.tvTitle = view.findViewById(R.id.tv_item_warn_setting);
            viewHolder.switchOpen = view.findViewById(R.id.switch_item_warn_setting);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        String title = WarnTypeU.getType(data.getType());
        viewHolder.tvTitle.setText(title);
        viewHolder.switchOpen.setChecked(data.isOpen());

        return view;
    }

    private class ViewHolder {
        TextView tvTitle;
        Switch switchOpen;
    }
}
