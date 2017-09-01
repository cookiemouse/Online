package com.tianyigps.online;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;

import com.tianyigps.online.base.BaseActivity;
import com.tianyigps.online.data.Data;

public class GuideActivity extends BaseActivity {

    private Button mButton;

    //  Handler
    private MyHandler myHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        init();

        setEventListener();
    }

    private void init() {
        mButton = (Button) findViewById(R.id.btn_activity_guide);

        myHandler = new MyHandler();
    }

    private void setEventListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLoadingDialog();

                myHandler.sendEmptyMessageDelayed(Data.MSG_NOTHING, 2000);
            }
        });
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_NOTHING: {
                    disMissLoadingDialog();
                }
            }
        }
    }
}
