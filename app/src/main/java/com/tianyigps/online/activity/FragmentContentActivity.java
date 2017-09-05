package com.tianyigps.online.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianyigps.online.R;
import com.tianyigps.online.fragment.ChoiceCarFragment;
import com.tianyigps.online.fragment.MonitorFragment;
import com.tianyigps.online.fragment.SettingFragment;
import com.tianyigps.online.fragment.WarnFragment;

public class FragmentContentActivity extends AppCompatActivity {

    private FrameLayout mFrameLayout;

    private ChoiceCarFragment mChoiceCarFragment;
    private MonitorFragment mMonitorFragment;
    private WarnFragment mWarnFragment;
    private SettingFragment mSettingFragment;

    private FragmentManager mFragmentManager;

    private LinearLayout mLinearLayoutChoiceCar, mLinearLayoutMonitor, mLinearLayoutWarn, mLinearLayoutSetting;
    private ImageView mImageViewChoiceCar, mImageViewMonitor, mImageViewWarn, mImageViewSetting;
    private TextView mTextViewChoiceCar, mTextViewMonitor, mTextViewWarn, mTextViewSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_content);

        //  透明状态栏
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        init();

        setEventListener();
    }

    private void init() {
        mFrameLayout = (FrameLayout) findViewById(R.id.fl_activity_fragment_content);

        mLinearLayoutChoiceCar = (LinearLayout) findViewById(R.id.ll_content_bottom_choice_car);
        mLinearLayoutMonitor = (LinearLayout) findViewById(R.id.ll_content_bottom_monitor);
        mLinearLayoutWarn = (LinearLayout) findViewById(R.id.ll_content_bottom_warn);
        mLinearLayoutSetting = (LinearLayout) findViewById(R.id.ll_content_bottom_setting);

        mImageViewChoiceCar = (ImageView) findViewById(R.id.iv_content_bottom_choice_car);
        mImageViewMonitor = (ImageView) findViewById(R.id.iv_content_bottom_monitor);
        mImageViewWarn = (ImageView) findViewById(R.id.iv_content_bottom_warn);
        mImageViewSetting = (ImageView) findViewById(R.id.iv_content_bottom_setting);

        mTextViewChoiceCar = (TextView) findViewById(R.id.tv_content_bottom_choice_car);
        mTextViewMonitor = (TextView) findViewById(R.id.tv_content_bottom_monitor);
        mTextViewWarn = (TextView) findViewById(R.id.tv_content_bottom_warn);
        mTextViewSetting = (TextView) findViewById(R.id.tv_content_bottom_setting);

        mChoiceCarFragment = new ChoiceCarFragment();
        mMonitorFragment = new MonitorFragment();
        mWarnFragment = new WarnFragment();
        mSettingFragment = new SettingFragment();

        mFragmentManager = getSupportFragmentManager();

        showChoiceCar();
    }

    private void setEventListener() {
        mLinearLayoutChoiceCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoiceCar();
            }
        });

        mLinearLayoutMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMonitor();
            }
        });

        mLinearLayoutWarn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showWarn();
            }
        });

        mLinearLayoutSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSetting();
            }
        });
    }

    //  置0
    private void setDefault() {
        mTextViewChoiceCar.setTextColor(getResources().getColor(R.color.colorBlackText));
        mTextViewMonitor.setTextColor(getResources().getColor(R.color.colorBlackText));
        mTextViewWarn.setTextColor(getResources().getColor(R.color.colorBlackText));
        mTextViewSetting.setTextColor(getResources().getColor(R.color.colorBlackText));

        mImageViewChoiceCar.setImageResource(R.drawable.ic_choice_car_gray);
        mImageViewMonitor.setImageResource(R.drawable.ic_monitor_gray);
        mImageViewWarn.setImageResource(R.drawable.ic_warn_gray);
        mImageViewSetting.setImageResource(R.drawable.ic_setting_gray);
    }

    //  显示Fragment
    private void showFragment(Fragment frag) {
        FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
        for (Fragment fragment : mFragmentManager.getFragments()) {
            fragmentTransaction.hide(fragment);
        }
        if (frag.isAdded()) {
            fragmentTransaction.show(frag);
        } else {
            fragmentTransaction.add(R.id.fl_activity_fragment_content, frag);
        }
        fragmentTransaction.commit();
    }

    //  显示选车
    private void showChoiceCar() {
        setDefault();
        mTextViewChoiceCar.setTextColor(getResources().getColor(R.color.colorBlueTheme));
        mImageViewChoiceCar.setImageResource(R.drawable.ic_choice_car_blue);
        showFragment(mChoiceCarFragment);
    }

    //  显示监控
    private void showMonitor() {
        setDefault();
        mTextViewMonitor.setTextColor(getResources().getColor(R.color.colorBlueTheme));
        mImageViewMonitor.setImageResource(R.drawable.ic_monitor_blue);
        showFragment(mMonitorFragment);
    }

    //  显示报警
    private void showWarn() {
        setDefault();
        mTextViewWarn.setTextColor(getResources().getColor(R.color.colorBlueTheme));
        mImageViewWarn.setImageResource(R.drawable.ic_warn_blue);
        showFragment(mWarnFragment);
    }

    //  显示设置
    private void showSetting() {
        setDefault();
        mTextViewSetting.setTextColor(getResources().getColor(R.color.colorBlueTheme));
        mImageViewSetting.setImageResource(R.drawable.ic_setting_blue);
        showFragment(mSettingFragment);
    }
}
