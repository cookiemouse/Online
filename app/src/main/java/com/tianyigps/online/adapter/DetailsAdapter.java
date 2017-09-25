package com.tianyigps.online.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tianyigps.online.R;
import com.tianyigps.online.data.AdapterDetailsData;

import java.util.List;

/**
 * Created by cookiemouse on 2017/9/25.
 */

public class DetailsAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterDetailsData> mDataList;

    public DetailsAdapter(Context context, List<AdapterDetailsData> mDataList) {
        this.context = context;
        this.mDataList = mDataList;
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public Object getItem(int i) {
        return mDataList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        AdapterDetailsData data = mDataList.get(i);

        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.item_details, null);
            viewHolder = new ViewHolder();
            viewHolder.tv = view.findViewById(R.id.tv_item_details);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tv.setText(data.getText());

        return view;
    }

    private class ViewHolder {
        TextView tv;
    }
}
