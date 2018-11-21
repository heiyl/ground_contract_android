package com.diyuewang.m;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.diyuewang.m.constants.Constants;
import com.diyuewang.m.model.UserDto;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.crashreport.CrashReport;
import com.tencent.smtt.sdk.QbSdk;

import cn.finalteam.okhttpfinal.OkHttpFinal;
import cn.finalteam.okhttpfinal.OkHttpFinalConfiguration;

public class BaseApplicaiton extends Application {

    private static long mMainThreadId;
    private static Handler mMainHandler;

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static Handler getMainHandler() {
        return mMainHandler;
    }

    private static BaseApplicaiton mInstance = null;

    public static UserDto loginUserDto;

    public static BaseApplicaiton getInstance() {
        if (mInstance == null) {
            throw new IllegalStateException("Application is not created.");
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mMainThreadId = android.os.Process.myTid();
        // 创建主线程的handler
        mMainHandler = new Handler();
        initBugly();
        initBaiduMap();
        initQbSdk();
        new Thread(new Runnable() {
            @Override
            public void run() {
                initOkHttpFinal();
            }
        }).start();
    }

    private void initBugly(){
        Bugly.init(getApplicationContext(), "5d92acddad", true);
    }


    private void initBaiduMap() {
        // 在使用 SDK 各组间之前初始化 context 信息，传入 ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    private void initOkHttpFinal() {
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder()
                .setTimeout(Constants.REQ_TIMEOUT)
                .setDebug(BuildConfig.DEBUG);
        OkHttpFinal.getInstance().init(builder.build());
    }

    private void initQbSdk() {
        //搜集本地tbs内核信息并上报服务器，服务器返回结果决定使用哪个内核。
        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {
            @Override
            public void onViewInitFinished(boolean arg0) {
                // TODO Auto-generated method stub
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                Log.d("app", " onViewInitFinished is " + arg0);
            }
            @Override
            public void onCoreInitFinished() {
                // TODO Auto-generated method stub
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(this,  cb);
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
