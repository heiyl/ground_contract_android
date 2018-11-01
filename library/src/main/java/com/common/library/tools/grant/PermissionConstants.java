package com.common.library.tools.grant;

import android.Manifest;

/**
 * Created by 太极 on 2017/11/6.
 */

public class PermissionConstants {
    public static final String STOR = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String CAMERA = Manifest.permission.CAMERA;
    public static final String RECORD = Manifest.permission.RECORD_AUDIO;
    public static final int STOR_CODE = 1;
    public static final int CAMERA_CODE = 2;
    public static final int RECORD_CODE = 3;
    private static int[] permissionArray = {STOR_CODE, CAMERA_CODE, RECORD_CODE};

    public static boolean isNeedCheck(int requestCode) {
        for (int pc : permissionArray) {
            if (requestCode == pc) {
                return true;
            }
        }
        return false;
    }
}
