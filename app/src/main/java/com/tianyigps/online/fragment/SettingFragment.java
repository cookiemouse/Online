package com.tianyigps.online.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableRow;

import com.tianyigps.online.R;
import com.tianyigps.online.activity.AboutActivity;
import com.tianyigps.online.activity.FlushTimeActivity;
import com.tianyigps.online.activity.InstructionActivity;
import com.tianyigps.online.activity.OpinionActivity;

/**
 * Created by cookiemouse on 2017/9/5.
 */

public class SettingFragment extends Fragment {

    private TableRow mTableRowFlushTime, mTableRowOpinion, mTableRowInstruction, mTableRowAbout;

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
    }

    private void setEventListener() {
        mTableRowFlushTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), FlushTimeActivity.class);
                startActivity(intent);
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
    }
}
