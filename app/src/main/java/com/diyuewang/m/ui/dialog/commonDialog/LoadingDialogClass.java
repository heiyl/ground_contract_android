package com.diyuewang.m.ui.dialog.commonDialog;

import android.content.Context;

public class LoadingDialogClass {

    private static LoadingDialog customDialog;

    // 显示自定义加载对话框
    public static LoadingDialog showLodDialog(Context context) {
        if (customDialog != null) {
            try {
                if (!customDialog.isShowing()) {
                    customDialog.show();
                }
            } catch (Exception e) {
            }
        } else {
            customDialog = new LoadingDialog(context);
            try {
                customDialog.show();
            } catch (Exception e) {
            }
        }


        return customDialog;
    }
    // 显示自定义加载对话框
    public static LoadingDialog showLodDialog(Context context, String tip) {
        if (customDialog != null) {
            try {
                if (!customDialog.isShowing()) {
                    customDialog.show();
                }
            } catch (Exception e) {
            }
        } else {
            customDialog = new LoadingDialog(context,tip);
            try {
                customDialog.show();
            } catch (Exception e) {
            }
        }


        return customDialog;
    }

    // 关闭自定义加载对话框
    public static void closeLodDialog() {
        if (customDialog != null) {
            try {
                customDialog.cancel();
                customDialog = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
