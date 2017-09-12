package com.tianyigps.online.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.tianyigps.online.R;
import com.tianyigps.online.adapter.FlushTimeAdapter;
import com.tianyigps.online.base.BaseActivity;
import com.tianyigps.online.data.AdapterFlushTimeData;
import com.tianyigps.online.manager.SharedManager;

import java.util.ArrayList;
import java.util.List;

public class FlushTimeActivity extends BaseActivity {

    private static final String TAG = "FlushTimeActivity";

    private ListView mListView;
    private List<AdapterFlushTimeData> mAdapterFlushTimeDataList;
    private FlushTimeAdapter mFlushTimeAdapter;

    private SharedManager mSharedManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flush_time);

        init();

        setEventListener();
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (AdapterFlushTimeData data : mAdapterFlushTimeDataList) {
            if (data.isOpen()) {
                mSharedManager.saveFlushTime(data.getTime());
                Log.i(TAG, "onStop: time-->" + data.getTime());
                return;
            }
        }
    }

    private void init() {
        mListView = (ListView) findViewById(R.id.lv_activity_flush_time);

        mSharedManager = new SharedManager(this);

        mAdapterFlushTimeDataList = new ArrayList<>();
        for (int i = 1; i < 7; i++) {
            mAdapterFlushTimeDataList.add(new AdapterFlushTimeData(i * 10, false));
        }

        int time = mSharedManager.getFlushTime() / 10;
        if (mAdapterFlushTimeDataList.size() >= time) {
            mAdapterFlushTimeDataList.get((time - 1)).setOpen(true);
        } else {
            mAdapterFlushTimeDataList.get(0).setOpen(true);
        }

        mFlushTimeAdapter = new FlushTimeAdapter(this, mAdapterFlushTimeDataList);
        mListView.setAdapter(mFlushTimeAdapter);
    }

    private void setEventListener() {
    }
}
