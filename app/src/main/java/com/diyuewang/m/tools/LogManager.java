package com.diyuewang.m.tools;

import android.util.Log;

import com.diyuewang.m.BuildConfig;


/**
 * 日志管理类
 */
public class LogManager {

    private static String TAG = "DI_YUE_WANG";

    public static void d(String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, "" + msg);
        }
    }

    public static void e(String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(TAG, "" + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, "" + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, "" + msg);
        }
    }
}
