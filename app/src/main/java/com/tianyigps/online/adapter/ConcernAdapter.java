package com.tianyigps.online.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.online.R;
import com.tianyigps.online.data.AdapterConcernData;

import java.util.List;

/**
 * Created by cookiemouse on 2017/9/13.
 */

public class ConcernAdapter extends BaseAdapter {

    private Context context;
    private List<AdapterConcernData> mDatas;

    public ConcernAdapter(Context context, List<AdapterConcernData> mDatas) {
        this.context = context;
        this.mDatas = mDatas;
    }

    private OnAdapterListener mOnAdapterListener;

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

        final AdapterConcernData data = mDatas.get(i);
        final int position = i;
        ViewHolder viewHolder = null;
        if (null == view) {
            view = LayoutInflater.from(context).inflate(R.layout.item_concern, null);

            viewHolder = new ViewHolder();
            viewHolder.tvName = view.findViewById(R.id.tv_item_concern);
            viewHolder.ivConcern = view.findViewById(R.id.iv_item_concern);

            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        viewHolder.tvName.setText(data.getName());
        viewHolder.ivConcern.setSelected(data.isOpen());
        viewHolder.ivConcern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnAdapterListener) {
                    throw new NullPointerException("OnAdapterListener is null");
                }
                mOnAdapterListener.onConcern(position);
            }
        });

        return view;
    }

    private class ViewHolder {
        TextView tvName;
        ImageView ivConcern;
    }

    public interface OnAdapterListener {
        void onConcern(int position);
    }

    public void setOnAdapterListener(OnAdapterListener listener) {
        this.mOnAdapterListener = listener;
    }
}
