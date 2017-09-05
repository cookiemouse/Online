package com.tianyigps.online.data;

/**
 * Created by cookiemouse on 2017/9/5.
 */

public class Account {
    private String mAccount, mPassword;

    public Account(String mAccount, String mPassword) {
        this.mAccount = mAccount;
        this.mPassword = mPassword;
    }

    public String getmAccount() {
        return mAccount;
    }

    public void setmAccount(String mAccount) {
        this.mAccount = mAccount;
    }

    public String getmPassword() {
        return mPassword;
    }

    public void setmPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
