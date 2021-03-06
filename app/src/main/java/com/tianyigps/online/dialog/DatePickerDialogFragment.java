package com.tianyigps.online.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.tianyigps.online.R;
import com.tianyigps.online.data.DatePickerData;
import com.tianyigps.online.utils.TimeFormatU;
import com.tianyigps.online.utils.ToastU;
import com.yundian.bottomdialog.v4.BottomDialog;

import java.util.Calendar;

/**
 * Created by cookiemouse on 2017/9/27.
 */

public class DatePickerDialogFragment extends DialogFragment {

    private static final String TAG = "DatePicker";

    private static final long HOUR = 60 * 60 * 1000;
    private static final long DAY_5 = 5 * 24 * 60 * 60 * 1000;

    private View mView;

    private TextView mTextViewStart, mTextViewEnd;
    private TextView mTextViewCustom, mTextViewStartTitle, mTextViewEndTitle, mTextViewToday, mTextViewYesterday, mTextViewHour;
    private RadioButton mRadioButtonCustom, mRadioButtonToday, mRadioButtonYesterday, mRadioButtonHour;
    private Button mButtonEnsure, mButtonCancle;
    private LinearLayout mLinearLayoutCustom, mLinearLayoutToday, mLinearLayoutYesterday, mLinearLayoutHour;

    //  是开始时间还是结束时间
    private boolean isStart = true;
    private String mStartTime, mEndTime;
    private long mNow, mStart, mEnd;

    private BottomDialog mBottomDialog;
    private DatePicker mDatePicker;
    private TimePicker mTimePicker;

    //  是开始时间还是结束时间弹出BottomDialog
    private boolean mIsStartDialog = true;

    private ToastU mToastU;

    private OnChoiceDateListener mOnChoiceDateListener;

    //  时间选择器显示时间
    private long mDateShow = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mView = LayoutInflater.from(getContext()).inflate(R.layout.dialog_date_picker, null);

        init(mView);

        setEventListener();
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
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    private void init(View view) {
        mRadioButtonCustom = view.findViewById(R.id.rb_dialog_date_picker_custom);
        mRadioButtonToday = view.findViewById(R.id.rb_dialog_date_picker_today);
        mRadioButtonYesterday = view.findViewById(R.id.rb_dialog_date_picker_yesterday);
        mRadioButtonHour = view.findViewById(R.id.rb_dialog_date_picker_hour);

        mTextViewStart = view.findViewById(R.id.tv_dialog_date_picker_start);
        mTextViewEnd = view.findViewById(R.id.tv_dialog_date_picker_end);

        mTextViewCustom = view.findViewById(R.id.tv_dialog_date_picker_custom);
        mTextViewStartTitle = view.findViewById(R.id.tv_dialog_date_picker_start_title);
        mTextViewEndTitle = view.findViewById(R.id.tv_dialog_date_picker_end_title);
        mTextViewToday = view.findViewById(R.id.tv_dialog_date_picker_today);
        mTextViewYesterday = view.findViewById(R.id.tv_dialog_date_picker_yesterday);
        mTextViewHour = view.findViewById(R.id.tv_dialog_date_picker_hour);

        mButtonEnsure = view.findViewById(R.id.btn_dialog_date_picker_ensure);
        mButtonCancle = view.findViewById(R.id.btn_dialog_date_picker_cancel);

        mLinearLayoutCustom = view.findViewById(R.id.ll_dialog_date_picker_custom);
        mLinearLayoutToday = view.findViewById(R.id.ll_dialog_date_picker_today);
        mLinearLayoutYesterday = view.findViewById(R.id.ll_dialog_date_picker_yesterday);
        mLinearLayoutHour = view.findViewById(R.id.ll_dialog_date_picker_hour);

        Calendar calendar = Calendar.getInstance();
        mNow = calendar.getTimeInMillis();
        mEnd = calendar.getTimeInMillis();
        mEndTime = TimeFormatU.millisToDate2(mEnd);
        mTextViewEnd.setText(mEndTime);

        calendar.setTimeInMillis(mNow - HOUR);
        mStart = calendar.getTimeInMillis();
        mStartTime = TimeFormatU.millisToDate2(mStart);
        mTextViewStart.setText(mStartTime);

        mToastU = new ToastU(getContext());
    }

