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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDAbstractLocationListener;
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
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polygon;
import com.baidu.mapapi.map.PolygonOptions;
import com.baidu.mapapi.map.Stroke;
import com.baidu.mapapi.model.LatLng;
import com.common.library.tools.grant.PermissionsPageManager;
import com.diyuewang.m.R;
import com.diyuewang.m.model.MarkerInfoUtil;
import com.diyuewang.m.tools.LogManager;
import com.diyuewang.m.tools.UIUtils;
import com.diyuewang.m.tools.helper.BDMapTools;
import com.diyuewang.m.ui.dialog.simpledialog.DialogUtils;
import com.diyuewang.m.ui.dialog.simpledialog.SimpleDialog;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseMapActivity extends BaseToolBarActivity implements SensorEventListener {

    // 定位相关
    protected LocationClient mLocClient;
//    protected final MyLocationListenner myListener = new MyLocationListenner();
    protected final MyBdLocationListenner myListener = new MyBdLocationListenner();
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
    protected List<MarkerInfoUtil> locationInfoList = new ArrayList<>();

    BitmapDescriptor bd = BitmapDescriptorFactory
            .fromResource(R.mipmap.icon_gcoding);
    private Polygon polygon;

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
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setOpenGps(true); // 打开gps
        option.setPriority(LocationClientOption.GpsFirst); //设置gps优先
        option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
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
        String name = (locationInfoList.size() + 1) + "";

        MarkerInfoUtil locationInfo = new MarkerInfoUtil();
        locationInfo.setLatitude(mCurrentLat);
        locationInfo.setLongitude(mCurrentLon);
        locationInfo.setName(name);

//        bd = getOverIcon(name);


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
        Bundle bundle = new Bundle();
        //info必须实现序列化接口
        bundle.putSerializable("info", locationInfo);
        marker.setExtraInfo(bundle);

        locationInfo.setMarker(marker);
        locationInfoList.add(locationInfo);


    }

    /**
     * 添加区域
     */
    protected void overlaySize(){
        List<LatLng> pts = new ArrayList<LatLng>();
        if(locationInfoList != null && locationInfoList.size() >= 3){
            removeOverlaySize();
            for (int i = 0; i < locationInfoList.size(); i++){
                LatLng pt = new LatLng(locationInfoList.get(i).getLatitude(), locationInfoList.get(i).getLongitude());
                pts.add(pt);
            }
            //构建用户绘制多边形的Option对象
            OverlayOptions polygonOption = new PolygonOptions()
                    .points(pts)
                    .stroke(new Stroke(5, 0xAA00FF00))
                    .fillColor(0xAAFFFF00);

            //在地图上添加多边形Option，用于显示
            polygon = (Polygon) mBaiduMap.addOverlay(polygonOption);
            double area = BDMapTools.getTotalArea(pts);
            double size = area / 667;
            double finalSize = Math.abs(size);
            UIUtils.showToastInCenter("所选区域面积："+finalSize +"亩");
            getOverlayArea(finalSize,true);
        }else{
            getOverlayArea(0,false);
            removeOverlaySize();
        }

    }

    protected abstract void getOverlayArea(double area,boolean isHave);

    private void removeOverlaySize() {
        if(polygon != null){
            polygon.remove();
            polygon = null;
        }
    }

    private BitmapDescriptor getOverIcon(String name){
        View markerView = LayoutInflater.from(this).inflate(R.layout.layout_market, null);
        TextView tv_name = (TextView) markerView.findViewById(R.id.tv_name);
        tv_name.setText(name);
        BitmapDescriptor bd = BitmapDescriptorFactory.fromView(markerView);
        return bd;

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
    /**
     * 定位SDK监听函数
     */
    private class MyBdLocationListenner extends BDAbstractLocationListener {

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
//            getLocationType(location);
        }
    }

    private void getLocationType(BDLocation location) {
        if (location.getLocType() == BDLocation.TypeGpsLocation){

            UIUtils.showToastInCenter("GPS: 卫星数为" + location.getSatelliteNumber());
            //当前为GPS定位结果，可获取以下信息
            location.getSpeed();    //获取当前速度，单位：公里每小时
            location.getSatelliteNumber();    //获取当前卫星数
            location.getAltitude();    //获取海拔高度信息，单位米
            location.getDirection();    //获取方向信息，单位度

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){
            UIUtils.showToastInCenter("NET: 运营商为" + location.getOperators());
            //当前为网络定位结果，可获取以下信息
            location.getOperators();    //获取运营商信息

        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {
            UIUtils.showToastInCenter("NET: TypeOffLineLocation");
            //当前为网络定位结果

        } else if (location.getLocType() == BDLocation.TypeServerError) {
            UIUtils.showToastInCenter("ERROR: 当前网络定位失败");
            //当前网络定位失败
            //可将定位唯一ID、IMEI、定位失败时间反馈至loc-bugs@baidu.com

        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
            UIUtils.showToastInCenter("EXCEPTION: 当前网络不通");
            //当前网络不通

        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
            UIUtils.showToastInCenter("EXCEPTION: 可能是用户没有授权");
            //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
            //可进一步参考onLocDiagnosticMessage中的错误返回码

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

    /**
     * 计算多边形面积
     * @return
     */
    protected double getArea(List<LatLng> pts) {
        double totalArea = 0;// 初始化总面积
        double LowX = 0.0;
        double LowY = 0.0;
        double MiddleX = 0.0;
        double MiddleY = 0.0;
        double HighX = 0.0;
        double HighY = 0.0;
        double AM = 0.0;
        double BM = 0.0;
        double CM = 0.0;
        double AL = 0.0;
        double BL = 0.0;
        double CL = 0.0;
        double AH = 0.0;
        double BH = 0.0;
        double CH = 0.0;
        double CoefficientL = 0.0;
        double CoefficientH = 0.0;
        double ALtangent = 0.0;
        double BLtangent = 0.0;
        double CLtangent = 0.0;
        double AHtangent = 0.0;
        double BHtangent = 0.0;
        double CHtangent = 0.0;
        double ANormalLine = 0.0;
        double BNormalLine = 0.0;
        double CNormalLine = 0.0;
        double OrientationValue = 0.0;
        double AngleCos = 0.0;
        double Sum1 = 0.0;
        double Sum2 = 0.0;
        double Count2 = 0;
        double Count1 = 0;
        double Sum = 0.0;
        double Radius = 6378137.0;// WGS84椭球半径
        int Count = pts.size();
        //最少3个点
        if (Count  < 3) {
            return 0;
        }
        for ( int i = 0; i < Count; i++) {
            if (i == 0) {
                LowX = pts.get(Count - 1).longitude * Math.PI / 180;
                LowY = pts.get(Count - 1).latitude * Math.PI / 180;
                MiddleX = pts.get(0).longitude * Math.PI / 180;
                MiddleY = pts.get(0).latitude * Math.PI / 180;
                HighX = pts.get(1).longitude * Math.PI / 180;
                HighY = pts.get(1).latitude * Math.PI / 180;
            } else if (i == Count - 1) {
                LowX = pts.get(Count - 2).longitude * Math.PI / 180;
                LowY = pts.get(Count - 2).latitude * Math.PI / 180;
                MiddleX = pts.get(Count - 1).longitude * Math.PI / 180;
                MiddleY = pts.get(Count - 1).latitude * Math.PI / 180;
                HighX = pts.get(0).longitude * Math.PI / 180;
                HighY = pts.get(0).latitude * Math.PI / 180;
            } else {
                LowX = pts.get(i - 1).longitude * Math.PI / 180;
                LowY = pts.get(i - 1).latitude * Math.PI / 180;
                MiddleX = pts.get(i).longitude * Math.PI / 180;
                MiddleY = pts.get(i).latitude * Math.PI / 180;
                HighX = pts.get(i + 1).longitude * Math.PI / 180;
                HighY = pts.get(i + 1).latitude * Math.PI / 180;
            }
            AM = Math.cos(MiddleY) * Math.cos(MiddleX);
            BM = Math.cos(MiddleY) * Math.sin(MiddleX);
            CM = Math.sin(MiddleY);
            AL = Math.cos(LowY) * Math.cos(LowX);
            BL = Math.cos(LowY) * Math.sin(LowX);
            CL = Math.sin(LowY);
            AH = Math.cos(HighY) * Math.cos(HighX);
            BH = Math.cos(HighY) * Math.sin(HighX);
            CH = Math.sin(HighY);
            CoefficientL = (AM * AM + BM * BM + CM * CM)/ (AM * AL + BM * BL + CM * CL);
            CoefficientH = (AM * AM + BM * BM + CM * CM)/ (AM * AH + BM * BH + CM * CH);
            ALtangent = CoefficientL * AL - AM;
            BLtangent = CoefficientL * BL - BM;
            CLtangent = CoefficientL * CL - CM;
            AHtangent = CoefficientH * AH - AM;
            BHtangent = CoefficientH * BH - BM;
            CHtangent = CoefficientH * CH - CM;
            AngleCos = (AHtangent * ALtangent + BHtangent * BLtangent + CHtangent* CLtangent)/ (Math.sqrt(AHtangent * AHtangent + BHtangent* BHtangent

                    + CHtangent * CHtangent) * Math.sqrt(ALtangent * ALtangent + BLtangent* BLtangent + CLtangent * CLtangent));
            AngleCos = Math.acos(AngleCos);
            ANormalLine = BHtangent * CLtangent - CHtangent * BLtangent;
            BNormalLine = 0 - (AHtangent * CLtangent - CHtangent* ALtangent);
            CNormalLine = AHtangent * BLtangent - BHtangent * ALtangent;
            if (AM != 0)
                OrientationValue = ANormalLine / AM;
            else if (BM != 0)
                OrientationValue = BNormalLine / BM;
            else
                OrientationValue = CNormalLine / CM;
            if (OrientationValue > 0) {
                Sum1 += AngleCos;
                Count1++;
            } else {
                Sum2 += AngleCos;
                Count2++;
            }
        }

        double tempSum1, tempSum2;
        tempSum1 = Sum1 + (2 * Math.PI * Count2 - Sum2);
        tempSum2 = (2 * Math.PI * Count1 - Sum1) + Sum2;
        if (Sum1 > Sum2) {
            if ((tempSum1 - (Count - 2) * Math.PI) < 1)
                Sum = tempSum1;
            else
                Sum = tempSum2;
        } else {
            if ((tempSum2 - (Count - 2) * Math.PI) < 1)
                Sum = tempSum2;
            else
                Sum = tempSum1;
        }
        totalArea = (Sum - (Count - 2) * Math.PI) * Radius * Radius;

//        return String.valueOf(Math.floor(totalArea)); // 返回总面积
        return Math.floor(totalArea); // 返回总面积
    }

}


