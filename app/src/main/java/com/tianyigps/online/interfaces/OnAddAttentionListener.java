package com.tianyigps.online.interfaces;

import com.tianyigps.online.base.BaseInterface;

/**
 * Created by cookiemouse on 2017/9/4.
 */

public interface OnAddAttentionListener extends BaseInterface{
    @Override
    void onSuccess(String result);

    @Override
    void onFailure();
}
