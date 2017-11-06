package com.tianyigps.online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.Gson;
import com.tianyigps.online.R;
import com.tianyigps.online.adapter.DetailsAdapter;
import com.tianyigps.online.base.BaseActivity;
import com.tianyigps.online.bean.DetailsBean;
import com.tianyigps.online.data.AdapterDetailsData;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.interfaces.OnShowTerminalInfoListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.RegularU;
import com.tianyigps.online.utils.TimeFormatU;

import java.util.ArrayList;
import java.util.List;

public class DetailsActivity extends BaseActivity {

    private static final String TAG = "DetailsActivity";

    private ListView mListView;
    private DetailsAdapter mDetailsAdapter;
    private List<AdapterDetailsData> mAdapterDetailsDataList;

    private NetManager mNetManager;
    private SharedManager mSharedManager;
    private String mToken;
    private String mImei;
    private String mStringMessage;
    private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        init();

        setEventListener();
    }

    private void init() {
        this.setTitleText("车辆信息");
        mListView = (ListView) findViewById(R.id.lv_activity_details);

        mAdapterDetailsDataList = new ArrayList<>();
        mDetailsAdapter = new DetailsAdapter(this, mAdapterDetailsDataList);

        mListView.setAdapter(mDetailsAdapter);

        myHandler = new MyHandler();

        mNetManager = new NetManager();
        mSharedManager = new SharedManager(this);
        mToken = mSharedManager.getToken();

        Intent intent = getIntent();
        mImei = intent.getStringExtra(Data.INTENT_IMEI);

        getTerminalInfo();
    }

    private void setEventListener() {
        mNetManager.setOnShowTerminalInfoListener(new OnShowTerminalInfoListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                DetailsBean detailsBean = gson.fromJson(result, DetailsBean.class);
                if (!detailsBean.isSuccess()) {
                    mStringMessage = detailsBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                DetailsBean.ObjBean objBean = detailsBean.getObj();
                mAdapterDetailsDataList.add(new AdapterDetailsData(objBean.getName()));
                mAdapterDetailsDataList.add(new AdapterDetailsData("设备号：" + objBean.getImei()));
                mAdapterDetailsDataList.add(new AdapterDetailsData("SIM卡号：" + objBean.getSim()));
                mAdapterDetailsDataList.add(new AdapterDetailsData("名称：" + objBean.getName()));
                if (RegularU.isEmpty(objBean.getCar_no())) {
                    mAdapterDetailsDataList.add(new AdapterDetailsData("车牌号：" ));
                } else {
                    mAdapterDetailsDataList.add(new AdapterDetailsData("车牌号：" + objBean.getCar_no()));
                }
                mAdapterDetailsDataList.add(new AdapterDetailsData("型号：" + objBean.getModel_name()));
                mAdapterDetailsDataList.add(new AdapterDetailsData("车型：" + objBean.getCar_type()));
                if (0 == objBean.getSale_time()) {
                    mAdapterDetailsDataList.add(new AdapterDetailsData("开通日期："));
                } else {
                    mAdapterDetailsDataList.add(new AdapterDetailsData("开通日期：" + TimeFormatU.millisToDate3(objBean.getSale_time())));
                }
                if (0 == objBean.getEnd_date()) {
                    mAdapterDetailsDataList.add(new AdapterDetailsData("到期日期："));
                } else {
                    mAdapterDetailsDataList.add(new AdapterDetailsData("到期日期：" + TimeFormatU.millisToDate3(objBean.getEnd_date())));
                }
                mAdapterDetailsDataList.add(new AdapterDetailsData("联系人：" + objBean.getContact_name()));
                mAdapterDetailsDataList.add(new AdapterDetailsData("联系电话：" + objBean.getContact_phone()));
                myHandler.sendEmptyMessage(Data.MSG_1);
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });
    }

    //  获取设备信息
    private void getTerminalInfo() {
        mNetManager.showTerminalInfo(mToken, mImei);
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
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
                    //  显示设备信息
                    mDetailsAdapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }
}
