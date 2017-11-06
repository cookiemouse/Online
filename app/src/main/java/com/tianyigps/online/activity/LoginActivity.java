package com.tianyigps.online.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.gson.Gson;
import com.tianyigps.online.R;
import com.tianyigps.online.adapter.PopupAccountAdapter;
import com.tianyigps.online.base.BaseActivity;
import com.tianyigps.online.bean.CheckUserBean;
import com.tianyigps.online.data.Account;
import com.tianyigps.online.data.Data;
import com.tianyigps.online.interfaces.OnCheckUserListener;
import com.tianyigps.online.manager.DataManager;
import com.tianyigps.online.manager.NetManager;
import com.tianyigps.online.manager.SharedManager;
import com.tianyigps.online.utils.RegularU;
import com.tianyigps.online.utils.TimerU;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    private EditText mEditTextAccount, mEditTextPassword;
    private ImageView mImageViewPull;
    private Button mButtonLogin;
    private CheckBox mCheckBoxPassword, mCheckBoxAuto;

    private LinearLayout mLinearLayoutAccount;
    private View mViewLine;

    private MyHandler myHandler;

    private NetManager mNetManager;
    private String mStringMessage;
    private SharedManager mSharedManager;
    private DataManager mDataManager;

    private int mCid;
    private String mPath, mContactAddr, mContactPhone, mContactName, mName, mToken, mAccount, mPassword;
    private boolean mRememberPassword, mAutoLogin;

    //  popupWindow账户列表
    private PopupWindow mPopupWindow;
    private List<Account> mAccountList;
    private PopupAccountAdapter mPopupAccountAdapter;
    private Account mAccountData;
    private boolean isOpened = false;

    //  双击back键退出
    private boolean mExitAble = false;
    private TimerU mTimerU;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();

        setEventListener();
    }

    @Override
    public void onBackPressed() {
        if (mExitAble) {
            this.finish();
            return;
        }
        mExitAble = true;
        showToast("再按一次退出");
        mTimerU.start();
    }

    @Override
    protected void onDestroy() {
        mDataManager.close();
        super.onDestroy();
    }

    /**
     * 初始化
     */
    private void init() {
        this.setTitleVisibilite(false);

        mEditTextAccount = (EditText) findViewById(R.id.et_activity_login_account);
        mEditTextPassword = (EditText) findViewById(R.id.et_activity_login_password);
        mImageViewPull = (ImageView) findViewById(R.id.iv_activity_login_pull);
        mButtonLogin = (Button) findViewById(R.id.btn_activity_login_login);
        mCheckBoxPassword = (CheckBox) findViewById(R.id.cb_activity_login_remember_password);
        mCheckBoxAuto = (CheckBox) findViewById(R.id.cb_activity_login_auto);

        mLinearLayoutAccount = (LinearLayout) findViewById(R.id.ll_activity_login_account);
        mViewLine = findViewById(R.id.view_activity_login_line_1);

        mTimerU = new TimerU(2);

        myHandler = new MyHandler();

        mNetManager = new NetManager();

        mSharedManager = new SharedManager(this);

        mDataManager = new DataManager(this);

        mPopupWindow = new PopupWindow(this);
        mPopupWindow.setOutsideTouchable(false);

        mAccountList = new ArrayList<>();

        mRememberPassword = mSharedManager.getRememberPassword();
        mAutoLogin = mSharedManager.getAutoLogin();
        mCheckBoxPassword.setChecked(mRememberPassword);
        mCheckBoxAuto.setChecked(mAutoLogin);
        mEditTextAccount.setText(mSharedManager.getAccount());
        if (mRememberPassword) {
            mEditTextPassword.setText(mSharedManager.getPassword());
        }
    }

    /**
     * 设置事件监听
     */
    private void setEventListener() {
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAccount = mEditTextAccount.getText().toString();
                mPassword = mEditTextPassword.getText().toString();
                mRememberPassword = mCheckBoxPassword.isChecked();
                mAutoLogin = mCheckBoxAuto.isChecked();

                if (RegularU.isEmpty(mAccount)) {
                    mStringMessage = "请输入用户名";
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                if (RegularU.isEmpty(mPassword)) {
                    mStringMessage = "请输入密码";
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }

                //  2017/9/4 登录
                showLoadingDialog();
                mNetManager.login(mAccount, mPassword);
            }
        });

        mImageViewPull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/9/4 下拉列表
                if (isOpened) {
                    mPopupWindow.dismiss();
                    return;
                }
                if (mDataManager.getAccounts().size() > 0) {
                    showPopupWindow();
                }
            }
        });

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                isOpened = false;
                mLinearLayoutAccount.setBackgroundColor(getResources().getColor(R.color.colorNull));
            }
        });

        mTimerU.setOnTickListener(new TimerU.OnTickListener() {
            @Override
            public void onTick(int time) {
            }

            @Override
            public void onEnd() {
                mExitAble = false;
            }
        });

        mNetManager.setOnCheckUserListener(new OnCheckUserListener() {
            @Override
            public void onSuccess(String result) {
                Log.i(TAG, "onSuccess: result-->" + result);
                Gson gson = new Gson();
                CheckUserBean checkUserBean = gson.fromJson(result, CheckUserBean.class);
                if (!checkUserBean.isSuccess()) {
                    mStringMessage = "账号或密码错误";
                    myHandler.sendEmptyMessage(Data.MSG_MSG);
                    return;
                }
                CheckUserBean.ObjBean objBean = checkUserBean.getObj();
                CheckUserBean.ObjBean.DataBean dataBean = objBean.getData();
                mCid = dataBean.getCid();
                mPath = dataBean.getPath();
                mContactAddr = dataBean.getContactAddr();
                mContactName = dataBean.getContactName();
                mContactPhone = dataBean.getContactPhone();
                mName = dataBean.getName();
                mToken = dataBean.getToken();

                myHandler.sendEmptyMessage(Data.MSG_1);
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
        if (mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
        View view = LayoutInflater.from(this).inflate(R.layout.view_popup_account, null);
        mPopupWindow.setBackgroundDrawable(null);
        ListView listView = view.findViewById(R.id.lv_view_popup_account);
        mAccountList.clear();
        List<Account> accounts = mDataManager.getAccounts();
        String editAccount = mEditTextAccount.getText().toString();
        for (Account account : accounts) {
            if (account.getmAccount().equals(editAccount)) {
                accounts.remove(account);
                break;
            }
        }
        mAccountList.addAll(accounts);

        mPopupAccountAdapter = new PopupAccountAdapter(this, mAccountList);
        listView.setAdapter(mPopupAccountAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                mAccountData = mAccountList.get(i);
                myHandler.sendEmptyMessage(Data.MSG_2);
                mPopupWindow.dismiss();
            }
        });
        mPopupAccountAdapter.setOnDeleteListener(new PopupAccountAdapter.OnDeleteListener() {
            @Override
            public void onDelete(int position) {
                // TODO: 2017/10/20 删除帐号
                String account = mAccountList.get(position).getmAccount();
                mDataManager.deleteAccount(account);
                mAccountList.remove(position);
                mPopupAccountAdapter.notifyDataSetChanged();
            }
        });
        mPopupWindow.setContentView(view);
        mPopupWindow.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        if (mAccountList.size() > 0) {
            mPopupWindow.showAsDropDown(mViewLine, -30, -30);
            isOpened = true;
            mEditTextAccount.clearFocus();
            mLinearLayoutAccount.setBackgroundResource(R.drawable.bg_popup_account);
        }
    }

    //  跳转到主页
    private void toFragmentContent() {
        Intent intent = new Intent(LoginActivity.this, FragmentContentActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void showToastCenter(String msg) {
        Toast toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    /**
     * 内部类Handler
     */
    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            disMissLoadingDialog();
            switch (msg.what) {
                case Data.MSG_ERO: {
                    showToast(mStringMessage);
                    break;
                }
                case Data.MSG_NOTHING: {
                    break;
                }
                case Data.MSG_MSG: {
                    showToastCenter(mStringMessage);
                    break;
                }
                case Data.MSG_1: {
                    //  登录成功
                    mSharedManager.saveUserData(mCid, mPath, mContactAddr, mContactName, mContactPhone, mName, mToken, mAccount, mPassword);
                    mSharedManager.saveRememberPassword(mRememberPassword);
                    mSharedManager.saveAutoLogin(mAutoLogin);
                    if (mRememberPassword) {
                        mDataManager.saveAccount(mAccount, mPassword);
                    } else {
                        mDataManager.deleteAccount(mAccount);
                    }
                    JPushInterface.setAlias(LoginActivity.this, 0, "" + mCid);
                    toFragmentContent();
                    break;
                }
                case Data.MSG_2: {
                    //  popupWindow选择帐户
                    mEditTextAccount.setText(mAccountData.getmAccount());
                    mEditTextPassword.setText(mAccountData.getmPassword());
                    break;
                }
            }
        }
    }
}
