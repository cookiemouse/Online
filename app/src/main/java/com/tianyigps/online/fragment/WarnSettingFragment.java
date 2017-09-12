package com.tianyigps.online.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.tianyigps.online.R;
import com.tianyigps.online.activity.FragmentContentActivity;
import com.tianyigps.online.adapter.WarnSettingAdapter;
import com.tianyigps.online.data.AdapterWarnSettingData;
import com.tianyigps.online.manager.SharedManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookiemouse on 2017/9/11.
 */

public class WarnSettingFragment extends Fragment {

    private static final String TAG = "WarnSettingFragment";

    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonReceive, mRadioButtonNotReceive;
    private ListView mListView;

    private List<AdapterWarnSettingData> mAdapterWarnSettingDataList;
    private WarnSettingAdapter mWarnSettingAdapter;

    private SharedManager mSharedManager;

    private OnDismissListener mOnDismissListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warn_setting, container, false);

        init(view);

        setEventListener(view);

        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            String type = "";
            for (AdapterWarnSettingData data : mAdapterWarnSettingDataList) {
                if (data.isOpen()) {
                    type += (data.getType() + ",");
                }
            }
            Log.i(TAG, "onHiddenChanged: type-->" + type);
            mSharedManager.saveWarnType(type);

            if (null == mOnDismissListener) {
                throw new NullPointerException("OnDismissListener is null");
            }
            mOnDismissListener.onDismiss(type);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        String type = mSharedManager.getWarnType();
        Log.i(TAG, "onResume: type-->" + type);
        String[] types = type.split(",");
        Log.i(TAG, "onResume: length-->" + types.length);
        for (AdapterWarnSettingData data : mAdapterWarnSettingDataList) {
            data.setOpen(false);
        }
        for (String str : types) {
            for (AdapterWarnSettingData data : mAdapterWarnSettingDataList) {
                if (data.getType().equals(str)) {
                    data.setOpen(true);
                }
            }
        }
    }

    private void init(View view) {
        mSharedManager = new SharedManager(getContext());

        mRadioGroup = view.findViewById(R.id.rg_fragment_warn_setting);
        mRadioButtonReceive = view.findViewById(R.id.rb_fragment_warn_setting_receive);
        mRadioButtonNotReceive = view.findViewById(R.id.rb_fragment_warn_setting_not_receive);

        boolean isWarn = mSharedManager.isWarn();
        if (isWarn) {
            mRadioButtonReceive.setChecked(true);
            mRadioButtonNotReceive.setChecked(false);
        } else {
            mRadioButtonReceive.setChecked(false);
            mRadioButtonNotReceive.setChecked(true);
        }

        mListView = view.findViewById(R.id.lv_fragment_warn_setting);

        mAdapterWarnSettingDataList = new ArrayList<>();
        mAdapterWarnSettingDataList.add(new AdapterWarnSettingData("01", false));
        mAdapterWarnSettingDataList.add(new AdapterWarnSettingData("99", false));
        mAdapterWarnSettingDataList.add(new AdapterWarnSettingData("84", false));
        mAdapterWarnSettingDataList.add(new AdapterWarnSettingData("83", false));
        mAdapterWarnSettingDataList.add(new AdapterWarnSettingData("93", false));
        mAdapterWarnSettingDataList.add(new AdapterWarnSettingData("97", false));
        mAdapterWarnSettingDataList.add(new AdapterWarnSettingData("98", false));

        mWarnSettingAdapter = new WarnSettingAdapter(getContext(), mAdapterWarnSettingDataList);

        mListView.setAdapter(mWarnSettingAdapter);
    }

    private void setEventListener(View view) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.hide(WarnSettingFragment.this);
                fragmentTransaction.commit();
                FragmentContentActivity activity = (FragmentContentActivity) getActivity();
                activity.hideBottomView();
            }
        });

        mRadioGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //  do nothing
            }
        });

        mRadioButtonReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRadioButtonNotReceive.setChecked(false);
                mSharedManager.saveWarn(true);
            }
        });

        mRadioButtonNotReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRadioButtonReceive.setChecked(false);
                mSharedManager.saveWarn(false);
            }
        });
    }

    public interface OnDismissListener {
        void onDismiss(String type);
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.mOnDismissListener = listener;
    }
}
