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
import android.widget.ImageView;

import com.tianyigps.online.R;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.fragment.MonitorFragment;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;

/**
 * Created by cookiemouse on 2017/9/13.
 */

public class OverviewDialogFragment extends DialogFragment {

    private static final String TAG = "OverviewDialogFragment";

    private NetManager mNetManager;
    private SharedManager mSharedManager;
    private MyHandler myHandler;

    private int mCid;
    private String mToken;

    private ImageView mImageViewClose;
    private ImageView mImageViewSwitch;

    private boolean mSwitch = false;

    private View mView;

    //  MonitorFragment
    private MonitorFragment mMonitorFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  获取参数

        mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_fragment_overview, null);

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
        mImageViewClose = view.findViewById(R.id.iv_dialog_fragment_overview_close);
        mImageViewSwitch = view.findViewById(R.id.switch_dialog_fragment_overview);

        mNetManager = new NetManager();
        mSharedManager = new SharedManager(getContext());
        myHandler = new MyHandler();

        mCid = mSharedManager.getCid();
        mToken = mSharedManager.getToken();
        mSwitch = mSharedManager.getShowAttention();
        mImageViewSwitch.setSelected(mSwitch);

        mMonitorFragment = (MonitorFragment) getParentFragment();
    }

    private void setEventListener() {
        mImageViewClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OverviewDialogFragment.this.dismiss();
            }
        });

        mImageViewSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSwitch = !mSwitch;
                mImageViewSwitch.setSelected(mSwitch);

                mSharedManager.saveShowAttention(mSwitch);
                mMonitorFragment.showAttentionDevices();
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_1: {
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
