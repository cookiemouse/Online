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

import com.google.gson.Gson;
import com.tianyigps.grouplistlibrary.GroupAdapter2;
import com.tianyigps.grouplistlibrary.GroupData2;
import com.tianyigps.grouplistlibrary.GroupListView2;
import com.tianyigps.online.R;
import com.tianyigps.online.bean.CompanyBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.fragment.MonitorFragment;
import com.tianyigps.online.fragment.MonitorGaodeFragment;
import com.tianyigps.online.interfaces.OnShowCustomersListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.ToastU;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookiemouse on 2017/9/13.
 */

public class OverviewDialogFragment extends DialogFragment {

    private static final String TAG = "OverviewDialogFragment";

    private NetManager mNetManager;
    private SharedManager mSharedManager;
    private MyHandler myHandler;

    private int mCid, mParentId, mParentGrade;
    private String mToken;

    private ImageView mImageViewClose;
    private ImageView mImageViewSwitch;

    private GroupListView2 mGroupListView;
    private List<GroupData2> mGroupDataList;
    private GroupAdapter2 mGroupAdapter;

    private String mStringMessage;

    private boolean mSwitch = false;

    private View mView;

    //  MonitorFragment
    private MonitorFragment mMonitorFragment;
    private MonitorGaodeFragment mMonitorGaodeFragment;

    private ToastU mToastU;

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

        mGroupListView = view.findViewById(R.id.glv_dialog_fragment_overview);

        mNetManager = new NetManager();
        mSharedManager = new SharedManager(getContext());
        myHandler = new MyHandler();

        mGroupDataList = new ArrayList<>();
        mGroupDataList.add(new GroupData2("" + mCid, "根目录", true));
        GroupData2 groupData = mGroupDataList.get(0);
        groupData.setExhibited(true);
        mGroupAdapter = new GroupAdapter2(getContext(), mGroupDataList);
        mGroupListView.setAdapter(mGroupAdapter);

        mCid = mSharedManager.getCid();
        mToken = mSharedManager.getToken();
        mSwitch = mSharedManager.getShowAttention();
        mImageViewSwitch.setSelected(mSwitch);

        mToastU = new ToastU(getContext());

        int mapType = mSharedManager.getMapType();
        switch (mapType) {
            case Data.MAP_BAIDU: {
                mMonitorFragment = (MonitorFragment) getParentFragment();
                break;
            }
            case Data.MAP_GAODE: {
                mMonitorGaodeFragment = (MonitorGaodeFragment) getParentFragment();
                break;
            }
            default: {
                Log.i(TAG, "init: default-->" + mapType);
            }
        }

        getCompany(mCid);
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
                if (null != mMonitorFragment) {
                    mMonitorFragment.showAttentionDevices();
                }
                if (null != mMonitorGaodeFragment) {
                    mMonitorGaodeFragment.showAttentionDevices();
                }
            }
        });

        mGroupListView.setOnItemClick(new GroupListView2.OnItemClick() {
            @Override
            public void onClick(int position) {
                Log.i(TAG, "onClick: position-->" + position);
                GroupData2 groupData = mGroupDataList.get(position);
                int cid = Integer.valueOf(groupData.getId());
                if (groupData.isLeaf()) {
                    getCompany(cid);
                }
            }

            @Override
            public void onBaseClick(int position) {
                Log.i(TAG, "onBaseClick: position-->" + position);
            }

            @Override
            public void onSwitch(int position) {
                Log.i(TAG, "onSwitch: position-->" + position);
                GroupData2 groupData = mGroupDataList.get(position);
                groupData.setSelected(!groupData.isSelected());
                mGroupListView.notifyDataSetSwitchChanged();
                // TODO: 2017/10/16 选中与取消
                String cidStr = "";
                for (GroupData2 groupData2 : mGroupDataList) {
                    if (groupData2.isSelected()) {
                        cidStr += groupData2.getId() + ",";
                    }
                }
                if (null != mMonitorFragment) {
                    mMonitorFragment.showCompleteDevices(cidStr);
                }
                if (null != mMonitorGaodeFragment) {
                    mMonitorGaodeFragment.showCompleteDevices(cidStr);
                }
            }
        });

        mNetManager.setOnShowCustomersListener(new OnShowCustomersListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                CompanyBean companyBean = gson.fromJson(result, CompanyBean.class);
                if (!companyBean.isSuccess()) {
                    mStringMessage = companyBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                for (CompanyBean.ObjBean objBean : companyBean.getObj()) {
                    mGroupDataList.add(new GroupData2("" + objBean.getId()
                            , "" + mParentId
                            , mParentGrade + 1
                            , objBean.getName()
                            , objBean.isLeaf()));
                }
                myHandler.sendEmptyMessage(Data.MSG_1);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });
    }

    //  获取公司列表
    private void getCompany(int cid) {
//        if (!mLoadingDialogFragment.isResumed()) {
//            mLoadingDialogFragment.show(getChildFragmentManager(), "loading");
//        }
        mParentId = cid;
        for (GroupData2 groupData : mGroupDataList) {
            if (groupData.getId().equals("" + cid)) {
                mParentGrade = groupData.getGrade();
            }
        }
        mNetManager.showCustomers(mToken, cid);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_MSG: {
                    mToastU.showToast(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    //  获取公司列表
                    mGroupListView.notifyDataSetChanged();
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
