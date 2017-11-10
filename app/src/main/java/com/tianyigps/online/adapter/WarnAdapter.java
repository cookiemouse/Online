package com.tianyigps.online.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.online.R;
import com.tianyigps.online.data.WarnAdapterData;
import com.tianyigps.online.utils.TimeFormatU;
import com.tianyigps.online.utils.WarnTypeU;

import java.util.List;

/**
 * Created by cookiemouse on 2017/9/6.
 */

public class WarnAdapter extends BaseAdapter {
    private static final String TAG = "WarnAdapter";

    private Context context;
    private List<WarnAdapterData> mWarnAdapterDatas;

    private long millsNow = 0, millsToday = 0;
    private long millsDay = 24 * 3600 * 1000;
    private long millsDay2 = 2 * 24 * 3600 * 1000;

    public WarnAdapter(Context context, List<WarnAdapterData> mWarnAdapterDatas) {
        this.context = context;
        this.mWarnAdapterDatas = mWarnAdapterDatas;
        millsNow = System.currentTimeMillis();
        millsToday = TimeFormatU.dateToMillis4(TimeFormatU.getDate());
    }

    @Override
    public void notifyDataSetChanged() {
        millsNow = System.currentTimeMillis();
        millsToday = TimeFormatU.dateToMillis4(TimeFormatU.getDate());
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mWarnAdapterDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mWarnAdapterDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;
        WarnAdapterData data = mWarnAdapterDatas.get(i);
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.item_warn, viewGroup, false);
            viewHolder = new ViewHolder();

            viewHolder.tvName = view.findViewById(R.id.tv_item_warn_name);
            viewHolder.tvType = view.findViewById(R.id.tv_item_warn_type);
            viewHolder.tvDate = view.findViewById(R.id.tv_item_warn_date);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvName.setText(data.getName());
        viewHolder.tvType.setText(WarnTypeU.getType(data.getType()) + "报警");
        long mills = TimeFormatU.dateToMillis2(data.getDate());
        long millsTime = millsNow - mills;

        if (mills >= millsToday) {
            viewHolder.tvDate.setText(TimeFormatU.millisToClock2(millsTime) + "前");
        } else if (mills >= millsToday - millsDay && mills < millsToday) {
            viewHolder.tvDate.setText("昨天" + TimeFormatU.millsToHourMin2(mills));
        } else if (mills >= millsToday - millsDay2 && mills < millsToday - millsDay) {
            viewHolder.tvDate.setText("前天" + TimeFormatU.millsToHourMin2(mills));
        } else {
            viewHolder.tvDate.setText(TimeFormatU.millsToMothDay(mills));
        }

        return view;
    }

    private class ViewHolder {
        TextView tvName, tvType, tvDate;
    }
}
