package com.common.library.model;


import java.io.Serializable;

public class ResultDto implements Serializable {
    // 0:true  其他：false
    public int code;
    // 错误信息
    public String msg;

}
