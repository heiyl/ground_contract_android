package com.diyuewang.m.ui.dialog.simpledialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.diyuewang.m.R;
import com.diyuewang.m.tools.UIUtils;


/**
 * Created by dinglong on 2018/1/17.
 * 类说明: 普通系统弹窗
 */

public class SimpleDialog {
    private View dialogView;
    private AlertDialog.Builder builder;
    private Boolean isCan = true;
    private AppCompatDialog dialog;
    private OnSimpleDialogClick onSimpleDialogClick;
    private static SimpleDialog systemDialog;

    public static SimpleDialog getInstance() {
        if (systemDialog == null) {
            systemDialog = new SimpleDialog();
        }
        return systemDialog;
    }

    public static SimpleDialog getNewInstance() {
        systemDialog = new SimpleDialog();
        return systemDialog;
    }

    public SimpleDialog initSystemDialog(Context context, boolean isTwoButton) {
        if (dialog != null) {
            try {
                dialog.cancel();
                dialog = null;
            } catch (Exception e) {
            }
        }
        builder = new AlertDialog.Builder(context);
        setSureText(context.getString(R.string.dialog_sure));
        if (isTwoButton) {
            setCancelText(context.getString(R.string.dialog_cancle));
        }
        dialog = builder.create();
        dialog.setCanceledOnTouchOutside(true);

        return SimpleDialog.this;
    }


    public SimpleDialog show() {
        try {
            if (builder != null) {
                dialog = builder.create();
                dialog.show();
                dialog.setCanceledOnTouchOutside(isCan);
            }
        } catch (Exception e) {
            Log.e("Exception= ", "show() called with: " + "");
        }

        return this;
    }

    public SimpleDialog setCanceledOnTouchOutside(Boolean isCan) {
        this.isCan = isCan;
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(isCan);
        }
        return this;
    }

    public SimpleDialog setTitle(@NonNull String title) {
//        if (dialog != null && dialogView != null) {
//            ((TextView) dialogView.findViewById(R.id.dialog_exit_title)).setText(title);
//        }
        if (builder != null) {
            builder.setTitle(title);
        }
        return this;
    }

    public SimpleDialog setCancelable(boolean isCancelable) {
//        if (dialog != null && dialogView != null) {
//            ((TextView) dialogView.findViewById(R.id.dialog_exit_title)).setText(title);
//        }
        if (builder != null) {
            builder.setCancelable(isCancelable);
        }
        return this;
    }

    public SimpleDialog setContent(@NonNull String content) {
//        if (dialog != null && dialogView != null) {
//            ((TextView) dialogView.findViewById(R.id.dialog_exit_content)).setText(content);
//        }
        if (builder != null) {
            builder.setMessage(content);

        }

        return this;
    }

    public SimpleDialog setSureText(@NonNull String sureText) {
//        if (dialog != null && dialogView != null) {
//            ((TextView) dialogView.findViewById(R.id.tv_dialog_sure)).setText(sureText);
//        }
        if (builder != null) {
            builder.setPositiveButton(sureText, new OnSystemDialogLister());
        }

        return this;
    }

    public SimpleDialog setCancelText(String cancelText) {
//        if (dialog != null && dialogView != null) {
//            ((TextView) dialogView.findViewById(R.id.tv_dialog_cancel)).setText(cancelText);
//        }
        if (builder != null) {
            if (TextUtils.isEmpty(cancelText)) {
                builder.setNegativeButton(UIUtils.getString(R.string.dialog_cancle), new OnSystemDialogLister());
            } else {
                builder.setNegativeButton(cancelText, new OnSystemDialogLister());
            }

        }
        return this;
    }


    private class OnSystemDialogLister implements DialogInterface.OnClickListener {

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_NEGATIVE://取消
                    dismiss();
                    if (onSimpleDialogClick != null) {
                        onSimpleDialogClick.onCancel();
                    }
                    break;
                case DialogInterface.BUTTON_POSITIVE://确定:
                    if (onSimpleDialogClick != null) {
                        onSimpleDialogClick.onSure();
                    }
                    break;
            }
        }
    }

    public void dismiss() {
        try {
            if (dialog != null) {
                dialog.dismiss();
                dialog = null;
                builder = null;
                if (dialogView != null) {
                    dialogView = null;
                }
            }
        } catch (Exception e) {

        }

    }

    public void destroy() {
        try {
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        } catch (Exception e) {

        }
        dialog = null;
        builder = null;
        dialogView = null;
        onSimpleDialogClick = null;
        systemDialog = null;
    }

    public interface OnSimpleDialogClick {
        void onSure();

        void onCancel();
    }

//    public SimpleDialog setOnSystemDialogClick(OnSimpleDialogClick onSimpleDialogClick) {
//        this.onSimpleDialogClick = onSimpleDialogClick;
//        return this;
//    }

    public SimpleDialog setOnSimpleDialogClick(OnSimpleDialogClick onSimpleDialogClick) {
        this.onSimpleDialogClick = onSimpleDialogClick;
        return this;
    }

    public SimpleDialog setOnDismissListener(DialogInterface.OnDismissListener onDissmissListener) {
        if (builder != null) {
            builder.setOnDismissListener(onDissmissListener);
        }
        return this;
    }
}
