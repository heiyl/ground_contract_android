package com.diyuewang.m.tools;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

public class ToastUtil {

    private static Toast toast;
    private static Toast centerToast;

    private static Toast initToast(CharSequence message, int duration) {
        //如果有变更字体大小的需求，那么Toast需要重新实例化
        if (toast == null) {
            toast = Toast.makeText(UIUtils.getContext(), message, duration);
        } else {
            toast.setText(message);
            toast.setDuration(duration);
        }
        return toast;
    }
    private static Toast initToast(CharSequence message, int duration, boolean isCenter) {
        //如果有变更字体大小的需求，那么Toast需要重新实例化
        if (centerToast == null) {
            centerToast = Toast.makeText(UIUtils.getContext(), message, duration);
            if(isCenter){
                centerToast.setGravity(Gravity.CENTER, 0, 0);
            }
        } else {
            centerToast.setText(message);
            centerToast.setDuration(duration);
        }
        return centerToast;
    }

    public static void cancel() {
        if (toast != null) {
            toast.cancel();
        }
        if (centerToast != null) {
            centerToast.cancel();
        }
    }

    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message) {

        if (TextUtils.isEmpty(message)) return;
        initToast(message, Toast.LENGTH_SHORT).show();

    }
    /**
     * 短时间显示Toast
     *
     * @param message
     */
    public static void showShort(CharSequence message, boolean isCenter) {

        if (TextUtils.isEmpty(message)) return;
        initToast(message, Toast.LENGTH_SHORT,isCenter).show();

    }

    /**
     * 短时间显示Toast
     *
     * @param strResId
     */
    public static void showShort(int strResId) {
        initToast(UIUtils.getContext().getResources().getText(strResId), Toast.LENGTH_SHORT).show();
    }
    /**
     * 短时间显示Toast
     *
     * @param strResId
     */
    public static void showShort(int strResId,boolean isCenter) {
        initToast(UIUtils.getContext().getResources().getText(strResId), Toast.LENGTH_SHORT,isCenter).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param message
     */
    public static void showLong(CharSequence message) {
        if (TextUtils.isEmpty(message)) return;
        initToast(message, Toast.LENGTH_LONG).show();
    }

    /**
     * 长时间显示Toast
     *
     * @param strResId
     */
    public static void showLong(int strResId) {
        initToast(UIUtils.getContext().getText(strResId), Toast.LENGTH_LONG).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param message
     * @param duration
     */
    public static void show(CharSequence message, int duration) {
        initToast(message, duration).show();
    }

    /**
     * 自定义显示Toast时间
     *
     * @param context
     * @param strResId
     * @param duration
     */
    public static void show(Context context, int strResId, int duration) {
        initToast(context.getResources().getText(strResId), duration).show();
    }

}
