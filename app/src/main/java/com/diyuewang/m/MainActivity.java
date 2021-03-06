package com.diyuewang.m;

import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatRadioButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.common.library.business.FinalHttpRequestCallback;
import com.common.library.model.ResultDto;
import com.common.library.tools.rvhelper.divider.RecycleViewDivider;
import com.diyuewang.m.base.BaseMapActivity;
import com.diyuewang.m.constants.API;
import com.diyuewang.m.constants.Constants;
import com.diyuewang.m.model.MarkerInfoUtil;
import com.diyuewang.m.tools.LogManager;
import com.diyuewang.m.tools.UIUtils;
import com.diyuewang.m.tools.helper.AccountUtil;
import com.diyuewang.m.ui.activity.LoginActivity;
import com.diyuewang.m.ui.adapter.MarketAdapter;
import com.diyuewang.m.ui.dialog.SelectPopupWindow;
import com.diyuewang.m.ui.dialog.commonDialog.LoadingDialogClass;
import com.diyuewang.m.ui.dialog.simpledialog.DialogUtils;
import com.diyuewang.m.ui.dialog.simpledialog.SimpleDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;
import okhttp3.Headers;
import okhttp3.Response;

public class MainActivity extends BaseMapActivity implements View.OnClickListener, SelectPopupWindow.WheelViewListener, MarketAdapter.RemoveMarketListener, RadioGroup.OnCheckedChangeListener {

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

    //镇名称
    @BindView(R.id.edt_town_content)
    AppCompatEditText edt_town_content;

    //村名称
    @BindView(R.id.edt_village_content)
    AppCompatEditText edt_village_content;

    //手机号
    @BindView(R.id.edt_phone_content)
    AppCompatEditText edt_phone_content;

    //手机号
    @BindView(R.id.edt_name_content)
    AppCompatEditText edt_name_content;

    //经纬度个数
    @BindView(R.id.iv_add)
    ImageView iv_add;

    //经纬度个数
    @BindView(R.id.tv_count)
    TextView tv_count;

    //面积
    @BindView(R.id.tv_total_size)
    TextView tv_total_size;

    //经纬度个数
    @BindView(R.id.rv_view)
    RecyclerView rv_view;

    @BindView(R.id.RadioGroup)
    RadioGroup RadioGroup;

    @BindView(R.id.RadioGroup_right)
    RadioGroup RadioGroup_right;

    @BindView(R.id.iv_logout)
    ImageView ivLogOUt;

    @BindView(R.id.iv_input)
    ImageView ivInput;


    private MarketAdapter adapter;

    private long firstTime = 1;

    private SelectPopupWindow menuWindow;
    private String country;
    private String province;
    private String city;
    private String district;

    private int type = -1;//0:托管;1：流转

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initStatusBar(true);
        initView();
        initMap();
        initAdapter();
    }

    @Override
    protected void getOverlayArea(double area,double area2, boolean isHave) {
        if (isHave) {
            tv_total_size.setVisibility(View.VISIBLE);
            tv_total_size.setText("所选区域面积：" + area + "亩" + "\n算法2计算面积：" + area2 + "亩");// + "\n算法2计算面积：" + area2 + "亩"
        } else {
            tv_total_size.setVisibility(View.GONE);
        }
    }

    private void initAdapter() {
        rv_view.setNestedScrollingEnabled(false);
        rv_view.setLayoutManager(new LinearLayoutManager(activity));
        rv_view.addItemDecoration(new RecycleViewDivider(activity, LinearLayoutManager.HORIZONTAL, R.drawable.line_diver));
        adapter = new MarketAdapter(this);
        adapter.setRemoveMarketListener(this);
        rv_view.setAdapter(adapter);
    }

    @Override
    protected void setAdress(BDLocation location) {
        country = location.getCountry();//获取国家
        province = location.getProvince();//获取省份
        city = location.getCity();//获取城市
        district = location.getDistrict();//获取区县
        tv_location_content.setText(city + " " + district);
    }

    @Override
    protected void addMarket(BDLocation location) {
        setOverlay();
        overlaySize();
        setLocationCount();
        mCurrentLocation = null;
    }

    private void initMap() {
        mBaiduMap = mMapView.getMap();
        //控制地图中放大缩小的按钮的位置
        mBaiduMap.setOnMapLoadedCallback(new BaiduMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                mMapView.setZoomControlsPosition(new Point(20, 20));
            }
        });

        //添加marker点击事件的监听
        /*mBaiduMap.setOnMarkerClickListener(new BaiduMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                //从marker中获取info信息
                Bundle bundle = marker.getExtraInfo();
                MarkerInfoUtil markerInfo = (MarkerInfoUtil) bundle.getSerializable("info");
                showDelMarketDialog(markerInfo,marker);
                return true;
            }
        });*/

        //权限控制
        if (Build.VERSION.SDK_INT >= 23) {
            showContacts();
        } else {
            initLocation();
        }

    }

    private void initView() {

        ivLogOUt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showLogOUtDialog(true);
            }
        });
        ivInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIputMarketDialog();
            }
        });

        mMapView = this.findViewById(R.id.bmapView);
        rl_select_type.setOnClickListener(this);
        tv_count.setOnClickListener(this);
        iv_add.setOnClickListener(this);
        RadioGroup.setOnCheckedChangeListener(this);
        RadioGroup_right.setOnCheckedChangeListener(this);

