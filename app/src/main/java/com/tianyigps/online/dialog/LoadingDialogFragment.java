package com.tianyigps.online.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;

import com.tianyigps.online.R;

/**
 * Created by cookiemouse on 2017/8/17.
 */

public class LoadingDialogFragment extends DialogFragment {

    private View mViewDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewDialog = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_loading, null);

        SwipeRefreshLayout swipeRefreshLayout = mViewDialog.findViewById(R.id.srl_dialog_fragment);
        swipeRefreshLayout.setColorSchemeColors(0xff3cabfa);
        swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(mViewDialog);
        return builder.create();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().setCancelable(false);
        getDialog().getWindow().setBackgroundDrawable(getActivity().getResources().getDrawable(R.color.colorNull));
    }

    @Override
    public void show(FragmentManager manager, String tag) {
//        super.show(manager, tag);
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(LoadingDialogFragment.this, tag);
        ft.commitAllowingStateLoss();
    }
}
