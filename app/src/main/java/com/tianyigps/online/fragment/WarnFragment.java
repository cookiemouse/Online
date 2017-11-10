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
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.online.R;
import com.tianyigps.online.activity.FragmentContentActivity;
import com.tianyigps.online.activity.WarnActivity;
import com.tianyigps.online.adapter.WarnAdapter;
import com.tianyigps.online.bean.WarnListBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.data.WarnAdapterData;
import com.tianyigps.online.interfaces.OnGetWarnListListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.ToastU;

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

    //  Listview滑动过半时，是否可请求数据
    private boolean mIsRequestAble = true;
    //  是否是加载更多
    private boolean mIsAddMore = false;

    private SharedManager mSharedManager;
    private String mToken, mCondition = "", mType;
    private int mCid, mLastId;
    private NetManager mNetManager;
    private MyHandler myHandler;

    private String mStringMessage;

    private ToastU mToastU;

    //  设定Fragment
    private WarnSettingFragment mWarnSettingFragment;
    private FragmentManager mFragmentManager;

    private FragmentContentActivity mFragmentContentActivity;

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
            mIsAddMore = false;
            getWarnInfo(mType, "", mCondition);
        }
    }

    private void init(View view) {
        mEditTextSearch = view.findViewById(R.id.et_fragment_warn_search);
        mTextViewSearch = view.findViewById(R.id.tv_fragment_warn_search);
        mTextViewSetup = view.findViewById(R.id.tv_fragment_warn_setup);
        mSwipeRefreshLayout = view.findViewById(R.id.srl_fragment_warn);

        mFragmentContentActivity = (FragmentContentActivity) getActivity();

        mFragmentManager = getChildFragmentManager();
        mWarnSettingFragment = new WarnSettingFragment();

        mListView = view.findViewById(R.id.lv_fragment_warn);

        mSwipeRefreshLayout.setColorSchemeColors(0xff3cabfa);

        mWarnAdapterDataList = new ArrayList<>();
        mWarnAdapter = new WarnAdapter(getContext(), mWarnAdapterDataList);

        mListView.setAdapter(mWarnAdapter);

        mToastU = new ToastU(getContext());

        mSharedManager = new SharedManager(getContext());
        mToken = mSharedManager.getToken();
        mCid = mSharedManager.getCid();
        mType = mSharedManager.getWarnType();

        mNetManager = new NetManager();

        myHandler = new MyHandler();

        mIsAddMore = false;
        getWarnInfo(mType, "", mCondition);
    }

    private void setEventListener() {
        mTextViewSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: visible-->" + mWarnSettingFragment.isVisible());
                if (mWarnSettingFragment.isVisible()) {
                    hideWarnSettingFragment();
                } else {
                    showWarnSettingFragment();
                }
            }
        });

        mTextViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: mTextViewSearch");
                hideKeyboard();
                mCondition = mEditTextSearch.getText().toString();
                mCondition = mCondition.trim();
                mIsAddMore = false;
                getWarnInfo(mType, "", mCondition);
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsAddMore = false;
                getWarnInfo(mType, "", mCondition);
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                WarnAdapterData data = mWarnAdapterDataList.get(i);
                Intent intent = new Intent(getActivity(), WarnActivity.class);
                intent.putExtra(Data.INTENT_NAME, data.getName());
                intent.putExtra(Data.INTENT_WARN_TYPE, data.getType());
                intent.putExtra(Data.INTENT_DATE, data.getDate());
                intent.putExtra(Data.INTENT_LATITUDE, data.getLatitude());
                intent.putExtra(Data.INTENT_LONGITUDE, data.getLongitude());
                startActivity(intent);
            }
        });

        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {
            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (mIsRequestAble && firstVisibleItem >= totalItemCount - visibleItemCount) {
                    Log.i(TAG, "onScroll: request");
                    mIsRequestAble = false;
                    mIsAddMore = true;
                    if (totalItemCount > 1) {
                        String lastid = mWarnAdapterDataList.get(totalItemCount - 1).getId();
                        getWarnInfo(mType, lastid, mCondition);
                    }
                }
            }
        });

        mWarnSettingFragment.setOnDismissListener(new WarnSettingFragment.OnDismissListener() {
            @Override
            public void onDismiss(String type) {
                mType = type;
                mIsAddMore = false;
                getWarnInfo(mType, "", mCondition);
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
                if (!mIsAddMore) {
                    mWarnAdapterDataList.clear();
                }
                mIsRequestAble = true;
                for (WarnListBean.ObjBean objBean : warnListBean.getObj()) {
                    mWarnAdapterDataList.add(new WarnAdapterData(objBean.getName()
                            , objBean.getWarn_type()
                            , objBean.getReceive_time()
                            , objBean.getImei()
                            , objBean.getLatitude()
                            , objBean.getLongitude()
                            , objBean.getId()));
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
        if (null == getActivity()) {
            return;
        }
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

    //  显示Toast
    private void showToast(String msg) {
        mToastU.showToast(msg);
    }

    //  关闭软键盘
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    //  显示设定Fragmnet
    private void showWarnSettingFragment() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        for (Fragment fragment : mFragmentManager.getFragments()) {
            fragmentTransaction.hide(fragment);
        }
        if (mWarnSettingFragment.isAdded()) {
            fragmentTransaction.show(mWarnSettingFragment);
        } else {
            fragmentTransaction.add(R.id.fl_fragment_warn_content, mWarnSettingFragment);
        }
        fragmentTransaction.commit();
        mFragmentContentActivity.showBottomView();
    }

    //  隐藏设定Fragment
    public void hideWarnSettingFragment() {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        fragmentTransaction.hide(mWarnSettingFragment);
        fragmentTransaction.commit();
        mFragmentContentActivity.hideBottomView();
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
                    showToast(mStringMessage);
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
