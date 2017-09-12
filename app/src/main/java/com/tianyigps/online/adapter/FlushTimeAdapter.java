package com.tianyigps.online.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.tianyigps.online.R;
import com.tianyigps.online.data.AdapterFlushTimeData;

import java.util.List;

/**
 * Created by cookiemouse on 2017/9/12.
 */

public class FlushTimeAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterFlushTimeData> mDatas;

    private ViewHolder viewHolder = null;

    public FlushTimeAdapter(Context context, List<AdapterFlushTimeData> mDatas) {
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

        final AdapterFlushTimeData data = mDatas.get(i);

        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.item_flush_time, null);
            viewHolder = new ViewHolder();

            viewHolder.time = view.findViewById(R.id.tv_item_flush_time);
            viewHolder.switchOpen = view.findViewById(R.id.switch_item_flush_time);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.time.setText("" + data.getTime());
        viewHolder.switchOpen.setChecked(data.isOpen());

        viewHolder.switchOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (AdapterFlushTimeData dataAll : mDatas) {
                    dataAll.setOpen(false);
                }
                data.setOpen(true);
                notifyDataSetChanged();
            }
        });

        return view;
    }

    private class ViewHolder {
        TextView time;
        Switch switchOpen;
    }
}
