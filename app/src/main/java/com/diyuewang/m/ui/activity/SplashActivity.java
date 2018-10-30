package com.diyuewang.m.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

import com.diyuewang.m.R;
import com.diyuewang.m.tools.helper.AccountUtil;
import com.diyuewang.m.tools.helper.IntentManager;


/**
 *  启动页
 */
public class SplashActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 2000;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);

        handler = new Handler();
        // 延迟SPLASH_DISPLAY_LENGHT时间然后跳转到MainActivity
        handler.postDelayed(new Runnable() {

            @Override
            public void run() {
                /*if (SharePreferencesUtils.isFirstStarted()) {
                    IntentManager.startGuideActivity(SplashActivity.this);
                } else {
                    AccountHelper.startApp(SplashActivity.this);
                }*/
                AccountUtil.startApp(SplashActivity.this);
//                IntentManager.startMainActivity(SplashActivity.this);
                SplashActivity.this.finish();
            }
        }, SPLASH_DISPLAY_LENGHT);

    }

    /**
     * 禁止返回事件
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
