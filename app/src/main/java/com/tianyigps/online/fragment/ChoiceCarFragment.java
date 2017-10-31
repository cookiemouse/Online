package com.tianyigps.online.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.tianyigps.grouplistlibrary.GroupAdapter;
import com.tianyigps.grouplistlibrary.GroupData;
import com.tianyigps.grouplistlibrary.GroupListView;
import com.tianyigps.online.R;
import com.tianyigps.online.activity.FragmentContentActivity;
import com.tianyigps.online.adapter.SearchDevicesAdapter;
import com.tianyigps.online.adapter.TerminalBaseExpandableListAdapter;
import com.tianyigps.online.bean.AddAttentionBean;
import com.tianyigps.online.bean.CompanyBean;
import com.tianyigps.online.bean.DelAttentionBean;
import com.tianyigps.online.bean.GroupBean;
import com.tianyigps.online.bean.NumberBean;
import com.tianyigps.online.bean.SearchDevicesBean;
import com.tianyigps.online.bean.TerminalListBean;
import com.tianyigps.online.data.AdapterExpandableChildData;
import com.tianyigps.online.data.AdapterExpandableGroupData;
import com.tianyigps.online.data.AdapterSearchDevicesData;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.dialog.LoadingDialogFragment;
import com.tianyigps.online.interfaces.OnAddAttentionListener;
import com.tianyigps.online.interfaces.OnDelAttentionListener;
import com.tianyigps.online.interfaces.OnGetGroupListener;
import com.tianyigps.online.interfaces.OnGetNumWithStatusListener;
import com.tianyigps.online.interfaces.OnGetTerminalByGroupListener;
import com.tianyigps.online.interfaces.OnSearchTerminalWithStatusListener;
import com.tianyigps.online.interfaces.OnShowCustomersListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.TimeFormatU;
import com.tianyigps.online.utils.ToastU;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookiemouse on 2017/9/5.
 */

public class ChoiceCarFragment extends Fragment {

    private static final String TAG = "ChoiceCarFragment";

    private static final int DELEY_1200 = 1200;

    //  top
    private RelativeLayout mRelativeLayoutTop;
    private GroupListView mListView;
    private List<GroupData> mGroupDataList;
    private GroupAdapter mGroupAdapter;

    //  middle
    private ImageView mImageViewDivision;

    //  bottom
    private EditText mEditTextSearch;
    private TextView mTextViewAll, mTextViewOnline, mTextViewOffline;
    private View mViewAll, mViewOnline, mViewOffline;
    private ExpandableListView mExpandableListView;
    private LinearLayout mLinearLayoutExpand;
    private ListView mListViewSearch;
    private int mOffline, mOnline;
    private int mCidSelected, mGroupSelected, mStatuSelected = 0;
    private List<AdapterExpandableGroupData> mAdapterExpandableGroupDataList;
    private List<AdapterSearchDevicesData> mAdapterSearchDevicesDataList;
    private TerminalBaseExpandableListAdapter mTerminalBaseExpandableListAdapter;
    private SearchDevicesAdapter mSearchDevicesAdapter;
    private boolean isFirstExpand = true;
    private int mParentPosition, mChildPosition;
    private int mSearchPosition;
    private String mSearchKey;

    //  data
    private NetManager mNetManager;
    private SharedManager mSharedManager;
    private int mCid, mParentId, mParentGrade;
    private String mToken, mName;
    private String mStringMessage;

    private MyHandler myHandler;

    //  Toast
    private ToastU mToastU;

    //  刷新时间
    private int mFlushTime = 10000;

    //  Loading
    private LoadingDialogFragment mLoadingDialogFragment;

