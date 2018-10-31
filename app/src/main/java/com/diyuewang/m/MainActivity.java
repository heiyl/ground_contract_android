package com.diyuewang.m;

import android.os.Bundle;
import android.view.KeyEvent;

import com.diyuewang.m.base.BaseToolBarActivity;
import com.diyuewang.m.tools.UIUtils;

import butterknife.ButterKnife;

public class MainActivity extends BaseToolBarActivity {

    private long firstTime = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initStatusBar(true);
        initToolBar(getString(R.string.title_main),false);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //双击退出
            long secondTime = System.currentTimeMillis();
            if (secondTime - firstTime > 1000) {
                UIUtils.showToast(getString(R.string.toast_clickExit));
                firstTime = secondTime;
                return true;
            } else {
                return super.onKeyUp(keyCode, event);
            }
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        back();
        overridePendingTransition(0, 0);
    }
}
