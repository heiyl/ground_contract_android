package com.common.library.tools.helper;

import android.content.Context;

import com.common.library.tools.FileUtils;
import com.common.library.tools.aes.AESOperator;

import java.io.File;

import cn.finalteam.toolsfinal.StringUtils;

/**
 * 账号管理业务类
 * le-speaking-android
 * 2017/12/25.
 * yulong
 */
public class AccountHelper {

    /**
     * 判断是否为登录状态
     *
     * @param context
     * @return
     */
    public static boolean isLogin(Context context) {
        if (context != null)
            return !StringUtils.isEmpty(FileUtils.readFile(FileUtils.getAuthPath(context)));
        else
            return false;
    }

    public static boolean logOut(Context context) {
        if (!AccountHelper.isLogin(context) || AccountHelper.deleteLoginUserFile(context)) {
            return true;
        }
        return false;
    }

    /**
     * 保存登录信息
     *
     * @param content
     * @param context
     * @return
     * @throws Exception
     */
    public static boolean saveLoginuserDto(String content, Context context) throws Exception {
        boolean result;
        String after = AESOperator.encrypt(content, AESOperator.PASSWORD, AESOperator.PASSWORD);
        result = FileUtils.writeFile(FileUtils.getAuthPath(context), after);
        return result;

    }

    /**
     * 删除保存在本地的登陆信息
     *
     * @param context
     */
    public static boolean deleteLoginUserFile(Context context) {
        File file = new File(FileUtils.getAuthPath(context));
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

}
