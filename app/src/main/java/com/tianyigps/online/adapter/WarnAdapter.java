package com.tianyigps.online.adapter;

import android.content.Context;
import android.util.Log;
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

    private long millsNow = 0;
    private long millsDay = 24 * 3600 * 1000;
    private long millsDay2 = 2 * 24 * 3600 * 1000;
    private long millsDay3 = 3 * 24 * 3600 * 1000;

    public WarnAdapter(Context context, List<WarnAdapterData> mWarnAdapterDatas) {
        this.context = context;
        this.mWarnAdapterDatas = mWarnAdapterDatas;
        millsNow = System.currentTimeMillis();
        Log.i(TAG, "WarnAdapter: millsDay2-->" + millsDay2);
        Log.i(TAG, "WarnAdapter: millsDay3-->" + millsDay3);
    }

    @Override
    public void notifyDataSetChanged() {
        millsNow = System.currentTimeMillis();
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

        int dayNow = TimeFormatU.millsGetDay(millsNow);
        int dayData = TimeFormatU.millsGetDay(mills);

        Log.i(TAG, "getView: millsNow-->" + millsNow);
        Log.i(TAG, "getView: mills-->" + mills);
        Log.i(TAG, "getView: millsTime-->" + millsTime);
        Log.i(TAG, "getView: dayNow-->" + dayNow);
        Log.i(TAG, "getView: dayData-->" + dayData);

        if (0 == (dayNow - dayData)) {
            viewHolder.tvDate.setText(TimeFormatU.millisToClock2(millsTime) + "前");
        } else if ((1 == (dayNow - dayData)) || (0 > (dayNow - dayData) && millsTime < millsDay2)) {
            viewHolder.tvDate.setText("昨天" + TimeFormatU.millsToHourMin2(mills));
        } else if ((2 == (dayNow - dayData)) || (0 > (dayNow - dayData) && millsTime < millsDay3)) {
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
