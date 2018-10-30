package com.diyuewang.m;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.support.multidex.MultiDex;

import com.diyuewang.m.constants.Constants;

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
        new Thread(new Runnable() {
            @Override
            public void run() {
                initOkHttpFinal();
            }
        }).start();
    }

    private void initOkHttpFinal() {
        OkHttpFinalConfiguration.Builder builder = new OkHttpFinalConfiguration.Builder()
                .setTimeout(Constants.REQ_TIMEOUT)
                .setDebug(BuildConfig.DEBUG);
        OkHttpFinal.getInstance().init(builder.build());
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
