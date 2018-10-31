package com.diyuewang.m.constants;


import com.diyuewang.m.BuildConfig;

public class API {
    public static Boolean isDebug = Boolean.TRUE;//是否debug版本
    public static final String HOST;

    static {
        switch (BuildConfig.API_ENV) {
            case "TEST":
                isDebug = Boolean.TRUE;
                HOST = "http://112.124.2.5:8080/diyue/";// 测试
                break;
            default:
                isDebug = Boolean.FALSE;
                HOST = "http://112.124.2.5:8080/diyue/"; // 正式地址
                break;

        }
    }

    /**
     * 短信推送
     */
    public static final String SMS_SEND = HOST + "/user/v1/sendCode.shtml";
    /**
     * 用户登录
     */
    public static final String USER_LOGIN = HOST + "/user/v1/userLogin.shtml";

}
