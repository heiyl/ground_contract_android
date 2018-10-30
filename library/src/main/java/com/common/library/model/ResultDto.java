package com.common.library.model;


import java.io.Serializable;

public class ResultDto implements Serializable {
    // true操作成功，false为失败
    public boolean result;
    // 错误码
    public String errorCode;
    // 错误信息
    public String errorMsg;

}
