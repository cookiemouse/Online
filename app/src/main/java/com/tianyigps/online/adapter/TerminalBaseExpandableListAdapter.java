package com.tianyigps.online.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.online.R;
import com.tianyigps.online.data.AdapterExpandableChildData;
import com.tianyigps.online.data.AdapterExpandableGroupData;

import java.util.List;

/**
 * Created by cookiemouse on 2017/9/20.
 */

public class TerminalBaseExpandableListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<AdapterExpandableGroupData> mGroupDataList;

    private OnItemListener mOnItemListener;

    public TerminalBaseExpandableListAdapter(Context context, List<AdapterExpandableGroupData> mGroupDataList) {
        this.context = context;
        this.mGroupDataList = mGroupDataList;
    }

    @Override
    public int getGroupCount() {
        return mGroupDataList.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return mGroupDataList.get(i).getExpandableChildDatalist().size();
    }

    @Override
    public Object getGroup(int i) {
        return mGroupDataList.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return mGroupDataList.get(i).getExpandableChildDatalist().get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        ViewHolderGroup viewHolderGroup = null;
        AdapterExpandableGroupData groupData = mGroupDataList.get(i);
        final int finalPosition = i;
        if (null == view) {
            viewHolderGroup = new ViewHolderGroup();
            view = LayoutInflater.from(context).inflate(R.layout.item_expandable_group, null);

            viewHolderGroup.tvName = view.findViewById(R.id.tv_item_expandable_group_name);
            viewHolderGroup.ivExhibit = view.findViewById(R.id.iv_item_expandable_group_exhibition);

            view.setTag(viewHolderGroup);
        } else {
            viewHolderGroup = (ViewHolderGroup) view.getTag();
        }

        viewHolderGroup.tvName.setText(groupData.getName());
        viewHolderGroup.ivExhibit.setSelected(groupData.isExhibit());

        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        ViewHolderChild viewHolderChild = null;
        AdapterExpandableGroupData groupData = mGroupDataList.get(i);
        final int finalP = i;
        final int finalC = i1;
        int size = groupData.getExpandableChildDatalist().size();
        if (size > i1) {
            AdapterExpandableChildData childData = groupData.getExpandableChildDatalist().get(i1);

            if (null == view) {
                viewHolderChild = new ViewHolderChild();
                view = LayoutInflater.from(context).inflate(R.layout.item_expandable_child, null);

                viewHolderChild.tvName = view.findViewById(R.id.tv_item_expandable_child_name);
                viewHolderChild.tvStatus = view.findViewById(R.id.tv_item_expandable_child_status);
                viewHolderChild.tvTime = view.findViewById(R.id.tv_item_expandable_child_time);
                viewHolderChild.ivConcern = view.findViewById(R.id.iv_item_expandable_child_concern);

                view.setTag(viewHolderChild);
            } else {
                viewHolderChild = (ViewHolderChild) view.getTag();
            }

            viewHolderChild.tvName.setText(childData.getName());
            viewHolderChild.tvStatus.setText(childData.getTerminalStatus());
            viewHolderChild.tvTime.setText("" + childData.getMargin());
            viewHolderChild.ivConcern.setSelected(childData.isAttention());

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == mOnItemListener) {
                        throw new NullPointerException("OnItemListener is null");
                    }
                    mOnItemListener.onChildClick(finalP, finalC);
                }
            });

            viewHolderChild.ivConcern.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (null == mOnItemListener) {
                        throw new NullPointerException("OnItemListener is null");
                    }
                    mOnItemListener.onConcernClick(finalP, finalC);
                }
            });
        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    private class ViewHolderGroup {
        TextView tvName;
        ImageView ivExhibit;
    }

    private class ViewHolderChild {
        ImageView ivConcern;
        TextView tvName, tvStatus, tvTime;
    }

    public interface OnItemListener {
        void onConcernClick(int parentPosition, int childPosition);

        void onChildClick(int parentPosition, int childPosition);
    }

    public void setOnGroupClick(OnItemListener listener) {
        this.mOnItemListener = listener;
    }
}
