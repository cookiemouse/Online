package com.tianyigps.online.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by djc on 2017/7/10.
 */

public class ToastU {
    private Context context;
    private Toast mToast;

    public ToastU(Context context) {
        this.context = context;
        mToast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
    }

    public void showToast(String msg){
        mToast.setText(msg);
        mToast.show();
    }
}
