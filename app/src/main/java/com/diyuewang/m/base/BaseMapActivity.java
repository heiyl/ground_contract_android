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

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationConfiguration.LocationMode;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.common.library.tools.grant.PermissionsManager;
import com.common.library.tools.grant.PermissionsPageManager;
import com.common.library.tools.grant.PermissionsResultAction;
import com.diyuewang.m.R;
import com.diyuewang.m.tools.UIUtils;
import com.diyuewang.m.ui.dialog.simpledialog.DialogUtils;
import com.diyuewang.m.ui.dialog.simpledialog.SimpleDialog;

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
    protected boolean have_permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);//获取传感器管理服务
        mCurrentMode = LocationMode.NORMAL;
    }

    protected void initLocation() {
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(this);
        mLocClient.registerLocationListener(myListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        mLocClient.setLocOption(option);
        mLocClient.start();
        changeLocationMode(MyLocationConfiguration.LocationMode.NORMAL);
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
        super.onDestroy();
    }

    public boolean requestPermissions() {
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResultSupportUnderM(this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_PHONE_STATE
                }, permissionsResultAction);
        return have_permission;
    }

    PermissionsResultAction permissionsResultAction = new PermissionsResultAction() {

        @Override
        public void onGranted() {
            //注意：针对6.0以上设备，必须在手动获得RECORD_AUDIO权限
            have_permission = true;
        }

        @Override
        public void onDenied(String permission) {
            have_permission = false;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

    public boolean checkPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            have_permission = false;
            requestPermissions();
        } else {
            have_permission = true;
        }
        return  have_permission;
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
}


