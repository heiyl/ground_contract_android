package com.diyuewang.m.model;

import com.common.library.model.ResultDto;

public class UserDto extends ResultDto {
    public UserModel data;
    public int loginType;//0:普通用户 1：员工
}
