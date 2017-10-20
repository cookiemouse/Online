package com.tianyigps.online.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableRow;

import com.tianyigps.online.R;
import com.tianyigps.online.activity.AboutActivity;
import com.tianyigps.online.activity.FlushTimeActivity;
import com.tianyigps.online.activity.InstructionActivity;
import com.tianyigps.online.activity.LoginActivity;
import com.tianyigps.online.activity.OpinionActivity;
import com.tianyigps.online.manager.SharedManager;

/**
 * Created by cookiemouse on 2017/9/5.
 */

public class SettingFragment extends Fragment {

    private TableRow mTableRowFlushTime, mTableRowOpinion, mTableRowInstruction, mTableRowAbout;
    private TableRow mTableRowPage;
    private ImageView mImageViewMonitor, mImageViewCarList;
    private Button mButtonExit;
    private LinearLayout mLinearLayoutPage;
    private ImageView mImageViewPage;

    private SharedManager mSharedManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_setting, container, false);

        init(view);

        setEventListener();

        return view;
    }

    private void init(View view) {
        mTableRowFlushTime = view.findViewById(R.id.tr_fragment_set_flush_time);
        mTableRowOpinion = view.findViewById(R.id.tr_fragment_opinion);
        mTableRowInstruction = view.findViewById(R.id.tr_fragment_instructions);
        mTableRowAbout = view.findViewById(R.id.tr_fragment_about);
        mTableRowPage = view.findViewById(R.id.tr_fragment_set_main_page);
        mImageViewMonitor = view.findViewById(R.id.iv_fragment_set_monitor);
        mImageViewCarList = view.findViewById(R.id.iv_fragment_set_car_list);

        mImageViewPage = view.findViewById(R.id.iv_fragment_setting_page);
        mLinearLayoutPage = view.findViewById(R.id.ll_fragment_setting);

        mButtonExit = view.findViewById(R.id.btn_fragment_setting);

        mSharedManager = new SharedManager(getContext());

        int mainPage = mSharedManager.getMainPage();
        if (mainPage == 1) {
            mImageViewMonitor.setSelected(true);
            mImageViewCarList.setSelected(false);
        } else {
            mImageViewMonitor.setSelected(false);
            mImageViewCarList.setSelected(true);
        }
    }

    private void setEventListener() {
        mTableRowFlushTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FlushTimeActivity.class);
                startActivity(intent);
            }
        });

        mTableRowPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mLinearLayoutPage.getVisibility() == View.VISIBLE) {
                    mImageViewPage.setSelected(false);
                    mLinearLayoutPage.setVisibility(View.GONE);
                } else {
                    mImageViewPage.setSelected(true);
                    mLinearLayoutPage.setVisibility(View.VISIBLE);
                }
            }
        });

        mImageViewMonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedManager.saveMainPage(1);
                mImageViewMonitor.setSelected(true);
                mImageViewCarList.setSelected(false);
            }
        });

        mImageViewCarList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSharedManager.saveMainPage(0);
                mImageViewMonitor.setSelected(false);
                mImageViewCarList.setSelected(true);
            }
        });

        mTableRowOpinion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), OpinionActivity.class);
                startActivity(intent);
            }
        });

        mTableRowInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), InstructionActivity.class);
                startActivity(intent);
            }
        });

        mTableRowAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), AboutActivity.class);
                startActivity(intent);
            }
        });

        mButtonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showChoiceDialog("账号退出后，您将接收不到报警信息的念头，是否退出。");
            }
        });
    }

    //  显示选择对话框
    public void showChoiceDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(msg);
        builder.setPositiveButton(R.string.ensure, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mSharedManager.saveAutoLogin(false);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                getActivity().finish();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //  do nothing
            }
        });
        builder.create().show();
    }
}
