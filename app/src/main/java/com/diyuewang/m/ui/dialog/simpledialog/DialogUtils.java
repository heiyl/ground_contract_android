package com.diyuewang.m.ui.dialog.simpledialog;

import android.content.Context;

/**
 * Created by dinglong on 2017/12/21.
 * 类说明: 弹窗管理类
 */

public class DialogUtils {
    private static DialogUtils dialogUtils;
    private static boolean isNewInstance;

    public DialogUtils() {
    }

    public static DialogUtils getInstance() {
        if (dialogUtils == null) {
            dialogUtils = new DialogUtils();
        }
        isNewInstance = false;
        return dialogUtils;
    }

    public static DialogUtils getNewInstance() {
        dialogUtils = new DialogUtils();
        isNewInstance = true;
        return dialogUtils;
    }

    public SimpleDialog initSimpleDialog(Context context, boolean isTwoButton) {
        SimpleDialog systemDialog;
        if (isNewInstance) {
            systemDialog = SimpleDialog.getNewInstance();
        } else {
            systemDialog = SimpleDialog.getInstance();
        }
        systemDialog.initSystemDialog(context, isTwoButton);

        return systemDialog;
    }



}
