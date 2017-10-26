package com.tianyigps.online.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;
import com.tianyigps.online.R;
import com.tianyigps.online.base.BaseActivity;
import com.tianyigps.online.bean.OpinionBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.interfaces.OnFeedbackListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.RegularU;

public class OpinionActivity extends BaseActivity {

    private static final String TAG = "OpinionActivity";

    private EditText mEditText;
    private Button mButton;

    private NetManager mNetManager;
    private SharedManager mSharedManager;
    private MyHandler myHandler;

    private String mStringMessage = "";
    private String mToken = "";
    private int mCid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opinion);

        init();

        setEventListener();
    }

    private void init() {
        this.setTitleText("意见反馈");

        mEditText = (EditText) findViewById(R.id.et_activity_opinion);
        mButton = (Button) findViewById(R.id.btn_activity_opinion);

        mSharedManager = new SharedManager(this);
        mNetManager = new NetManager();

        mToken = mSharedManager.getToken();
        mCid = mSharedManager.getCid();

        myHandler = new MyHandler();
    }

    private void setEventListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/9/12 上传意见
                String opinion = mEditText.getText().toString();
                if (RegularU.isEmpty(opinion)) {
                    mStringMessage = "请填写您宝贵的意见或建议，以便我们为您提供更好的服务";
                    myHandler.obtainMessage(Data.MSG_MSG).sendToTarget();
                    return;
                }
                mNetManager.feedback(mToken, mCid, opinion);
            }
        });

        mNetManager.setOnFeedbackListener(new OnFeedbackListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                OpinionBean opinionBean = gson.fromJson(result, OpinionBean.class);
                if (!opinionBean.isSuccess()){
                    mStringMessage = Data.DEFAULT_MESSAGE;
                    myHandler.obtainMessage(Data.MSG_MSG).sendToTarget();
                    return;
                }
                mStringMessage = "提交成功";
                myHandler.obtainMessage(Data.MSG_1).sendToTarget();
            }

            @Override
            public void onFailure() {
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_MSG);
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_MSG: {
                    showMessageDialog(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    showMessageDialog(mStringMessage);
                    break;
                }
            }
        }
    }
}
