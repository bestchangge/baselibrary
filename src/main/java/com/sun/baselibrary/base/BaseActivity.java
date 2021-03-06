package com.sun.baselibrary.base;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.classic.common.MultipleStatusView;
import com.sun.baselibrary.R;
import com.sun.baselibrary.databinding.ActivityBaseBinding;
import com.sun.baselibrary.view.loadingview.LoadingDialog;
import com.sun.baselibrary.view.statusbar.StatusBarUtil;

/**
 * Created by sun on 17/08/09.
 */
public abstract class BaseActivity<SV extends ViewDataBinding> extends AppCompatActivity {

    // 布局view
    protected SV bindingView;
    private ActivityBaseBinding mBaseBinding;
    private MultipleStatusView mMultipleStatusView;

    protected <T extends View> T getView(int id) {
        return (T) findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        initView();
        initData();
    }

    protected abstract int getLayoutId();
    public void initView(){}
    public abstract void initData();

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        mBaseBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.activity_base, null, false);
        bindingView = DataBindingUtil.inflate(getLayoutInflater(), layoutResID, null, false);

        // content
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        bindingView.getRoot().setLayoutParams(params);
        RelativeLayout mContainer = (RelativeLayout) mBaseBinding.getRoot().findViewById(R.id.container);
        mContainer.addView(bindingView.getRoot());
        getWindow().setContentView(mBaseBinding.getRoot());

        // 设置透明状态栏
        StatusBarUtil.setColor(this, ContextCompat.getColor(this,R.color.colorTheme), 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        mMultipleStatusView = getView(R.id.multipleStatusView);

        setToolBar();
    }

    /**
     * 设置titlebar
     */

    protected void setToolBar() {
        setSupportActionBar(mBaseBinding.toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //去除默认Title显示
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.icon_back);
        }
        mBaseBinding.toolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) BaseActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                onBackPressed();
            }
        });
    }

    public Toolbar getToolbar() {
        return mBaseBinding.toolBar;
    }

    public void setTitle(CharSequence text) {
//        mBaseBinding.toolBar.setTitle(text);
        mBaseBinding.tvTitle.setText(text);
    }

    protected void showLoading() {
        mMultipleStatusView.showLoading();
    }

    protected void showContent() {
        mMultipleStatusView.showContent();
    }

    protected void showError() {
        mMultipleStatusView.showError();
    }

    protected void showEmpty() {
        mMultipleStatusView.showEmpty();
    }

    protected void showNoNetwork() {
        mMultipleStatusView.showNoNetwork();
    }

    protected MultipleStatusView getmMultipleStatusView(){
        return mMultipleStatusView;
    }

    private LoadingDialog progress;
    protected void dismissProgres() {
        if (progress == null) {
            return;
        }
        if (progress.isShowing()) {
            progress.dismiss();
        }
    }

    protected void showProgress() {
        if (progress == null) {
            progress = LoadingDialog.createProgressDialog(this);
        }
        if (!progress.isShowing()) {
            progress.show();
        }
    }

}
