package com.tianyigps.online.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tianyigps.online.R;
import com.tianyigps.online.adapter.ConcernAdapter;
import com.tianyigps.online.bean.AttentionBean;
import com.tianyigps.online.data.AdapterConcernData;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.fragment.MonitorFragment;
import com.tianyigps.online.interfaces.OnAttentionListListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookiemouse on 2017/9/13.
 */

public class ConcernDialogFragment extends DialogFragment {

    private static final String TAG = "ConcernDialogFragment";

    private NetManager mNetManager;
    private SharedManager mSharedManager;
    private MyHandler myHandler;

    private int mCid;
    private String mToken;

    private ImageView mImageViewClose;
    private ListView mListView;
    private List<AdapterConcernData> mAdapterConcernDataList;
    private ConcernAdapter mConcernAdapter;

    private View mView;

    //  MonitorFragment
    private MonitorFragment mMonitorFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  获取参数

        mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_concern, null);

        init(mView);

        setEventListener();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //  return super.onCreateView(inflater, container, savedInstanceState);
        //  返回null, 让Fragment来管理Dialog
        return null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //  return super.onCreateDialog(savedInstanceState);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setView(mView);

        AlertDialog dialog = builder.create();

        return dialog;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCancelable(false);
    }

    private void init(View view) {
        mImageViewClose = view.findViewById(R.id.iv_dialog_fragment_concern_close);
        mListView = view.findViewById(R.id.lv_dialog_fragment_concern);

        mAdapterConcernDataList = new ArrayList<>();
        mConcernAdapter = new ConcernAdapter(getContext(), mAdapterConcernDataList);
        mListView.setAdapter(mConcernAdapter);

        mNetManager = new NetManager();
        mSharedManager = new SharedManager(getContext());
        myHandler = new MyHandler();

        mCid = mSharedManager.getCid();
        mToken = mSharedManager.getToken();

        getAttentionList();

        mMonitorFragment = (MonitorFragment) getParentFragment();
    }

    private void setEventListener() {
        mImageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConcernDialogFragment.this.dismiss();
            }
        });

        mConcernAdapter.setOnAdapterListener(new ConcernAdapter.OnAdapterListener() {
            @Override
            public void onConcern(int position) {
                // TODO: 2017/9/13 取消关注
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: 2017/9/13 item点击
            }
        });

        mNetManager.setOnAttentionListListener(new OnAttentionListListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                AttentionBean attentionBean = gson.fromJson(result, AttentionBean.class);
                if (!attentionBean.isSuccess()) {
                    return;
                }
                mAdapterConcernDataList.clear();
                for (AttentionBean.ObjBean objBean : attentionBean.getObj()) {
                    mAdapterConcernDataList.add(new AdapterConcernData(objBean.getName(), objBean.getImei()));
                }
                myHandler.sendEmptyMessage(Data.MSG_1);
            }

            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
            }
        });
    }

    //  获取关注列表
    private void getAttentionList() {
        mNetManager.getAttentionList(mToken, mCid);
    }

    //  取消关注
    private void cancelAttention() {
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_1: {
                    mConcernAdapter.notifyDataSetChanged();
                    break;
                }
                case Data.MSG_2: {
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default-->" + msg.what);
                }
            }
        }
    }
}
