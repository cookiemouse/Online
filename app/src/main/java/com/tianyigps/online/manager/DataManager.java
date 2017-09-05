package com.tianyigps.online.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.tianyigps.online.data.Account;
import com.tianyigps.online.data.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookiemouse on 2017/9/4.
 */

public class DataManager {

    private static final String TAG = "DataManager";

    private SQLiteDatabase mSqLiteDatabase;

    public DataManager(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context);
        mSqLiteDatabase = databaseHelper.getWritableDatabase();
    }

    public void saveAccount(String account, String password) {
        if (isExist(account)) {
            mSqLiteDatabase.delete(Data.TAB_ACCOUNT, "account=?", new String[]{account});
        }
        mSqLiteDatabase.beginTransaction();
        ContentValues contentValues = new ContentValues();
        contentValues.put("account", account);
        contentValues.put("password", password);
        mSqLiteDatabase.insert(Data.TAB_ACCOUNT, null, contentValues);
        mSqLiteDatabase.setTransactionSuccessful();
        mSqLiteDatabase.endTransaction();
    }

    public List<Account> getAccounts() {
        List<Account> accounts = new ArrayList<>();
        mSqLiteDatabase.beginTransaction();
        try {
            Cursor cursor = mSqLiteDatabase.query(Data.TAB_ACCOUNT, null, null, null, null, null, null);
            mSqLiteDatabase.setTransactionSuccessful();
            if (null != cursor && cursor.moveToFirst()) {
                do {
                    String account = cursor.getString(0);
                    String password = cursor.getString(1);
                    Log.i(TAG, "getAccount: account-->" + account + ", password-->" + password);
                    accounts.add(new Account(account, password));
                } while (cursor.moveToNext());

                cursor.close();
            }
        } catch (Exception e) {
            Log.e(TAG, e + "SqliteDatabase query error");
        } finally {
            mSqLiteDatabase.endTransaction();
        }
        return accounts;
    }

    @Deprecated
    public void deleteAccount() {
        //  未实现
    }

    @Deprecated
    public void modifyAccount() {
        //  未实现
    }

    private boolean isExist(String account) {
        for (Account a : getAccounts()) {
            if (a.getmAccount().equals(account)) {
                return true;
            }
        }
        return false;
    }

    public void close() {
        mSqLiteDatabase.close();
    }
}
