package com.tianyigps.online.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tianyigps.online.data.Data;

/**
 * Created by cookiemouse on 2017/9/5.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "DatabaseHelper";

    private static final int VERSION = 1;

    private static final String TAB_ACCOUNT = "create table " + Data.TAB_ACCOUNT + "(account TEXT,password TEXT)";

    public DatabaseHelper(Context context) {
        this(context, Data.DATA_DB_NAME, null, VERSION);
    }

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(TAB_ACCOUNT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
