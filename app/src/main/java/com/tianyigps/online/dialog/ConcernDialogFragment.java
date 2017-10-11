package com.tianyigps.online.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.tianyigps.online.bean.AddAttentionBean;
import com.tianyigps.online.bean.AttentionBean;
import com.tianyigps.online.bean.DelAttentionBean;
import com.tianyigps.online.data.AdapterConcernData;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.fragment.MonitorFragment;
import com.tianyigps.online.interfaces.OnAddAttentionListener;
import com.tianyigps.online.interfaces.OnAttentionListListener;
import com.tianyigps.online.interfaces.OnDelAttentionListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.ToastU;

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
    private int mPosition;

    private View mView;

    //  MonitorFragment
    private MonitorFragment mMonitorFragment;

    private String mStringMessage;
    private ToastU mToastU;

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

        mToastU = new ToastU(getContext());

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
                // 2017/9/13 取消关注
                mPosition = position;
                AdapterConcernData data = mAdapterConcernDataList.get(position);
                attention(data.isOpen(), data.getImei());
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO: 2017/9/13 item点击
                mPosition = i;
                AdapterConcernData data = mAdapterConcernDataList.get(i);
                mMonitorFragment.showDevices(data.getImei());
                ConcernDialogFragment.this.dismiss();
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

        mNetManager.setOnAddAttentionListener(new OnAddAttentionListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                AddAttentionBean addAttentionBean = gson.fromJson(result, AddAttentionBean.class);
                mStringMessage = addAttentionBean.getMsg();
                if (!addAttentionBean.isSuccess()) {
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                myHandler.sendEmptyMessage(Data.MSG_2);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });

        mNetManager.setOnDelAttentionListener(new OnDelAttentionListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                DelAttentionBean delAttentionBean = gson.fromJson(result, DelAttentionBean.class);
                mStringMessage = delAttentionBean.getMsg();
                if (!delAttentionBean.isSuccess()) {
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                myHandler.sendEmptyMessage(Data.MSG_3);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });
    }

    //  获取关注列表
    private void getAttentionList() {
        mNetManager.getAttentionList(mToken, mCid);
    }

    //  添加、取消关注
    private void attention(boolean isAttend, String imei) {
        if (isAttend) {
            showDelAttentionDialog(imei);
            return;
        }
        mNetManager.addAttention(mToken, mCid, imei);
    }

    //  取消关注确认对话框
    private void showDelAttentionDialog(final String imei) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("取消关注后，将无法收到该车报警推送。");
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mNetManager.delAttention(mToken, mCid, imei);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // do nothing
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_1: {
                    //  获取关注列表
                    mConcernAdapter.notifyDataSetChanged();
                    break;
                }
                case Data.MSG_2: {
                    //  添加关注
                    AdapterConcernData data = mAdapterConcernDataList.get(mPosition);
                    data.setOpen(true);
                    mConcernAdapter.notifyDataSetChanged();
                    mToastU.showToast(mStringMessage);
                    break;
                }
                case Data.MSG_3: {
//                    AdapterConcernData data = mAdapterConcernDataList.get(mPosition);
//                    data.setOpen(false);
                    mAdapterConcernDataList.remove(mPosition);
                    mConcernAdapter.notifyDataSetChanged();
                    mToastU.showToast(mStringMessage);
                    //  取消关注
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: default-->" + msg.what);
                }
            }
        }
    }
}
