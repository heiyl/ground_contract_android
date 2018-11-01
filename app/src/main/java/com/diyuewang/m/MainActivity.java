package com.diyuewang.m;

import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.diyuewang.m.base.BaseMapActivity;
import com.diyuewang.m.tools.UIUtils;
import com.diyuewang.m.ui.dialog.SelectPopupWindow;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseMapActivity implements View.OnClickListener, SelectPopupWindow.WheelViewListener {

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
    LinearLayout rlt_root;

    private long firstTime = 1;

    private SelectPopupWindow menuWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initStatusBar(true);
        initView();
        initMap();
    }

    @Override
    protected void setAdress(BDLocation location) {
        String country = location.getCountry();//获取国家
        String province = location.getProvince();//获取省份
        String city = location.getCity();//获取城市
        String district = location.getDistrict();//获取区县
        String street = location.getStreet();//获取街道信息
        tv_location_content.setText(country + " " + province + " " + city + " " + district);
    }

    private void initMap() {
        mBaiduMap = mMapView.getMap();
        initLocation();

    }

    private void initView() {
        mMapView = this.findViewById(R.id.bmapView);
        rl_select_type.setOnClickListener(this);

        initToolBarLeftRightTxt(UIUtils.getString(R.string.title_main), "添加经纬度", "发布", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showToast("添加经纬度");
                setOverlay();
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showToast("发布");
            }
        });
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
}
