package com.diyuewang.m.tools;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.diyuewang.m.BaseApplicaiton;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;

import cn.finalteam.okhttpfinal.Part;


public class UIUtils {
    /**
     * 上下文
     *
     * @return
     */
    public static Context getContext() {
        return BaseApplicaiton.getInstance();
    }

    public static Resources getResources() {
        return getContext().getResources();
    }

    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    public static String getPackageName() {
        return getContext().getPackageName();
    }

    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    public static Handler getMainHandler() {
        return BaseApplicaiton.getMainHandler();
    }

    public static long getMainThreadId() {
        return BaseApplicaiton.getMainThreadId();
    }

    /**
     * 让task在主线程中执行
     */
    public static void post(Runnable task) {
        int myTid = android.os.Process.myTid();

        if (myTid == getMainThreadId()) {
            // 在主线程中执行的
            task.run();
        } else {
            // 在子线程中执行的
            getMainHandler().post(task);
        }
    }


    /**
     * 显示Toast
     *
     * @param msg
     */
    public static void showToast(final String msg) {

        post(new Runnable() {

            @Override
            public void run() {

                ToastUtil.showShort(msg);
            }
        });
    }

    /**
     * 显示Toast距中
     *
     * @param msg
     */
    public static void showToastInCenter(final String msg) {

        post(new Runnable() {

            @Override
            public void run() {
                ToastUtil.showShort(msg, true);
            }
        });
    }

    /**
     * 显示Toast
     *
     * @param resId
     */
    public static void showToast(final int resId) {

        post(new Runnable() {
            @Override
            public void run() {
                ToastUtil.showShort(resId);
            }
        });
    }

    /**
     * 显示Toast剧中
     *
     * @param resId
     */
    public static void showToastInCenter(final int resId) {

        post(new Runnable() {

            @Override
            public void run() {
                ToastUtil.showShort(resId, true);
            }
        });
    }

    /**
     * 显示Toast
     *
     * @param msg
     */
    public static void showToastLong(final String msg) {

        post(new Runnable() {

            @Override
            public void run() {
//                Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
                ToastUtil.showLong(msg);
            }
        });
    }

    /**
     * 显示ToastLong
     *
     * @param resId
     */
    public static void showToastLong(final int resId) {

        post(new Runnable() {

            @Override
            public void run() {
//                Toast.makeText(getContext(), getString(resId), Toast.LENGTH_SHORT).show();
                ToastUtil.showLong(resId);
            }
        });
    }

    /**
     * 取消toast
     */
    public static void cancelToast() {

        post(new Runnable() {

            @Override
            public void run() {
                ToastUtil.cancel();
            }
        });
    }

    /**
     * dip 转 px
     *
     * @param dip
     * @return
     */
    public static int dip2px(int dip) {
        //
        // 公式： dp = px / (dpi / 160) px = dp * (dpi / 160)
        // dp = px / denisity
        // px = dp * denisity;
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        return (int) (dip * density + 0.5f);
    }

    public static int px2dip(int px) {
        // dp = px / denisity
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float density = metrics.density;
        return (int) (px / density + 0.5f);
    }

    /**
     * 执行延时任务
     */
    public static void postDelayed(Runnable task, int delayed) {
        getMainHandler().postDelayed(task, delayed);
    }

    /**
     * 移除任务
     *
     * @param task
     */
    public static void removeCallbacks(Runnable task) {
        getMainHandler().removeCallbacks(task);
    }

    public static String getString(int id, Object... formatArgs) {
        return getResources().getString(id, formatArgs);
    }

    /**
     * 获取view
     *
     * @param mContext
     * @param res
     * @param parent
     * @return
     */
    public static View getView(Context mContext, int res, ViewGroup parent) {
        View contentView = LayoutInflater.from(mContext).inflate(res, parent);
        return contentView;
    }


    public static int getPingHeight() {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);

