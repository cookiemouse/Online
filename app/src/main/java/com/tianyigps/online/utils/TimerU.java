package com.tianyigps.online.utils;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * Created by djc on 2017/7/13.
 */

public class TimerU {
    private static final String TAG = "TimerU";

    private static final int DELAY = 1000;

    private static final int FLAG_START = 0x01;
    private static final int FLAG_END = 0x02;

    private int mIntMax;
    private int mIntMin = 0;
    private int mIntNow;

    private boolean isStart = false;

    private TimerUHandler mHandler;

    private OnTickListener mOnTickListener;

    public TimerU(int mIntMax) {
        this.mIntMax = mIntMax;
        this.mIntNow = mIntMax;
        mHandler = new TimerUHandler();
    }

    public void start() {
        if (isStart) {
            return;
        }
        mHandler.sendEmptyMessage(FLAG_START);
        isStart = true;
    }

    private class TimerUHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (null == mOnTickListener) {
                return;
            }
            switch (msg.what) {
                case FLAG_START: {
                    mOnTickListener.onTick(mIntNow);
                    if (mIntNow > mIntMin) {
                        sendEmptyMessageDelayed(FLAG_START, DELAY);
                        mIntNow--;
                        break;
                    }
                    sendEmptyMessage(FLAG_END);
                    break;
                }
                case FLAG_END: {
                    mOnTickListener.onEnd();
                    mIntNow = mIntMax;
                    isStart = false;
                    break;
                }
                default: {
                    Log.i(TAG, "handleMessage: ");
                }
            }
        }
    }

    public interface OnTickListener {
        void onTick(int time);

        void onEnd();
    }

    public void setOnTickListener(OnTickListener listener) {
        this.mOnTickListener = listener;
    }
}
