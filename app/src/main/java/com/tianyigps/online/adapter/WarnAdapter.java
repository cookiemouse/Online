package com.tianyigps.online.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.online.R;
import com.tianyigps.online.data.WarnAdapterData;

import java.util.List;

/**
 * Created by cookiemouse on 2017/9/6.
 */

public class WarnAdapter extends BaseAdapter {

    private Context context;
    private List<WarnAdapterData> mWarnAdapterDatas;

    public WarnAdapter(Context context, List<WarnAdapterData> mWarnAdapterDatas) {
        this.context = context;
        this.mWarnAdapterDatas = mWarnAdapterDatas;
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
        viewHolder.tvType.setText(data.getType());
        viewHolder.tvDate.setText(data.getDate());

        return view;
    }

    private class ViewHolder {
        TextView tvName, tvType, tvDate;
    }
}
