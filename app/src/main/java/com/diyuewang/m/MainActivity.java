package com.diyuewang.m;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.mapapi.map.MapView;
import com.diyuewang.m.base.BaseToolBarActivity;
import com.diyuewang.m.ui.dialog.SelectPopupWindow;
import com.diyuewang.m.tools.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseToolBarActivity implements View.OnClickListener, SelectPopupWindow.WheelViewListener {

    //流转区域视图
    @BindView(R.id.rl_location)
    RelativeLayout rl_location;

    //流转区域内容
    @BindView(R.id.tv_location_content)
    TextView tv_location_content;

    //流转方式视图
    @BindView(R.id.rl_select_type)
    RelativeLayout rl_select_type;

    //流转方式内容
    @BindView(R.id.tv_select_type_content)
    TextView tv_select_type_content;

    //流转面积
    @BindView(R.id.edt_size_content)
    AppCompatEditText edt_size_content;

    @BindView(R.id.rlt_root)
    RelativeLayout rlt_root;

    @BindView(R.id.bmapView)
    MapView mMapView;

    private long firstTime = 1;

    private SelectPopupWindow menuWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initStatusBar(true);
        initToolBar(getString(R.string.title_main),false);
        initView();
    }

    private void initView() {
        rl_select_type.setOnClickListener(this);
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

    @Override
    public void getContent(String content) {
        tv_select_type_content.setText(content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_select_type:
                menuWindow = new SelectPopupWindow(activity, rlt_root,this);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }
}
