package com.tianyigps.online.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.gson.Gson;
import com.tianyigps.online.R;
import com.tianyigps.online.base.BaseActivity;
import com.tianyigps.online.bean.CheckUserBean;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.interfaces.OnCheckUserListener;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.utils.RegularU;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private EditText mEditTextAccount, mEditTextPassword;
    private ImageView mImageViewPull;
    private Button mButtonLogin;
    private CheckBox mCheckBoxPassword, mCheckBoxAuto;

    private MyHandler myHandler;

    private NetManager mNetManager;
    private String mStringMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        setEventListener();
    }

    /**
     * 初始化
     */
    private void init() {
        mEditTextAccount = (EditText) findViewById(R.id.et_activity_login_account);
        mEditTextPassword = (EditText) findViewById(R.id.et_activity_login_password);
        mImageViewPull = (ImageView) findViewById(R.id.iv_activity_login_pull);
        mButtonLogin = (Button) findViewById(R.id.btn_activity_login_login);
        mCheckBoxPassword = (CheckBox) findViewById(R.id.cb_activity_login_remember_password);
        mCheckBoxAuto = (CheckBox) findViewById(R.id.cb_activity_login_auto);

        myHandler = new MyHandler();

        mNetManager = new NetManager();
    }

    /**
     * 设置事件监听
     */
    private void setEventListener() {
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String account = mEditTextAccount.getText().toString();
                String password = mEditTextPassword.getText().toString();
                boolean remembered = mCheckBoxPassword.isChecked();
                boolean auto = mCheckBoxAuto.isChecked();

                if (RegularU.isEmpty(account)) {
                    return;
                }
                if (RegularU.isEmpty(password)) {
                    return;
                }
                // TODO: 2017/9/4 登录
                showLoadingDialog();
                mNetManager.login(account, password, "");
            }
        });

        mImageViewPull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/9/4 下拉列表
            }
        });

        mNetManager.setOnCheckUserListener(new OnCheckUserListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                CheckUserBean checkUserBean = gson.fromJson(result, CheckUserBean.class);
                if (!checkUserBean.isSuccess()) {
                    mStringMessage = checkUserBean.getMsg();
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                }
                CheckUserBean.ObjBean objBean = checkUserBean.getObj();
                CheckUserBean.ObjBean.DataBean dataBean = objBean.getData();
                dataBean.getCid();
                dataBean.getPath();
                dataBean.getContactAddr();
                dataBean.getContactName();
                dataBean.getContactPhone();
                dataBean.getName();
                dataBean.getToken();
            }

            @Override
            public void onFailure() {
                Log.i(TAG, "onFailure: ");
                mStringMessage = Data.DEFAULT_MESSAGE;
                myHandler.sendEmptyMessage(Data.MSG_ERO);
            }
        });
    }

    /**
     * 显示popupWindow
     */
    private void showPopupWindow() {
    }

    /**
     * 内部类Handler
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case Data.MSG_ERO: {
                    showMessageDialog(mStringMessage);
                    break;
                }
                case Data.MSG_NOTHING: {
                    break;
                }
                case Data.MSG_MSG: {
                    showMessageDialog(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    //  登录成功
                    break;
                }
            }
        }
    }
}
