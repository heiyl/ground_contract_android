package com.diyuewang.m.tools.helper;

import android.app.Activity;
import android.content.Context;

import com.common.library.tools.ActivityStackManager;
import com.common.library.tools.helper.AccountHelper;
import com.diyuewang.m.MainActivity;

import cn.finalteam.okhttpfinal.RequestParams;

/**
 * 账号管理业务类
 */
public class AccountUtil {

    /**
     * 判断是否为登录状态
     *
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
        return AccountHelper.isLogin(context);
    }

    public static void logOut(Activity context) {
        boolean logOut = AccountHelper.logOut(context);
        if(logOut){
            //TODO 常驻数据修改
            ActivityStackManager.getInstance().finishToActivity(MainActivity.class, true);
            IntentManager.startLoginActivity(context);
        }
    }
    public static void saveLoginuserDto(String content, Context context) throws Exception {
        boolean save = AccountHelper.saveLoginuserDto(content,context);
        if(save){
            //TODO 常驻数据修改
        }
    }


    /**
     * 传递共通的参数authToken
     *
     * @param context
     * @return
     */
    public static RequestParams getRequestParams(Context context) {
        RequestParams requestParams = new RequestParams();
        requestParams.addHeader("platform", "Android");
        if (isLogin(context)) {
           /* String accessToken = getUserInfo().accessToken;
            requestParams.addHeader("accessToken", accessToken);*/
        }
        return requestParams;
    }

    /**
     * 开始APP
     *
     * @param context
     */
    public static void startApp(Context context) {
        /*if (AccountUtil.isLogin(context)) {
            IntentManager.startMainActivity(context);
        } else {
            IntentManager.startLoginActivity(context);
        }*/
        IntentManager.startMainActivity(context);
    }
}
