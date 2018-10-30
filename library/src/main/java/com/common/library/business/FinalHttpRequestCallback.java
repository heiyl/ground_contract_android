package com.common.library.business;

import com.common.library.model.ResultDto;

import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;

/**
 * 请求基类
 * @param <T>
 */
public class FinalHttpRequestCallback<T extends ResultDto> extends BaseHttpRequestCallback<T> {

    @Override
    protected void onSuccess(T t) {
        super.onSuccess(t);
        onRespSuccess(t);
    }

    @Override
    public void onFailure(int errorCode, String msg) {
        super.onFailure(errorCode, msg);
        onRespFailure(errorCode, msg);
    }

    protected void onRespSuccess(T t) {

    }

    protected void onRespFailure(int errorCode, String msg) {

    }
}
