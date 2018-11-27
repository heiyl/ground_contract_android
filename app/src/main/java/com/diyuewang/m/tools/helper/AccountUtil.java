package com.diyuewang.m.tools.helper;

import android.app.Activity;
import android.content.Context;

import com.common.library.tools.ActivityStackManager;
import com.common.library.tools.FileUtils;
import com.common.library.tools.aes.AESOperator;
import com.common.library.tools.helper.AccountHelper;
import com.diyuewang.m.BaseApplicaiton;
import com.diyuewang.m.MainActivity;
import com.diyuewang.m.constants.Constants;
import com.diyuewang.m.model.Employee;
import com.diyuewang.m.model.User;
import com.diyuewang.m.model.UserDto;
import com.diyuewang.m.tools.LogManager;
import com.diyuewang.m.tools.UIUtils;
import com.diyuewang.m.ui.activity.BrowserActivity;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

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

    public static void logOut(Activity context,boolean isMain) {
        boolean logOut = AccountHelper.logOut(context);
        if(logOut){
            //TODO 常驻数据修改
            BaseApplicaiton.loginUserDto = null;
            if(isMain){
                ActivityStackManager.getInstance().finishToActivity(MainActivity.class, true);
                ActivityStackManager.getInstance().finishActivity(MainActivity.class);
            }else{
                ActivityStackManager.getInstance().finishToActivity(BrowserActivity.class, true);
                ActivityStackManager.getInstance().finishActivity(BrowserActivity.class);
            }
            IntentManager.startLoginActivity(context);
        }
    }
    public static boolean saveLoginuserDto(String content, Context context) throws Exception {
        boolean save = AccountHelper.saveLoginuserDto(content,context);
        if(save){
            try {
                Gson gson = new Gson();
                BaseApplicaiton.loginUserDto = gson.fromJson(content, UserDto.class);
            } catch (JsonSyntaxException e) {
                LogManager.e("saveLoginuserDto", e.toString());
            }

        }
        return save;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static Employee getEmployee() {
        Employee userInfo = null;
        if (getLoginUserDto() != null && getLoginUserDto().data != null && getLoginUserDto().data.employee != null) {
            userInfo = getLoginUserDto().data.employee;
        }
        return userInfo;
    }
    /**
     * 获取用户信息
     *
     * @return
     */
    public static User getUser() {
        User userInfo = null;
        if (getLoginUserDto() != null && getLoginUserDto().data != null && getLoginUserDto().data.user != null) {
            userInfo = getLoginUserDto().data.user;
        }
        return userInfo;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public static String getUserId() {
        String userId = "";
        if(getLoginUserDto().loginType == Constants.LOGIN_TYPE_NORMAL){
            User user = null;
            if(getUser() != null){
                userId = getUser().id;
            }

        }else{

            Employee userInfo = null;
            if (getEmployee() != null) {
                userId = getEmployee().id;
            }
        }
        return userId;
    }

    /**
     * 获取登录信息
     *
     * @return
     */
    public static UserDto getLoginUserDto() {
        if (BaseApplicaiton.loginUserDto == null) {
            try {
                String auth = FileUtils.readFile(FileUtils.getAuthPath(UIUtils.getContext()));//解密前的auth
                if (auth != null) {
//                    String authafter = AESUtils.decrypt(AESUtils.PASSWORD, auth);// 解密后的auth
                    String authafter = AESOperator.decrypt(auth, AESOperator.PASSWORD, AESOperator.PASSWORD);
                    try {
                        Gson gson = new Gson();
                        BaseApplicaiton.loginUserDto = gson.fromJson(authafter, UserDto.class);
                    } catch (JsonSyntaxException e) {
                        LogManager.e("getLoginUserDto", e.toString());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return BaseApplicaiton.loginUserDto;
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
        if (AccountUtil.isLogin(context)) {
            if(AccountUtil.getLoginUserDto().loginType == 0){
                IntentManager.startBrowActivity(context);
            }else{
                IntentManager.startMainActivity(context);
            }
        } else {
            IntentManager.startLoginActivity(context);
        }
//        IntentManager.startMainActivity(context);
    }
}
