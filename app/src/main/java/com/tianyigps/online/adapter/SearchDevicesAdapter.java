package com.tianyigps.online.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.online.R;
import com.tianyigps.online.data.AdapterSearchDevicesData;

import java.util.List;

/**
 * Created by cookiemouse on 2017/10/10.
 */

public class SearchDevicesAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterSearchDevicesData> mDataList;

    private OnItemListener mOnItemListener;

    public SearchDevicesAdapter(Context context, List<AdapterSearchDevicesData> mDataList) {
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
        final int finalPosition = i;
        AdapterSearchDevicesData childData = mDataList.get(i);
        if (null == view) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(context).inflate(R.layout.item_expandable_child, null);

            viewHolder.tvName = view.findViewById(R.id.tv_item_expandable_child_name);
            viewHolder.tvStatus = view.findViewById(R.id.tv_item_expandable_child_status);
            viewHolder.tvTime = view.findViewById(R.id.tv_item_expandable_child_time);
            viewHolder.ivConcern = view.findViewById(R.id.iv_item_expandable_child_concern);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvName.setText(childData.getName());
        viewHolder.tvStatus.setText(childData.getTerminalStatus());
        viewHolder.tvTime.setText("" + childData.getMargin());
        viewHolder.ivConcern.setSelected(childData.isAttention());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onItemClick(finalPosition);
            }
        });

        viewHolder.ivConcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnItemListener) {
                    throw new NullPointerException("OnItemListener is null");
                }
                mOnItemListener.onConcernClick(finalPosition);
            }
        });
        return view;
    }

    public interface OnItemListener {
        void onConcernClick(int position);

        void onItemClick(int position);
    }

    public void setOnItemListener(OnItemListener listener) {
        this.mOnItemListener = listener;
    }

    private class ViewHolder {
        ImageView ivConcern;
        TextView tvName, tvStatus, tvTime;
    }
}
