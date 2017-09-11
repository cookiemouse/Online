package com.tianyigps.online.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.online.R;
import com.tianyigps.online.activity.WarnActivity;
import com.tianyigps.online.adapter.WarnAdapter;
import com.tianyigps.online.bean.WarnListBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.data.WarnAdapterData;
import com.tianyigps.online.interfaces.OnGetWarnListListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookiemouse on 2017/9/5.
 */

public class WarnFragment extends Fragment {

    private final static String TAG = "WarnFragment";

    private EditText mEditTextSearch;
    private TextView mTextViewSearch, mTextViewSetup;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;
    private List<WarnAdapterData> mWarnAdapterDataList;
    private WarnAdapter mWarnAdapter;

    private SharedManager mSharedManager;
    private String mToken, mCondition = "";
    private int mCid, mLastId;
    private NetManager mNetManager;
    private MyHandler myHandler;

    private String mStringMessage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warn, container, false);

        init(view);

        setEventListener();

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            getWarnInfo("01", "", mCondition);
        }
    }

    private void init(View view) {
        mEditTextSearch = view.findViewById(R.id.et_fragment_warn_search);
        mTextViewSearch = view.findViewById(R.id.tv_fragment_warn_search);
        mTextViewSetup = view.findViewById(R.id.tv_fragment_warn_setup);
        mSwipeRefreshLayout = view.findViewById(R.id.srl_fragment_warn);
        mListView = view.findViewById(R.id.lv_fragment_warn);

        mSwipeRefreshLayout.setColorSchemeColors(0xff3cabfa);

        mWarnAdapterDataList = new ArrayList<>();
        mWarnAdapter = new WarnAdapter(getContext(), mWarnAdapterDataList);

        mListView.setAdapter(mWarnAdapter);

        mSharedManager = new SharedManager(getContext());
        mToken = mSharedManager.getToken();
        mCid = mSharedManager.getCid();

        mNetManager = new NetManager();

        myHandler = new MyHandler();

        getWarnInfo("01", "", mCondition);
    }

    private void setEventListener() {
        mTextViewSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        mTextViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: mTextViewSearch");
                hideKeyboard();
                mCondition = mEditTextSearch.getText().toString();
                mCondition = mCondition.trim();
                getWarnInfo("01", "", mCondition);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWarnInfo("01", "", mCondition);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), WarnActivity.class);
                startActivity(intent);
            }
        });

        mNetManager.setOnGetWarnListListener(new OnGetWarnListListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                WarnListBean warnListBean = gson.fromJson(result, WarnListBean.class);
                if (!warnListBean.isSuccess()) {
                    mStringMessage = warnListBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                mWarnAdapterDataList.clear();
                for (WarnListBean.ObjBean objBean : warnListBean.getObj()) {
                    mWarnAdapterDataList.add(new WarnAdapterData(objBean.getName()
                            , objBean.getWarn_type()
                            , objBean.getReceive_time()
                            , objBean.getImei()));
                }
                myHandler.sendEmptyMessage(Data.MSG_1);
            }

            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });
    }

    //  获取报警信息
    private void getWarnInfo(String type, String lastId, String condition) {
        mSwipeRefreshLayout.setRefreshing(true);
        mNetManager.getWarnList(mToken, mCid, type, lastId, condition);
    }

    //  显示对话框
    private void showMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(mStringMessage);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  do nothing
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    //  关闭软键盘
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
            switch (msg.what) {
                case Data.MSG_ERO: {
                    break;
                }
                case Data.MSG_MSG: {
                    showMessageDialog();
                    break;
                }
                case Data.MSG_NOTHING: {
                    break;
                }
                case Data.MSG_1: {
                    mWarnAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }
}
