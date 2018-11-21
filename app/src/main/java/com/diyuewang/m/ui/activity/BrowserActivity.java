package com.diyuewang.m.ui.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.FrameLayout;

import com.common.library.tools.status_bar.StatusBarUtil;
import com.common.web_ibrary.customwebview.x5webview.X5WebView;
import com.diyuewang.m.R;
import com.diyuewang.m.base.BaseToolBarActivity;


/**
 * 官网
 */
public class BrowserActivity extends BaseToolBarActivity {

    //内容显示区域
    private FrameLayout center_layout;
    private X5WebView mX5WebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_x5webview);

        initStatusBar();
        initViews();
        initDatas();
        initEvents();
    }

    private void initStatusBar() {
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.setStatusBarColor(this, R.color.colorAccent);
        StatusBarUtil.StatusBarLightMode(this, StatusBarUtil.StatusBarLightMode(this));
    }

    @Override
    public void onDestroy() {
        if (mX5WebView != null) {
            mX5WebView.removeAllViews();
            mX5WebView.destroy();
        }

        super.onDestroy();
    }


    private void initViews() {
        //内容显示区域
        center_layout = (FrameLayout) findViewById(R.id.center_layout);

        mX5WebView = new X5WebView(this, null);
        center_layout.addView(mX5WebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
    }

    private void initDatas() {
        mX5WebView.setCanBackPreviousPage(true, BrowserActivity.this);//设置可返回上一页

        mX5WebView.loadUrl("http://m.diyuewang.com/");
    }

    private void initEvents() {

    }

    /**
     * 截取返回软键事件【在activity中写，不能在自定义的X5Webview中】
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((keyCode == KeyEvent.KEYCODE_BACK) && mX5WebView.canGoBack()) {
                mX5WebView.goBack();
                return true;
            } else {
                finish();
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