    private void setEventListener() {
        mLinearLayoutCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                mTextViewStart.setEnabled(true);
                mTextViewStartTitle.setEnabled(true);
                mTextViewEnd.setEnabled(true);
                mTextViewEndTitle.setEnabled(true);
                mTextViewCustom.setEnabled(true);

                mRadioButtonCustom.setChecked(true);

                Calendar calendar = Calendar.getInstance();
                mEnd = calendar.getTimeInMillis();
                mEndTime = TimeFormatU.millisToDate2(mEnd);
                mTextViewEnd.setText(mEndTime);

                calendar.setTimeInMillis(mNow - HOUR);
                mStart = calendar.getTimeInMillis();
                mStartTime = TimeFormatU.millisToDate2(mStart);
                mTextViewStart.setText(mStartTime);
            }
        });

        mLinearLayoutToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                mTextViewToday.setEnabled(true);
                mRadioButtonToday.setChecked(true);
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(year, month, day, 0, 0, 0);
                mStart = calendar.getTimeInMillis();
                mStartTime = TimeFormatU.millisToDate2(mStart);

                calendar.setTimeInMillis(day - 1);
                calendar.set(year, month, day, 23, 59, 59);
                mEnd = calendar.getTimeInMillis();
                mEndTime = TimeFormatU.millisToDate2(mEnd);
            }
        });

        mLinearLayoutYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                mTextViewYesterday.setEnabled(true);
                mRadioButtonYesterday.setChecked(true);
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH) - 1;
                calendar.set(year, month, day, 0, 0, 0);
                mStart = calendar.getTimeInMillis();
                mStartTime = TimeFormatU.millisToDate2(mStart);

                calendar.setTimeInMillis(day - 1);
                calendar.set(year, month, day, 23, 59, 59);
                mEnd = calendar.getTimeInMillis();
                mEndTime = TimeFormatU.millisToDate2(mEnd);
            }
        });

        mLinearLayoutHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                mTextViewHour.setEnabled(true);
                mRadioButtonHour.setChecked(true);
                Calendar calendar = Calendar.getInstance();
                mEnd = calendar.getTimeInMillis();
                mEndTime = TimeFormatU.millisToDate2(mEnd);

                calendar.setTimeInMillis(mNow - HOUR);
                mStart = calendar.getTimeInMillis();
                mStartTime = TimeFormatU.millisToDate2(mStart);
            }
        });

        mRadioButtonCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                mTextViewStart.setEnabled(true);
                mTextViewStartTitle.setEnabled(true);
                mTextViewEnd.setEnabled(true);
                mTextViewEndTitle.setEnabled(true);
                mTextViewCustom.setEnabled(true);

                mRadioButtonCustom.setChecked(true);

                Calendar calendar = Calendar.getInstance();
                mEnd = calendar.getTimeInMillis();
                mEndTime = TimeFormatU.millisToDate2(mEnd);
                mTextViewEnd.setText(mEndTime);

                calendar.setTimeInMillis(mNow - HOUR);
                mStart = calendar.getTimeInMillis();
                mStartTime = TimeFormatU.millisToDate2(mStart);
                mTextViewStart.setText(mStartTime);
            }
        });

        mRadioButtonToday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                mTextViewToday.setEnabled(true);
                mRadioButtonToday.setChecked(true);
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                calendar.set(year, month, day, 0, 0, 0);
                mStart = calendar.getTimeInMillis();
                mStartTime = TimeFormatU.millisToDate2(mStart);

                calendar.setTimeInMillis(day - 1);
                calendar.set(year, month, day, 23, 59, 59);
                mEnd = calendar.getTimeInMillis();
                mEndTime = TimeFormatU.millisToDate2(mEnd);
            }
        });

        mRadioButtonYesterday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                mTextViewYesterday.setEnabled(true);
                mRadioButtonYesterday.setChecked(true);
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH) - 1;
                calendar.set(year, month, day, 0, 0, 0);
                mStart = calendar.getTimeInMillis();
                mStartTime = TimeFormatU.millisToDate2(mStart);

                calendar.setTimeInMillis(day - 1);
                calendar.set(year, month, day, 23, 59, 59);
                mEnd = calendar.getTimeInMillis();
                mEndTime = TimeFormatU.millisToDate2(mEnd);
            }
        });

        mRadioButtonHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setDefault();
                mTextViewHour.setEnabled(true);
                mRadioButtonHour.setChecked(true);
                Calendar calendar = Calendar.getInstance();
                mEnd = calendar.getTimeInMillis();
                mEndTime = TimeFormatU.millisToDate2(mEnd);

                calendar.setTimeInMillis(mNow - HOUR);
                mStart = calendar.getTimeInMillis();
                mStartTime = TimeFormatU.millisToDate2(mStart);
            }
        });

        mTextViewStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStart = true;
                mDateShow = TimeFormatU.dateToMillis3(mTextViewStart.getText().toString());
                showBottomDialog();
            }
        });

        mTextViewEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isStart = false;
                mDateShow = TimeFormatU.dateToMillis3(mTextViewEnd.getText().toString());
                showBottomDialog();
            }
        });

        mButtonCancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        mButtonEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 2017/9/27 确定
                if (null == mOnChoiceDateListener) {
                    throw new NullPointerException("OnChoiceDateListener is null");
                }
                if (mEnd - mStart > DAY_5) {
                    show5DayDialog();
                    return;
                }
                mOnChoiceDateListener.onChoice(new DatePickerData(TimeFormatU.millisToDate(mStart), TimeFormatU.millisToDate(mEnd)));
                dismiss();
            }
        });
    }

    //  所有的radioButton均不选择
    private void setDefault() {
        mTextViewStart.setEnabled(false);
        mTextViewEnd.setEnabled(false);

        mTextViewCustom.setEnabled(false);
        mTextViewStartTitle.setEnabled(false);
        mTextViewEndTitle.setEnabled(false);
        mTextViewToday.setEnabled(false);
        mTextViewYesterday.setEnabled(false);
        mTextViewHour.setEnabled(false);

        mRadioButtonCustom.setChecked(false);
        mRadioButtonToday.setChecked(false);
        mRadioButtonYesterday.setChecked(false);
        mRadioButtonHour.setChecked(false);
    }

    //  显示底部时间选择框
    private void showBottomDialog() {
        mBottomDialog = new BottomDialog();
        View view = LayoutInflater.from(getContext()).inflate(R.layout.view_date_picker, null);

        mDatePicker = view.findViewById(R.id.dp_view_date_picker);
        mTimePicker = view.findViewById(R.id.tp_view_date_picker);
        mDatePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        mTimePicker.setDescendantFocusability(DatePicker.FOCUS_BLOCK_DESCENDANTS);
        mTimePicker.setIs24HourView(true);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(mDateShow);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        Log.i(TAG, "showBottomDialog: year-->" + year);
        Log.i(TAG, "showBottomDialog: month-->" + month);
        Log.i(TAG, "showBottomDialog: day-->" + day);
        Log.i(TAG, "showBottomDialog: hour-->" + hour);
        Log.i(TAG, "showBottomDialog: min-->" + min);

        mDatePicker.updateDate(year, month, day);
        mTimePicker.setCurrentHour(hour);
        mTimePicker.setCurrentMinute(min);

        Button btnEnsure = view.findViewById(R.id.btn_view_date_picker_ensure);
        Button btnCancel = view.findViewById(R.id.btn_view_date_picker_cancel);

        btnEnsure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int year = mDatePicker.getYear();
                int month = mDatePicker.getMonth();
                int day = mDatePicker.getDayOfMonth();
                int hour = mTimePicker.getCurrentHour();
                int min = mTimePicker.getCurrentMinute();

                Calendar calendar = Calendar.getInstance();
                calendar.set(year, month, day, hour, min, 0);

                if (isStart) {
                    mStart = calendar.getTimeInMillis();
                    mStartTime = TimeFormatU.millisToDate2(mStart);
                    if (mStart > mNow) {
                        mToastU.showToast("开始时间大于当前时间");
                        return;
                    }
                    mTextViewStart.setText(mStartTime);
                } else {
                    mEnd = calendar.getTimeInMillis();
                    mEndTime = TimeFormatU.millisToDate2(mEnd);
                    if (mEnd > mNow) {
                        mToastU.showToast("结束时间大于当前时间");
                        return;
                    }
                    mTextViewEnd.setText(mEndTime);
                }

                mBottomDialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomDialog.dismiss();
            }
        });

        mBottomDialog.setContentView(view);
        mBottomDialog.setCancelable(false);
        mBottomDialog.show(getChildFragmentManager(), "");
    }

    //  显示超过5天对话框
    private void show5DayDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("一次性查询最多离开的放 5天的轨迹，请重新选择。");
        builder.setCancelable(false);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  do nothing
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public interface OnChoiceDateListener {
        void onChoice(DatePickerData datePickerData);
    }

    public void setOnChoiceDateListener(OnChoiceDateListener listener) {
        this.mOnChoiceDateListener = listener;
    }
}
