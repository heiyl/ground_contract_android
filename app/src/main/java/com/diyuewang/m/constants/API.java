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
                HOST = "http://api.diyuewang.com/"; // 正式地址
                break;

        }
    }

    /**
     * 短信推送
     */
    public static final String SMS_SEND = HOST + "main/send.shtml";
    /**
     * 短信验证
     */
    public static final String SMS_VAL = HOST + "user/login/phone/val.shtml";
    /**
     * 用户登录
     */
    public static final String USER_LOGIN = HOST + "employee/login.shtml";
    /**
     * 发布土地 -员工发布
     */
    public static final String EMPLOYEE_SEND = HOST + "order/create/employee.shtml";

}
