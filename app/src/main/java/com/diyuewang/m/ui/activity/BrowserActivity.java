package com.diyuewang.m.ui.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.common.library.tools.status_bar.StatusBarUtil;
import com.common.web_ibrary.customwebview.x5webview.X5WebView;
import com.diyuewang.m.R;
import com.diyuewang.m.base.BaseToolBarActivity;
import com.diyuewang.m.tools.LogManager;
import com.diyuewang.m.tools.helper.AccountUtil;

import java.util.HashMap;
import java.util.Iterator;


/**
 * 官网
 */
public class BrowserActivity extends BaseToolBarActivity {

    //内容显示区域
    private FrameLayout center_layout;
    private X5WebView mX5WebView;
    private ImageView ivLogOUt;
    private String URL = "http://m.diyuewang.com";

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

        //权限控制
        /*if (Build.VERSION.SDK_INT>=23){
            showContacts();
        }*/
        //内容显示区域
        ivLogOUt = (ImageView) findViewById(R.id.iv_logout);

        ivLogOUt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogOUtDialog(false);
            }
        });
        center_layout = (FrameLayout) findViewById(R.id.center_layout);

        mX5WebView = new X5WebView(this, null);
        center_layout.addView(mX5WebView, new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT));
    }

    private void initDatas() {
        mX5WebView.setCanBackPreviousPage(true, BrowserActivity.this);//设置可返回上一页

        HashMap<String, String> params = new HashMap<>();
        params.put("id",AccountUtil.getUserId());
        params.put("phone",AccountUtil.getLoginUserDto().data.user.phone);
        params.put("sex",AccountUtil.getLoginUserDto().data.user.sex + "");
        params.put("name",AccountUtil.getLoginUserDto().data.user.name);
        params.put("photo",AccountUtil.getLoginUserDto().data.user.phone);
        params.put("status",AccountUtil.getLoginUserDto().data.user.status+"");
        String url = getUrl(params);
        LogManager.e(url);
        mX5WebView.loadUrl(url);
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

    private static final int BAIDU_READ_PHONE_STATE =100;

    public void showContacts(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getApplicationContext(),"没有权限,请手动开启定位权限",Toast.LENGTH_SHORT).show();
            // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义）
            ActivityCompat.requestPermissions(activity,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION}, BAIDU_READ_PHONE_STATE);
        }
    }


    //Android6.0申请权限的回调方法
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // requestCode即所声明的权限获取码，在checkSelfPermission时传入
            case BAIDU_READ_PHONE_STATE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 获取到权限，作相应处理（调用定位SDK应当确保相关权限均被授权，否则可能引起定位失败）
                    LogManager.d("heiyulong");
                } else {
                    // 没有获取到权限，做特殊处理
//                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    private String getUrl(HashMap<String, String> params) {
        String url = URL;
        // 添加url参数
        if (params != null) {
            Iterator<String> it = params.keySet().iterator();
            StringBuffer sb = null;
            while (it.hasNext()) {
                String key = it.next();
                String value = params.get(key);
                if (sb == null) {
                    sb = new StringBuffer();
                    sb.append("?");
                } else {
                    sb.append("&");
                }
                sb.append(key);
                sb.append("=");
                sb.append(value);
            }
            url += sb.toString();
        }
        return url;
    }
}
