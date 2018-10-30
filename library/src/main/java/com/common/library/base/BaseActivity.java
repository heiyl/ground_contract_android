package com.common.library.base;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.common.library.R;
import com.common.library.tools.ActivityStackManager;
import com.common.library.tools.status_bar.StatusBarUtil;


/**
 * 这是我们所有 Activity 的基类，好处是只要改了这个类，所有 Activity 的风格就都统一改了。不能直接把这个类当 Activity 用。
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected String TAG;
    protected Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        activity = this;
        ActivityStackManager.getInstance().addActivity(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制竖屏

    }

    /**
     * 控制状态栏样式
     * @param style
     */
    protected void initStatusBar(boolean style) {
        if(style){
            StatusBarUtil.transparencyBar(this);
            StatusBarUtil.setStatusBarColor(this, R.color.white);
            StatusBarUtil.StatusBarLightMode(this, StatusBarUtil.StatusBarLightMode(this));
        }else{
            StatusBarUtil.transparencyBar(this);
            StatusBarUtil.setStatusBarColor(this, android.R.color.transparent);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityStackManager.getInstance().removeActivity(this);
    }

    protected void back() {
        if (activity != null) {
            activity.finish();
        }
    }

    @Override
    public void finish() {
        KeyBoardCancle();
        super.finish();
        //解决退出动画无效问题解决退出动画无效问题
        overridePendingTransition(R.anim.slide_right_in, R.anim.slide_left_out);
    }

    public void KeyBoardCancle() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

}
