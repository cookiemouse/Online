package com.tianyigps.online.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.tianyigps.online.R;
import com.tianyigps.online.data.Account;

import java.util.List;

/**
 * Created by cookiemouse on 2017/9/5.
 */

public class PopupAccountAdapter extends BaseAdapter {

    private Context context;
    private List<Account> mAccounts;

    private OnDeleteListener mOnDeleteListener;

    public PopupAccountAdapter(Context context, List<Account> mAccounts) {
        this.context = context;
        this.mAccounts = mAccounts;
    }

    @Override
    public int getCount() {
        return mAccounts.size();
    }

    @Override
    public Object getItem(int position) {
        return mAccounts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View contentView, ViewGroup viewGroup) {

        ViewHolder viewHolder = null;

        final int positionFinal = position;

        Account account = mAccounts.get(position);
        if (null == contentView) {
            contentView = LayoutInflater.from(context).inflate(R.layout.item_popup_account, null);
            viewHolder = new ViewHolder();

            viewHolder.tvAccount = contentView.findViewById(R.id.tv_item_popup_account);
            viewHolder.ivDelete = contentView.findViewById(R.id.iv_item_popup_account);

            contentView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) contentView.getTag();
        }

        viewHolder.tvAccount.setText(account.getmAccount());

        viewHolder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnDeleteListener) {
                    throw new NullPointerException("OnDeleteListener is null");
                }
                mOnDeleteListener.onDelete(positionFinal);
            }
        });

        return contentView;
    }

    private class ViewHolder {
        TextView tvAccount;
        ImageView ivDelete;
    }

    public interface OnDeleteListener {
        void onDelete(int position);
    }

    public void setOnDeleteListener(OnDeleteListener listener) {
        this.mOnDeleteListener = listener;
    }
}
