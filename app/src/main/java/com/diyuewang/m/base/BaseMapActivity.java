package com.diyuewang.m.base;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.common.library.tools.grant.PermissionsPageManager;
import com.diyuewang.m.R;
import com.diyuewang.m.model.LocationInfo;
import com.diyuewang.m.tools.LogManager;
import com.diyuewang.m.tools.UIUtils;
import com.diyuewang.m.ui.dialog.simpledialog.DialogUtils;
import com.diyuewang.m.ui.dialog.simpledialog.SimpleDialog;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMapActivity extends BaseToolBarActivity implements SensorEventListener {

    // 定位相关
    protected LocationClient mLocClient;
    protected final MyLocationListenner myListener = new MyLocationListenner();
    protected LocationMode mCurrentMode;
    protected BitmapDescriptor mCurrentMarker;
    protected static final int accuracyCircleFillColor = 0xAAFFFF88;
    protected static final int accuracyCircleStrokeColor = 0xAA00FF00;
    protected SensorManager mSensorManager;
    protected Double lastX = 0.0;
    protected int mCurrentDirection = 0;
    protected double mCurrentLat = 0.0;
    protected double mCurrentLon = 0.0;
    protected float mCurrentAccracy;

    protected MyLocationData locData;
    protected boolean isFirstLoc = true; // 是否首次定位
    protected MapView mMapView;
    protected BaiduMap mBaiduMap;
    protected List<LocationInfo> locationInfoList = new ArrayList<>();

    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_gcoding);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = LocationMode.NORMAL;
        super.onCreate(savedInstanceState);
    }

    protected void initLocation() {
        initOverLay();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mLocClient.setLocOption(option);
        mLocClient.start();
        changeLocationMode(MyLocationConfiguration.LocationMode.NORMAL);
    }

    protected void initOverLay(){
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
        mBaiduMap.setMapStatus(msu);
    }

    /**
     * 设置覆盖物
     */
    protected void setOverlay(){
        LatLng latLng = null;
        Marker marker;
        MarkerOptions options;

        LocationInfo locationInfo = new LocationInfo();
        locationInfo.mCurrentLat = mCurrentLat;
        locationInfo.mCurrentLon = mCurrentLon;
        locationInfoList.add(locationInfo);

        //获取经纬度
        latLng = new LatLng(mCurrentLat,mCurrentLon);
        //设置marker
        options = new MarkerOptions()
                .position(latLng)//设置位置
                .icon(bd)//设置图标样式
                .zIndex(9) // 设置marker所在层级
                .draggable(true); // 设置手势拖拽;
//        options.animateType(MarkerOptions.MarkerAnimateType.drop);
        options.animateType(MarkerOptions.MarkerAnimateType.grow);
        //添加marker
        marker = (Marker) mBaiduMap.addOverlay(options);


    }

    protected void changeLocationMode(LocationMode mCurrentMode) {
        switch (mCurrentMode) {
            case NORMAL:
                //跟随
                mCurrentMode = MyLocationConfiguration.LocationMode.FOLLOWING;
                mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                        mCurrentMode, true, mCurrentMarker));
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.overlook(0);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
                break;
            case COMPASS:
                //普通
                mCurrentMode = MyLocationConfiguration.LocationMode.NORMAL;
                mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                        mCurrentMode, true, mCurrentMarker));
                MapStatus.Builder builder1 = new MapStatus.Builder();
                builder1.overlook(0);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder1.build()));
                break;
            case FOLLOWING:
                //罗盘
                mCurrentMode = MyLocationConfiguration.LocationMode.COMPASS;
                mBaiduMap.setMyLocationConfiguration(new MyLocationConfiguration(
                        mCurrentMode, true, mCurrentMarker));
                break;
            default:
                break;
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        double x = sensorEvent.values[SensorManager.DATA_X];
        if (Math.abs(x - lastX) > 1.0) {
            mCurrentDirection = (int) x;
            locData = new MyLocationData.Builder()
                    .accuracy(mCurrentAccracy)
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(mCurrentLat)
                    .longitude(mCurrentLon).build();
            mBaiduMap.setMyLocationData(locData);
        }
        lastX = x;

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    /**
     * 定位SDK监听函数
     */
    private class MyLocationListenner implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // map view 销毁后不在处理新接收的位置
            if (location == null || mMapView == null) {
                return;
            }
            mCurrentLat = location.getLatitude();
            mCurrentLon = location.getLongitude();
            mCurrentAccracy = location.getRadius();
            locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(mCurrentDirection).latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();
            mBaiduMap.setMyLocationData(locData);
            if (isFirstLoc) {
                isFirstLoc = false;
                setAdress(location);

                LatLng ll = new LatLng(location.getLatitude(),
                        location.getLongitude());
                MapStatus.Builder builder = new MapStatus.Builder();
                builder.target(ll).zoom(18.0f);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(builder.build()));
            }
        }

        public void onReceivePoi(BDLocation poiLocation) {
        }
    }

    protected abstract void setAdress(BDLocation location);

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        //为系统的方向传感器注册监听器
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_UI);
    }

    @Override
    protected void onStop() {
        //取消注册传感器监听
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        // 退出时销毁定位
        mLocClient.stop();
        // 关闭定位图层
        mBaiduMap.setMyLocationEnabled(false);
        mMapView.onDestroy();
        mMapView = null;
        bd.recycle();
        super.onDestroy();
    }


    private void showPermissionDialog(){
        DialogUtils.getInstance().initSimpleDialog(activity, false)
                .setTitle(UIUtils.getString(R.string.dialog_permission_title))
                .setContent("权限请求失败，是否去设置界面中打开地约的权限？")
                .setSureText(UIUtils.getString(R.string.dialog_set))
                .setCanceledOnTouchOutside(false)
                .setCancelable(false)
                .setOnSimpleDialogClick(new SimpleDialog.OnSimpleDialogClick() {
                    @Override
                    public void onSure() {
                        try {
                            Intent intent = PermissionsPageManager.getIntent(activity);
                            activity.startActivity(intent);
                        } catch (Exception e) {
                            Intent settingIntent = PermissionsPageManager.getSettingIntent(activity);
                            activity.startActivity(settingIntent);
                        }
                    }
                    @Override
                    public void onCancel() {
                    }
                }).show();
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
        }else{
            initLocation();
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
                    initLocation();
                } else {
                    // 没有获取到权限，做特殊处理
                    initLocation();
//                    Toast.makeText(getApplicationContext(), "获取位置权限失败，请手动开启", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

}


