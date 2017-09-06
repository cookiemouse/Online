package com.tianyigps.online.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.tianyigps.online.R;
import com.tianyigps.online.adapter.WarnAdapter;
import com.tianyigps.online.data.WarnAdapterData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cookiemouse on 2017/9/5.
 */

public class WarnFragment extends Fragment {

    private EditText mEditTextSearch;
    private TextView mTextViewSearch, mTextViewSetup;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private ListView mListView;
    private List<WarnAdapterData> mWarnAdapterDataList;
    private WarnAdapter mWarnAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_warn, container, false);

        init(view);

        setEventListener();

        return view;
    }

    private void init(View view) {
        mEditTextSearch = view.findViewById(R.id.et_fragment_warn_search);
        mTextViewSearch = view.findViewById(R.id.tv_fragment_warn_search);
        mTextViewSetup = view.findViewById(R.id.tv_fragment_warn_setup);
        mSwipeRefreshLayout = view.findViewById(R.id.srl_fragment_warn);
        mListView = view.findViewById(R.id.lv_fragment_warn);

        mSwipeRefreshLayout.setColorSchemeColors(0xff3cabfa);

        mWarnAdapterDataList = new ArrayList<>();
        mWarnAdapter = new WarnAdapter(getContext(), mWarnAdapterDataList);

        for (int i = 0; i < 5; i++) {
            mWarnAdapterDataList.add(new WarnAdapterData("沪KZ7555荣威白有线" + i, "断电报警", "7月19日"));
        }

        mListView.setAdapter(mWarnAdapter);
    }

    private void setEventListener() {
        mTextViewSetup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        mTextViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            }
        });
    }
}
