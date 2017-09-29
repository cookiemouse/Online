package com.tianyigps.online.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.tianyigps.online.R;
import com.tianyigps.online.data.Data;

/**
 * Created by cookiemouse on 2017/9/28.
 */

public class SpeedPickerDialogFragment extends DialogFragment {

    private static final String TAG = "SpeedPicker";

    private View mView;

    private TextView mTextView1, mTextView2, mTextView3, mTextView4, mTextView5;

    private int mSpeed = Data.SPEED_100;

    private OnChoiceSpeedListener mOnChoiceSpeedListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mView = LayoutInflater.from(getContext()).inflate(R.layout.view_popup_window, null);

        mTextView1 = mView.findViewById(R.id.tv_view_popup_window_speed_1);
        mTextView2 = mView.findViewById(R.id.tv_view_popup_window_speed_2);
        mTextView3 = mView.findViewById(R.id.tv_view_popup_window_speed_3);
        mTextView4 = mView.findViewById(R.id.tv_view_popup_window_speed_4);
        mTextView5 = mView.findViewById(R.id.tv_view_popup_window_speed_5);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mSpeed = bundle.getInt(Data.KEY_SPEED);
        Log.i(TAG, "onCreateView: speed-->" + mSpeed);
        setSeep(mSpeed);
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

    private void setEventListener() {
        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mTextView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                mTextView1.setTextColor(getContext().getResources().getColor(R.color.colorBlueTheme));
                if (null == mOnChoiceSpeedListener) {
                    throw new NullPointerException("OnChoiceSpeedListener is null");
                }
                mOnChoiceSpeedListener.onChoice(Data.SPEED_400);
                dismiss();
            }
        });

        mTextView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                mTextView2.setTextColor(getContext().getResources().getColor(R.color.colorBlueTheme));
                if (null == mOnChoiceSpeedListener) {
                    throw new NullPointerException("OnChoiceSpeedListener is null");
                }
                mOnChoiceSpeedListener.onChoice(Data.SPEED_200);
                dismiss();
            }
        });

        mTextView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                mTextView3.setTextColor(getContext().getResources().getColor(R.color.colorBlueTheme));
                if (null == mOnChoiceSpeedListener) {
                    throw new NullPointerException("OnChoiceSpeedListener is null");
                }
                mOnChoiceSpeedListener.onChoice(Data.SPEED_100);
                dismiss();
            }
        });

        mTextView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                mTextView4.setTextColor(getContext().getResources().getColor(R.color.colorBlueTheme));
                if (null == mOnChoiceSpeedListener) {
                    throw new NullPointerException("OnChoiceSpeedListener is null");
                }
                mOnChoiceSpeedListener.onChoice(Data.SPEED_50);
                dismiss();
            }
        });

        mTextView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                mTextView5.setTextColor(getContext().getResources().getColor(R.color.colorBlueTheme));
                if (null == mOnChoiceSpeedListener) {
                    throw new NullPointerException("OnChoiceSpeedListener is null");
                }
                mOnChoiceSpeedListener.onChoice(Data.SPEED_10);
                dismiss();
            }
        });
    }

    //  设置为初始化状态
    private void setDefault() {
        mTextView1.setTextColor(getContext().getResources().getColor(R.color.colorBlackText));
        mTextView2.setTextColor(getContext().getResources().getColor(R.color.colorBlackText));
        mTextView3.setTextColor(getContext().getResources().getColor(R.color.colorBlackText));
        mTextView4.setTextColor(getContext().getResources().getColor(R.color.colorBlackText));
        mTextView5.setTextColor(getContext().getResources().getColor(R.color.colorBlackText));
    }

    //  设置状态
    private void setSeep(int speed) {
        this.setDefault();
        switch (speed) {
            case Data.SPEED_10: {
                mTextView5.setTextColor(getContext().getResources().getColor(R.color.colorBlueTheme));
                break;
            }
            case Data.SPEED_50: {
                mTextView4.setTextColor(getContext().getResources().getColor(R.color.colorBlueTheme));
                break;
            }
            case Data.SPEED_100: {
                mTextView3.setTextColor(getContext().getResources().getColor(R.color.colorBlueTheme));
                break;
            }
            case Data.SPEED_200: {
                mTextView2.setTextColor(getContext().getResources().getColor(R.color.colorBlueTheme));
                break;
            }
            case Data.SPEED_400: {
                mTextView1.setTextColor(getContext().getResources().getColor(R.color.colorBlueTheme));
                break;
            }
            default: {
                mTextView3.setTextColor(getContext().getResources().getColor(R.color.colorBlueTheme));
                Log.i(TAG, "setSeep: default-->" + speed);
            }
        }
    }

    public interface OnChoiceSpeedListener {
        void onChoice(int speed);
    }

    public void setOnChoiceSpeedListener(OnChoiceSpeedListener listener) {
        this.mOnChoiceSpeedListener = listener;
    }
}