    //  ContentActivity
    private FragmentContentActivity mFragmentContentActivity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_choice_car, container, false);

        init(view);

        setEventListener();

        return view;
    }

    @Override
    public void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
        //  开始刷新
        if (mCidSelected != 0 && mGroupSelected != 0) {
            myHandler.removeMessages(Data.MSG_9);
            myHandler.sendEmptyMessageDelayed(Data.MSG_9, mFlushTime);
        }
    }

    @Override
    public void onPause() {
        Log.i(TAG, "onPause: ");
        myHandler.removeMessages(Data.MSG_9);
        super.onPause();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.i(TAG, "onHiddenChanged: hidden-->" + hidden);
        super.onHiddenChanged(hidden);
        if (hidden) {
            myHandler.removeMessages(Data.MSG_9);
        } else {
            //  开始刷新
            mFlushTime = mSharedManager.getFlushTime() * 1000;
            Log.i(TAG, "onHiddenChanged: mFlushTime-->" + mFlushTime);
            if (mCidSelected != 0 && mGroupSelected != 0) {
                myHandler.removeMessages(Data.MSG_9);
                myHandler.sendEmptyMessageDelayed(Data.MSG_9, mFlushTime);
            }
        }
    }

    @Override
    public void onStop() {
        myHandler.removeMessages(Data.MSG_7);
        super.onStop();
    }

    private void init(View view) {
        mRelativeLayoutTop = view.findViewById(R.id.rl_fragment_choice_car_accounts);
        mListView = view.findViewById(R.id.lv_fragment_choice_car);

        mImageViewDivision = view.findViewById(R.id.iv_fragment_choice_car_division);

        mEditTextSearch = view.findViewById(R.id.et_fragment_choice_car_search);
        mTextViewAll = view.findViewById(R.id.tv_fragment_choice_car_all);
        mTextViewOnline = view.findViewById(R.id.tv_fragment_choice_car_online);
        mTextViewOffline = view.findViewById(R.id.tv_fragment_choice_car_offline);
        mViewAll = view.findViewById(R.id.view_fragment_choice_car_all);
        mViewOnline = view.findViewById(R.id.view_fragment_choice_car_online);
        mViewOffline = view.findViewById(R.id.view_fragment_choice_car_offline);

        mExpandableListView = view.findViewById(R.id.elv_fragment_choice_car);
        mLinearLayoutExpand = view.findViewById(R.id.ll_fragment_choice_car_expand);
        mListViewSearch = view.findViewById(R.id.lv_fragment_choice_car_search);

        mAdapterExpandableGroupDataList = new ArrayList<>();
        mAdapterSearchDevicesDataList = new ArrayList<>();
        mTerminalBaseExpandableListAdapter = new TerminalBaseExpandableListAdapter(getContext(), mAdapterExpandableGroupDataList);
        mSearchDevicesAdapter = new SearchDevicesAdapter(getContext(), mAdapterSearchDevicesDataList);
        mExpandableListView.setAdapter(mTerminalBaseExpandableListAdapter);
        mListViewSearch.setAdapter(mSearchDevicesAdapter);

        mToastU = new ToastU(getContext());

        mFragmentContentActivity = (FragmentContentActivity) getActivity();

        mLoadingDialogFragment = new LoadingDialogFragment();

        mNetManager = new NetManager();
        mSharedManager = new SharedManager(getContext());
        mCid = mSharedManager.getCid();
        mCidSelected = mCid;
        mToken = mSharedManager.getToken();
        mName = mSharedManager.getName();

        mFlushTime = mSharedManager.getFlushTime() * 1000;

        myHandler = new MyHandler();

        mGroupDataList = new ArrayList<>();
        mGroupDataList.add(new GroupData("" + mCid, mName, true));
        GroupData groupData = mGroupDataList.get(0);
        groupData.setExhibited(true);
        groupData.setSelected(true);
        mGroupAdapter = new GroupAdapter(getContext(), mGroupDataList);
        mListView.setAdapter(mGroupAdapter);

        getCompany(mCid);
        getGroup(mCid);
        getNumber(mCid);
    }

    private void setEventListener() {
        mImageViewDivision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEditTextSearch.clearFocus();
                if (mRelativeLayoutTop.getVisibility() == View.VISIBLE) {
                    mRelativeLayoutTop.setVisibility(View.GONE);
                    mImageViewDivision.setSelected(true);
                } else {
                    mRelativeLayoutTop.setVisibility(View.VISIBLE);
                    mImageViewDivision.setSelected(false);
                }
            }
        });

        mEditTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                myHandler.removeMessages(Data.MSG_7);
                mSearchKey = editable.toString();
                myHandler.sendEmptyMessageDelayed(Data.MSG_7, DELEY_1200);
            }
        });

        mTextViewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/9/18 All
                setDefault();
                mStatuSelected = 0;
                mTextViewAll.setTextColor(getResources().getColor(R.color.colorBlue));
                mViewAll.setVisibility(View.VISIBLE);
                isFirstExpand = true;
                getDevices();
            }
        });

        mTextViewOnline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/9/18 online
                setDefault();
                mStatuSelected = 1;
                mTextViewOnline.setTextColor(getResources().getColor(R.color.colorBlue));
                mViewOnline.setVisibility(View.VISIBLE);
                isFirstExpand = true;
                getDevices();
            }
        });

        mTextViewOffline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/9/18 offline
                setDefault();
                mStatuSelected = 2;
                mTextViewOffline.setTextColor(getResources().getColor(R.color.colorBlue));
                mViewOffline.setVisibility(View.VISIBLE);
                isFirstExpand = true;
                getDevices();
            }
        });

        mListView.setOnItemClick(new GroupListView.OnItemClick() {
            @Override
            public void onClick(int position) {
                GroupData groupData = mGroupDataList.get(position);
                int cid = Integer.valueOf(groupData.getId());
                mCidSelected = cid;
                if (groupData.isLeaf()) {
                    getCompany(cid);
                }
                getGroup(cid);
                getNumber(cid);
                isFirstExpand = true;
            }

            @Override
            public void onBaseClick(int i) {
                mEditTextSearch.clearFocus();
                if (mLinearLayoutExpand.getVisibility() == View.GONE) {
                    mLinearLayoutExpand.setVisibility(View.VISIBLE);
                    mListViewSearch.setVisibility(View.GONE);
                    mEditTextSearch.setText(null);
                }
            }
        });

        mSearchDevicesAdapter.setOnItemListener(new SearchDevicesAdapter.OnItemListener() {
            @Override
            public void onConcernClick(int position) {
                // 2017/10/10 关注
                AdapterSearchDevicesData data = mAdapterSearchDevicesDataList.get(position);
                attention(data.isAttention(), data.getImei());
            }

            @Override
            public void onItemClick(int position) {
                // 2017/10/10 item
                AdapterSearchDevicesData data = mAdapterSearchDevicesDataList.get(position);
                Bundle bundle = new Bundle();
                bundle.putString(Data.KEY_IMEI, data.getImei());
                mFragmentContentActivity.showMonitor(bundle);
            }
        });

        mTerminalBaseExpandableListAdapter.setOnGroupClick(new TerminalBaseExpandableListAdapter.OnItemListener() {
            @Override
            public void onConcernClick(int parentPosition, int childPosition) {
                mParentPosition = parentPosition;
                mChildPosition = childPosition;
                AdapterExpandableChildData childData = mAdapterExpandableGroupDataList.get(parentPosition)
                        .getExpandableChildDatalist().get(childPosition);
                attention(childData.isAttention(), childData.getImei());
            }

            @Override
            public void onChildClick(int parentPosition, int childPosition) {
                mParentPosition = parentPosition;
                mChildPosition = childPosition;
                AdapterExpandableChildData childData = mAdapterExpandableGroupDataList.get(parentPosition)
                        .getExpandableChildDatalist().get(childPosition);
                Bundle bundle = new Bundle();
                bundle.putString(Data.KEY_IMEI, childData.getImei());
                mFragmentContentActivity.showMonitor(bundle);
            }
        });

        mExpandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                AdapterExpandableGroupData groupData = mAdapterExpandableGroupDataList.get(i);
                mGroupSelected = groupData.getId();
                return false;
            }
        });

        mExpandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int i) {
                mAdapterExpandableGroupDataList.get(i).setExhibit(false);
            }
        });

        mExpandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                AdapterExpandableGroupData groupData = mAdapterExpandableGroupDataList.get(i);
                groupData.setExhibit(true);
                if (groupData.getExpandableChildDatalist().size() == 0) {
                    getDevices();
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
                    mGroupDataList.add(new GroupData("" + objBean.getId()
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

        mNetManager.setOnGetGroupListener(new OnGetGroupListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                GroupBean groupBean = gson.fromJson(result, GroupBean.class);
                if (!groupBean.isSuccess()) {
                    mStringMessage = groupBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                mAdapterExpandableGroupDataList.clear();
                mGroupSelected = groupBean.getObj().get(0).getId();
                for (GroupBean.ObjBean objBean : groupBean.getObj()) {
                    mAdapterExpandableGroupDataList.add(new AdapterExpandableGroupData(objBean.getName(), objBean.getId()));
                }
                myHandler.sendEmptyMessage(Data.MSG_2);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });

        mNetManager.setOnGetNumWithStatusListener(new OnGetNumWithStatusListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                NumberBean numberBean = gson.fromJson(result, NumberBean.class);
                if (!numberBean.isSuccess()) {
                    mStringMessage = numberBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                NumberBean.ObjBean objBean = numberBean.getObj();
                mOffline = objBean.getOffLine();
                mOnline = objBean.getOnline();
                myHandler.sendEmptyMessage(Data.MSG_3);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });

        mNetManager.setOnGetTerminalByGroupListener(new OnGetTerminalByGroupListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                TerminalListBean terminalListBean = gson.fromJson(result, TerminalListBean.class);
                if (!terminalListBean.isSuccess()) {
                    mStringMessage = terminalListBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                List<AdapterExpandableChildData> mAdapterExpandableChildDataList = new ArrayList<>();
                for (TerminalListBean.ObjBean objBean : terminalListBean.getObj()) {
                    mAdapterExpandableChildDataList.add(new AdapterExpandableChildData(objBean.getName()
                            , objBean.getTerminalStatus()
                            , objBean.getImei()
                            , TimeFormatU.millisToClock2(objBean.getMargin())
                            , objBean.isIsAttention()
                            , objBean.getAttention_id()));
                }
                for (AdapterExpandableGroupData data : mAdapterExpandableGroupDataList) {
                    if (data.getId() == mGroupSelected) {
                        data.setExpandableChildDatalist(mAdapterExpandableChildDataList);
                    }
                }
                myHandler.sendEmptyMessage(Data.MSG_4);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
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
                myHandler.sendEmptyMessage(Data.MSG_5);
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
                myHandler.sendEmptyMessage(Data.MSG_6);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });

        mNetManager.setOnSearchTerminalWithStatusListener(new OnSearchTerminalWithStatusListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                SearchDevicesBean searchDevicesBean = gson.fromJson(result, SearchDevicesBean.class);
                mStringMessage = searchDevicesBean.getMsg();
                if (!searchDevicesBean.isSuccess()) {
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                mAdapterSearchDevicesDataList.clear();
                for (SearchDevicesBean.ObjBean objBean : searchDevicesBean.getObj()) {
                    mAdapterSearchDevicesDataList.add(new AdapterSearchDevicesData(objBean.getName()
                            , objBean.getTerminalStatus()
                            , objBean.getImei()
                            , TimeFormatU.millisToClock2(objBean.getMargin())
                            , objBean.isIsAttention()
                            , objBean.getAttention_id()));
                }
                myHandler.sendEmptyMessage(Data.MSG_8);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });
    }

    //  all、online、offline置零
    private void setDefault() {
        mEditTextSearch.clearFocus();
        mViewAll.setVisibility(View.INVISIBLE);
        mTextViewAll.setTextColor(getResources().getColor(R.color.colorBlackText));
        mViewOnline.setVisibility(View.INVISIBLE);
        mTextViewOnline.setTextColor(getResources().getColor(R.color.colorBlackText));
        mViewOffline.setVisibility(View.INVISIBLE);
        mTextViewOffline.setTextColor(getResources().getColor(R.color.colorBlackText));
    }

    //  获取公司列表
    private void getCompany(int cid) {
        if (!mLoadingDialogFragment.isResumed()) {
            mLoadingDialogFragment.show(getChildFragmentManager(), "loading");
        }
        mParentId = cid;
        for (GroupData groupData : mGroupDataList) {
            if (groupData.getId().equals("" + cid)) {
                mParentGrade = groupData.getGrade();
            }
        }
        mNetManager.showCustomers(mToken, cid);
    }

    //  获取Group
    private void getGroup(int cid) {
        mNetManager.getGroup(mToken, mCid, cid);
    }

    //  获取在线，离线数量
    private void getNumber(int cid) {
        mNetManager.getNumWithStatus(mToken, mCid, cid);
    }

    //  获取各状态下的设备
    private void getDevices() {
        mNetManager.getTerminalByGroup(mToken, mCid, mCidSelected, mGroupSelected, mStatuSelected);
    }

    //  添加、取消关注
    private void attention(boolean isAttend, String imei) {
        if (isAttend) {
            showDelAttentionDialog(imei);
            return;
        }
        mNetManager.addAttention(mToken, mCid, imei);
    }

    //  搜索
    private void searchDevices(String key) {
        mNetManager.searchTerminalWithStatus(mToken, mCidSelected, key);
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
            if (mLoadingDialogFragment.isAdded()) {
                mLoadingDialogFragment.dismissAllowingStateLoss();
            }
            switch (msg.what) {
                case Data.MSG_ERO: {
                    break;
                }
                case Data.MSG_MSG: {
                    mToastU.showToast(mStringMessage);
                    break;
                }
                case Data.MSG_NOTHING: {
                    break;
                }
                case Data.MSG_1: {
                    //  公司list
                    mListView.notifyDataSetChanged();
                    break;
                }
                case Data.MSG_2: {
                    //  组
                    getDevices();
                    break;
                }
                case Data.MSG_3: {
                    //  在线，离线数量
                    int all = mOnline + mOffline;
                    mTextViewAll.setText("全部（" + all + "）");
                    mTextViewOnline.setText("在线（" + mOnline + "）");
                    mTextViewOffline.setText("离线（" + mOffline + "）");
                    break;
                }
                case Data.MSG_4: {
                    //  获取设备列表
                    mTerminalBaseExpandableListAdapter.notifyDataSetChanged();
                    if (isFirstExpand) {
                        mExpandableListView.expandGroup(0);
                        isFirstExpand = false;
                    }
                    myHandler.removeMessages(Data.MSG_9);
                    myHandler.sendEmptyMessageDelayed(Data.MSG_9, mFlushTime);
                    break;
                }
                case Data.MSG_5: {
                    //  添加关注成功
                    mToastU.showToast(mStringMessage);
                    if (mLinearLayoutExpand.getVisibility() == View.VISIBLE) {
                        AdapterExpandableChildData childData = mAdapterExpandableGroupDataList.get(mParentPosition)
                                .getExpandableChildDatalist().get(mChildPosition);
                        childData.setAttention(true);
                        mTerminalBaseExpandableListAdapter.notifyDataSetChanged();
                    } else {
                        AdapterSearchDevicesData data = mAdapterSearchDevicesDataList.get(mSearchPosition);
                        data.setAttention(true);
                        mSearchDevicesAdapter.notifyDataSetChanged();
                    }
                    break;
                }
                case Data.MSG_6: {
                    //  删除关注成功
                    mToastU.showToast(mStringMessage);
                    if (mLinearLayoutExpand.getVisibility() == View.VISIBLE) {
                        AdapterExpandableChildData childData = mAdapterExpandableGroupDataList.get(mParentPosition)
                                .getExpandableChildDatalist().get(mChildPosition);
                        childData.setAttention(false);
                        mTerminalBaseExpandableListAdapter.notifyDataSetChanged();
                    } else {
                        AdapterSearchDevicesData data = mAdapterSearchDevicesDataList.get(mSearchPosition);
                        data.setAttention(false);
                        mSearchDevicesAdapter.notifyDataSetChanged();
                    }
                    break;
                }
                case Data.MSG_7: {
                    //  搜索框内容变更，延时一秒搜索
                    int size = mSearchKey.length();
                    if (size > 0) {
                        // TODO: 2017/10/10 搜索
                        mLinearLayoutExpand.setVisibility(View.GONE);
                        mListViewSearch.setVisibility(View.VISIBLE);

                        searchDevices(mSearchKey);
                    } else {
                        mLinearLayoutExpand.setVisibility(View.VISIBLE);
                        mListViewSearch.setVisibility(View.GONE);
                    }
                    break;
                }
                case Data.MSG_8: {
                    //  搜索结果
                    if (mAdapterSearchDevicesDataList.size() == 0) {
                        mToastU.showToast("搜索数据不存在");
                        mEditTextSearch.clearFocus();
                    }
                    mSearchDevicesAdapter.notifyDataSetChanged();
                    myHandler.removeMessages(Data.MSG_9);
                    myHandler.sendEmptyMessageDelayed(Data.MSG_9, mFlushTime);
                    break;
                }
                case Data.MSG_9: {
                    //  刷新设备
                    if (mLinearLayoutExpand.getVisibility() == View.VISIBLE) {
                        getDevices();
                    } else {
                        String key = mEditTextSearch.getText().toString();
                        searchDevices(key);
                    }
                    break;
                }
            }
        }
    }
}
