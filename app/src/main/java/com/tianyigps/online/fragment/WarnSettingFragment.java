package com.tianyigps.online.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookiemouse on 2017/9/11.
 */

public class WarnSettingFragment extends Fragment {

    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonReceive, mRadioButtonNotReceive;
    private ListView mListView;

    private List<AdapterWarnSettingData> mAdapterWarnSettingDataList;
    private WarnSettingAdapter mWarnSettingAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warn_setting, container, false);

        init(view);

        setEventListener(view);

        return view;
    }

    private void init(View view) {

        mRadioGroup = view.findViewById(R.id.rg_fragment_warn_setting);
        mRadioButtonReceive = view.findViewById(R.id.rb_fragment_warn_setting_receive);
        mRadioButtonNotReceive = view.findViewById(R.id.rb_fragment_warn_setting_not_receive);

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
            }
        });

        mRadioButtonNotReceive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRadioButtonReceive.setChecked(false);
            }
        });
    }
}
