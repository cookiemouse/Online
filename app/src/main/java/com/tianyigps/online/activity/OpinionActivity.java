package com.tianyigps.online.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.tianyigps.online.R;
import com.tianyigps.online.base.BaseActivity;

public class OpinionActivity extends BaseActivity {

    private EditText mEditText;
    private Button mButton;

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
    }

    private void setEventListener() {
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/9/12 上传意见
                String opinion = mEditText.getText().toString();
            }
        });
    }
}