//        setToolBarLeftTitleColor(R.color.colorAccent);
        setToolBarRightTitleColor(R.color.colorAccent);
        initToolBarRightTxt(UIUtils.getString(R.string.title_main), "发布", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canSubmit()) {
                    submit();
                }
            }
        }, false);
        /*initToolBarLeftRightTxt(UIUtils.getString(R.string.title_main), "注销", "发布", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountUtil.logOut(MainActivity.this);
            }
        }, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(canSubmit()){
                    submit();
                }
            }
        });*/
    }

    private void setLocationCount() {
        if (locationInfoList != null && locationInfoList.size() > 0) {
            tv_count.setVisibility(View.VISIBLE);
            tv_count.setText("清除已添加的" + locationInfoList.size() + "个点");
        } else {
            tv_count.setVisibility(View.GONE);
        }
        adapter.clearData();
        adapter.addDataAll(locationInfoList);
        adapter.notifyDataSetChanged();
    }

    /**
     * 发布
     */
    private void submit() {
        LoadingDialogClass.showLodDialog(MainActivity.this, "发布中...");
        RequestParams params = AccountUtil.getRequestParams(activity);
        params.addFormDataPart("area", edt_size_content.getText().toString());
        params.addFormDataPart("businessType", type);
        params.addFormDataPart("title", AccountUtil.getEmployee().title);
        params.addFormDataPart("country", country);
        params.addFormDataPart("province", province);
        params.addFormDataPart("city", city);
        params.addFormDataPart("county", district);
        params.addFormDataPart("employeeId", AccountUtil.getUserId());
        params.addFormDataPart("points", getLocationInfo());
        params.addFormDataPart("town", edt_town_content.getText().toString());
        params.addFormDataPart("village", edt_village_content.getText().toString());
        params.addFormDataPart("userPhone", edt_phone_content.getText().toString());
        params.addFormDataPart("userName", edt_name_content.getText().toString());
        HttpRequest.post(API.EMPLOYEE_SEND, params, new FinalHttpRequestCallback<ResultDto>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onResponse(Response httpResponse, String response, Headers headers) {
                super.onResponse(httpResponse, response, headers);
                LogManager.d("EMPLOYEE_SEND", response);
            }

            @Override
            protected void onRespSuccess(ResultDto resp) {
                super.onRespSuccess(resp);
                try {
                    if (resp.code == Constants.REQ_RESPOSE_CODE) {
                        LoadingDialogClass.closeLodDialog();
                        resetData();
                        UIUtils.showToastInCenter(resp.msg);
                    } else {
                        UIUtils.showToastInCenter(resp.msg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onRespFailure(int errorCode, String msg) {
                super.onRespFailure(errorCode, msg);
                LoadingDialogClass.closeLodDialog();
                UIUtils.showToastInCenter("请求异常！");
            }
        });
    }

    private void resetData() {
        locationInfoList.clear();
        mBaiduMap.clear();
        overlaySize();
        setLocationCount();
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
        //0:托管;1：流转
        if ("托管".equals(content)) {
            type = 0;
        } else {
            type = 1;
        }
        tv_select_type_content.setText(content);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_select_type:
                menuWindow = new SelectPopupWindow(activity, rlt_root, this);
                break;
            case R.id.tv_count:
                showResetDialog();
                break;
            case R.id.iv_add:
                if (!UIUtils.isFastChangeClick()) {
                    showAddMarketDialog();
//                    LoadingDialogClass.showLodDialog(activity, "GPS信号接收中，请稍等...");
                    addMarking = true;
                }
                break;
        }
    }

    private boolean canSubmit() {
        if (StringUtils.isEmpty(country) || StringUtils.isEmpty(province) || StringUtils.isEmpty(city) || StringUtils.isEmpty(district)) {
            UIUtils.showToastInCenter("定位失败，无法发布！");
            return false;
        }
        String townContent = edt_town_content.getText().toString();
        String townVillage = edt_village_content.getText().toString();
        if (StringUtils.isEmpty(townContent) || StringUtils.isEmpty(townVillage)) {
            UIUtils.showToastInCenter("镇&村名称不能为空！");
            return false;
        }
        if (type == -1) {
            UIUtils.showToastInCenter("未选择流转方式！");
            return false;
        }

        String sizeContent = edt_size_content.getText().toString();
        if (StringUtils.isEmpty(sizeContent)) {
            UIUtils.showToastInCenter("土地面积不能为空！");
            return false;
        }
        String phoneContent = edt_phone_content.getText().toString();
        if (StringUtils.isEmpty(phoneContent)) {
            UIUtils.showToastInCenter("手机号不能为空！");
            return false;
        }
        String nameContent = edt_name_content.getText().toString();
        if (StringUtils.isEmpty(nameContent)) {
            UIUtils.showToastInCenter("姓名不能为空！");
            return false;
        }
        if (locationInfoList == null || locationInfoList.size() < 3) {
            UIUtils.showToastInCenter("经纬度至少添加三个！");
            return false;
        }
        return true;
    }

    /**
     * 获取打点信息
     */
    private String getLocationInfo() {
        String content = "";
        if (locationInfoList != null && locationInfoList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < locationInfoList.size(); i++) {
                String singleContent = locationInfoList.get(i).getLatitude() + "," + locationInfoList.get(i).getLongitude();
                sb.append(singleContent);
                if (i != locationInfoList.size() - 1) {
                    sb.append("\r\n");
                }
            }
            content = sb.toString();
        }
        return content;
    }

    protected void showResetDialog() {
        DialogUtils.getInstance().initSimpleDialog(activity, true)
                .setTitle(UIUtils.getString(R.string.dialog_reset_title))
                .setContent("当前添加的经纬度将被清除，您确定要重置经纬度吗？")
                .setSureText(UIUtils.getString(R.string.dialog_sure))
                .setCanceledOnTouchOutside(false)
//                .setCancelable(false)
                .setOnSimpleDialogClick(new SimpleDialog.OnSimpleDialogClick() {
                    @Override
                    public void onSure() {
                        resetData();
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }

    protected void showDelMarketDialog(final MarkerInfoUtil markerInfo, final Marker marker) {
        DialogUtils.getInstance().initSimpleDialog(activity, true)
                .setTitle(UIUtils.getString(R.string.dialog_del_market_title))
                .setContent("当前要删除的经纬度为:【" + markerInfo.getLatitude() + "," + markerInfo.getLongitude() + "】")
                .setSureText(UIUtils.getString(R.string.dialog_sure))
                .setCanceledOnTouchOutside(false)
//                .setCancelable(false)
                .setOnSimpleDialogClick(new SimpleDialog.OnSimpleDialogClick() {
                    @Override
                    public void onSure() {
                        locationInfoList.remove(markerInfo);
                        marker.remove();
                        overlaySize();
                        setLocationCount();
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }
    protected void showIputMarketDialog() {
        View content = LayoutInflater.from(activity).inflate(R.layout.dialog_input_market, null);
        final AppCompatEditText edtJingdu = content.findViewById(R.id.edt_jingdu);
        final AppCompatEditText edtWeidu = content.findViewById(R.id.edt_weidu);
        DialogUtils.getInstance().initSimpleDialog(activity, true)
                .setTitle(UIUtils.getString(R.string.dialog_input_market_title))
                .setView(content)
                .setSureText(UIUtils.getString(R.string.dialog_sure))
                .setCanceledOnTouchOutside(false)
//                .setCancelable(false)
                .setOnSimpleDialogClick(new SimpleDialog.OnSimpleDialogClick() {
                    @Override
                    public void onSure() {
                        String jingdu = edtJingdu.getText().toString().trim();
                        String weidu = edtWeidu.getText().toString().trim();
                        if(!StringUtils.isEmpty(jingdu) && !StringUtils.isEmpty(weidu)){
                            double longitude = Double.parseDouble(jingdu);
                            double latitude = Double.parseDouble(weidu);
                            BDLocation location = new BDLocation();
                            location.setLatitude(latitude);
                            location.setLongitude(longitude);
                            mCurrentLocation = location;
                            addMarket(location);
                        }else{
                            UIUtils.showToastInCenter("经纬度信息不能为空！");
                        }
                    }

                    @Override
                    public void onCancel() {
                    }
                }).show();
    }

    @Override
    public void removeMarekt(MarkerInfoUtil markerInfoUtil, Marker marker) {
        showDelMarketDialog(markerInfoUtil, marker);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.normal:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
            case R.id.statellite:
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case R.id.FOLLOWING:
                changeLocationMode(MyLocationConfiguration.LocationMode.NORMAL);
                break;
            case R.id.COMPASS:
                changeLocationMode(MyLocationConfiguration.LocationMode.FOLLOWING);
                break;
        }
    }
}
