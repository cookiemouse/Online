package com.tianyigps.online.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.tianyigps.online.R;

/**
 * Created by cookiemouse on 2017/9/28.
 */

public class SpeedPickerDialogFragment extends DialogFragment {

    private View mView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(getContext()).inflate(R.layout.view_popup_window, null);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return null;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false);
        builder.setView(mView);

        AlertDialog dialog = builder.create();

        setEventListener();

        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        Window dialogWindow = getDialog().getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.END | Gravity.TOP);
        lp.x = 0;
        lp.y = 0;
        lp.width = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(lp);

        getDialog().getWindow().setBackgroundDrawable(getActivity().getResources().getDrawable(R.color.colorNull));
    }

    private void setEventListener(){
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}
