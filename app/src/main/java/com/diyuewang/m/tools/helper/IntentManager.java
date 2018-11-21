package com.diyuewang.m.tools.helper;

import android.content.Context;
import android.content.Intent;

import com.diyuewang.m.MainActivity;
import com.diyuewang.m.ui.activity.BrowserActivity;
import com.diyuewang.m.ui.activity.LoginActivity;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class IntentManager {

    /**
     * 跳到主页面类
     *
     * @param context
     */
    public static void startMainActivity(Context context) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }
    /**
     * 跳到主页面类
     *
     * @param context
     */
    public static void startBrowActivity(Context context) {
        Intent intent = new Intent(context, BrowserActivity.class);
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);

    }

    /**
     * 登陆页面
     * @param context
     */
    public static void startLoginActivity(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }
}
