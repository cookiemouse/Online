package com.tianyigps.online.base;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tianyigps.online.R;
import com.tianyigps.online.fragment.LoadingDialogFragment;

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    private LinearLayout mLinearLayoutAll;

    private FrameLayout mFrameLayout;

    private TextView mTextViewTitle;
    private ImageView mImageViewLeft, mImageViewRight;
    private LinearLayout mLinearLayoutTitleAll;

    private OnTitleRightClickListener mOnTitleRightClickListener;
    private OnTitleBackClickListener mOnTitleBackClickListener;

    //  LoadingFragment
    private LoadingDialogFragment mLoadingDialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base);

        //透明状态栏
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().hide();
//        }
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        mLoadingDialogFragment = new LoadingDialogFragment();

        mLinearLayoutAll = (LinearLayout) findViewById(R.id.activity_base);

        mFrameLayout = (FrameLayout) findViewById(R.id.fl_activity_base_content);

        mImageViewLeft = (ImageView) findViewById(R.id.iv_layout_title_base_left);
        mImageViewRight = (ImageView) findViewById(R.id.iv_layout_title_base_right);
        mTextViewTitle = (TextView) findViewById(R.id.tv_layout_title_base_middle);
        mLinearLayoutTitleAll = (LinearLayout) findViewById(R.id.ll_layout_title_base_all);

        this.setTitleBackground(R.color.colorBlueTheme);
        this.setTitleRightButtonVisibilite(false);
        mImageViewLeft.setImageResource(R.drawable.ic_back);

        mImageViewLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnTitleRightClickListener) {
                    onBackPressed();
                    return;
                }
                mOnTitleBackClickListener.onClick();
            }
        });

        mImageViewRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null == mOnTitleRightClickListener) {
                    return;
                }
                mOnTitleRightClickListener.onClick();
            }
        });
    }

    @Override
    public void setContentView(int layoutResID) {
        mFrameLayout.removeAllViews();
        View.inflate(this, layoutResID, mFrameLayout);
        onContentChanged();
    }

    @Override
    public void setContentView(View view) {
        mFrameLayout.removeAllViews();
        mFrameLayout.addView(view);
        onContentChanged();
    }

    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        mFrameLayout.removeAllViews();
        mFrameLayout.addView(view, params);
        onContentChanged();
    }

    public interface OnTitleRightClickListener {
        void onClick();
    }

    public interface OnTitleBackClickListener {
        void onClick();
    }

    public void setOnTitleRightClickListener(OnTitleRightClickListener listener) {
        this.mOnTitleRightClickListener = listener;
    }

    public void setOnTitleBackClickListener(OnTitleBackClickListener listener) {
        this.mOnTitleBackClickListener = listener;
    }

    //  设置标题右按钮是否可见
    public void setTitleRightButtonVisibilite(boolean visibilite) {
        if (visibilite) {
            mImageViewRight.setVisibility(View.VISIBLE);
            return;
        }
        mImageViewRight.setVisibility(View.GONE);
    }

    //  设置右按钮图标
    public void setTitleRightButtonResource(int resourceid) {
        mImageViewRight.setImageResource(resourceid);
    }

    //  设置标题内容
    public void setTitleText(String title) {
        mTextViewTitle.setText(title);
    }

    public void setTitleText(int title) {
        mTextViewTitle.setText(getResources().getText(title));
    }

    //  设置标题栏背景
    public void setTitleBackground(int resouceid) {
        mLinearLayoutTitleAll.setBackgroundResource(resouceid);
    }

    //  显示Loading对话框
    public void showLoadingDialog() {
        Log.i(TAG, "showLoadingDialog: ");
        mLoadingDialogFragment.show(getFragmentManager(), "BaseActivity");
    }

    //  取消LoadingDialog
    public void disMissLoadingDialog() {
        Log.i(TAG, "disMissLoadingDialog: ");
        if (mLoadingDialogFragment.isAdded()) {
            mLoadingDialogFragment.dismiss();
        }
    }

}