        return wm.getDefaultDisplay().getHeight();

    }

    public static int getPingWidth() {
        WindowManager wm = (WindowManager) getContext()
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay().getWidth();

    }


    public static String getVersionName() {
        // 获取packagemanager的实例
        PackageManager packageManager = UIUtils.getContext().getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = packInfo.versionName;
        return version;
    }

    public static String getFullUrl(String url, List<Part> params, boolean urlEncoder) {
        StringBuffer urlFull = new StringBuffer();
        urlFull.append(url);
        if (urlFull.indexOf("?", 0) < 0 && params.size() > 0) {
            urlFull.append("?");
        }
        int flag = 0;
        for (Part part : params) {
            String key = part.getKey();
            String value = part.getValue();
            if (urlEncoder) {//只对key和value编码
                try {
                    key = URLEncoder.encode(key, "UTF-8");
                    value = URLEncoder.encode(value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            urlFull.append(key).append("=").append(value);
            if (++flag != params.size()) {
                urlFull.append("&");
            }
        }

        return urlFull.toString();
    }


    //获取屏幕宽高
    public static Point getScreenWidthHeight(Activity context) {
        Point size = new Point();
        try {
            int realWidth = 0, realHeight = 0;
            Display display = context.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            if (android.os.Build.VERSION.SDK_INT >= 17) {
                display.getRealSize(size);
                realWidth = size.x;
                realHeight = size.y;
            } else if (android.os.Build.VERSION.SDK_INT < 17
                    && android.os.Build.VERSION.SDK_INT >= 14) {
                Method mGetRawH = Display.class.getMethod("getRawHeight");
                Method mGetRawW = Display.class.getMethod("getRawWidth");
                realWidth = (Integer) mGetRawW.invoke(display);
                realHeight = (Integer) mGetRawH.invoke(display);
            } else {
                realWidth = metrics.widthPixels;
                realHeight = metrics.heightPixels;
            }
            size.x = realWidth;
            size.y = realHeight;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static void setButtonBg(Context context, Button bt, int normal, int selected) {

        bt.setCompoundDrawablesWithIntrinsicBounds(null, getStateListDrawable(context, normal, selected), null, null);
    }

    public static StateListDrawable getStateListDrawable(Context context, int normal, int selected) {

        StateListDrawable drawable = new StateListDrawable();
        //Non focused states
        drawable.addState(new int[]{-android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                context.getResources().getDrawable(normal));
        drawable.addState(new int[]{-android.R.attr.state_focused, android.R.attr.state_selected, -android.R.attr.state_pressed},
                context.getResources().getDrawable(selected));
        //Focused states
        drawable.addState(new int[]{android.R.attr.state_focused, -android.R.attr.state_selected, -android.R.attr.state_pressed},
                context.getResources().getDrawable(selected));
        drawable.addState(new int[]{android.R.attr.state_focused, android.R.attr.state_selected, -android.R.attr.state_pressed},
                context.getResources().getDrawable(selected));
        //Pressed
        drawable.addState(new int[]{android.R.attr.state_selected, android.R.attr.state_pressed},
                context.getResources().getDrawable(selected));
        drawable.addState(new int[]{android.R.attr.state_pressed},
                context.getResources().getDrawable(selected));

        return drawable;

    }

    private static final int MIN_DELAY_TIME = 500;  // 两次点击间隔不能少于500ms
    private static long lastClickTime;

    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= MIN_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    private static final int MIN_Change_DELAY_TIME = 500;  // 两次点击间隔不能少于500ms
    private static long lastChangeClickTime;

    /**
     * 判断是否是快速点击
     * 注意：连续两次调用的时候需要重置 lastChangeClickTime 时间。调用方法为 resetLastChangeClickTime()
     *
     * @return
     */
    public static boolean isFastChangeClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastChangeClickTime) >= MIN_Change_DELAY_TIME) {
            flag = false;
            lastChangeClickTime = currentClickTime;
        }
        return flag;
    }

    public static void resetLastChangeClickTime() {
        lastChangeClickTime = 0;
    }

}
