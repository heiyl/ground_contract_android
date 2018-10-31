package com.diyuewang.m.tools.helper;

import android.content.Context;

import com.common.library.model.ResultDto;
import com.diyuewang.m.constants.API;
import com.diyuewang.m.constants.Constants;
import com.diyuewang.m.tools.LogManager;
import com.diyuewang.m.tools.UIUtils;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import okhttp3.Headers;
import okhttp3.Response;


public class SmsUtil {


    public interface SendSmsCallBack {
        public void sendLoginSmsCallBack(ResultDto status);
    }

    /**
     * 发送登录验证码
     *
     * @param context
     * @param phone
     */
    public static void sendLoginSms(Context context, String phone, final SendSmsCallBack sendSmsCallBack) {

        String phoneNo = phone;


        RequestParams params = new RequestParams();
        params.addFormDataPart("phoneNo", phoneNo);
        params.addFormDataPart("type", 2);
        LogManager.d("SMS_SEND", API.SMS_SEND + "?" + params);
        HttpRequest.post(API.SMS_SEND, params, new BaseHttpRequestCallback<ResultDto>() {
            @Override
            public void onStart() {
                super.onStart();
            }


            @Override
            public void onResponse(Response httpResponse, String response, Headers headers) {
                super.onResponse(httpResponse, response, headers);
            }

            @Override
            protected void onSuccess(ResultDto resp) {
                super.onSuccess(resp);
                try {

                    if (resp.code == Constants.REQ_RESPOSE_CODE) {
//                        UIUtils.showToast(UIUtils.getString(R.string.toast_sendSmsed));
                    } else {
                        UIUtils.showToast(resp.msg);
                    }
                    if (sendSmsCallBack != null) {
                        sendSmsCallBack.sendLoginSmsCallBack(resp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
//                UIUtils.showToast(UIUtils.getString(R.string.toast_error_net_work));
            }
        });


    }
}
