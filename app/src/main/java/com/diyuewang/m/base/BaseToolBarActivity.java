package com.diyuewang.m.base;

import android.graphics.drawable.StateListDrawable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.common.library.base.BaseActivity;
import com.common.library.tools.DrawableUtil;
import com.diyuewang.m.R;
import com.diyuewang.m.tools.LogManager;


/**
 * 导航栏基类
 */
public abstract class BaseToolBarActivity extends BaseActivity {
    /**
     * 统一初始化titlebar
     */
    protected void initToolBar(String title, boolean haveBtn) {
        FrameLayout mHeaderLeft = (FrameLayout) findViewById(R.id.headbar_left_btn_container);
        ImageView mBackImg = (ImageView) findViewById(R.id.toolbar_back_img);
        TextView mTitleTxt = (TextView) findViewById(R.id.toolbar_title);
        mTitleTxt.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (haveBtn) {
            mHeaderLeft.setVisibility(View.VISIBLE);
            mHeaderLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back();
                }
            });
        } else {
            mHeaderLeft.setVisibility(View.GONE);
        }
        LogManager.e(TAG, this.getClass().getSimpleName());
    }

    /**
     * 统一初始化titlebar
     */
    protected void initToolBar(String title) {
        FrameLayout mHeaderLeft = (FrameLayout) findViewById(R.id.headbar_left_btn_container);
        ImageView mBackImg = (ImageView) findViewById(R.id.toolbar_back_img);
        TextView mTitleTxt = (TextView) findViewById(R.id.toolbar_title);
        mTitleTxt.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHeaderLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    /**
     * 统一初始化titlebar左侧图片
     */
    protected void initToolBarLeftImg(String title, int leftId) {
        FrameLayout mHeaderLeft = (FrameLayout) findViewById(R.id.headbar_left_btn_container);
        ImageView mBackImg = (ImageView) findViewById(R.id.toolbar_back_img);
        TextView mTitleTxt = (TextView) findViewById(R.id.toolbar_title);
        mTitleTxt.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mRightTxt = (TextView) findViewById(R.id.toolbar_right_txt);
        mRightTxt.setVisibility(View.GONE);
        ImageView mRightImg = (ImageView) findViewById(R.id.toolbar_right_img);
        mRightImg.setVisibility(View.GONE);
        mBackImg.setImageResource(leftId);
        mHeaderLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    /**
     * 统一初始化titlebar左侧图片
     */
    protected void initToolBarLeftImg(String title, int leftId, View.OnClickListener listener) {
        FrameLayout mHeaderLeft = (FrameLayout) findViewById(R.id.headbar_left_btn_container);
        ImageView mBackImg = (ImageView) findViewById(R.id.toolbar_back_img);
        TextView mTitleTxt = (TextView) findViewById(R.id.toolbar_title);
        mTitleTxt.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mRightTxt = (TextView) findViewById(R.id.toolbar_right_txt);
        mRightTxt.setVisibility(View.GONE);
        ImageView mRightImg = (ImageView) findViewById(R.id.toolbar_right_img);
        mRightImg.setVisibility(View.GONE);
        mBackImg.setImageResource(leftId);
        mHeaderLeft.setOnClickListener(listener);
    }

    /**
     * 统一初始化titlebar左侧图片
     */
    protected void initToolBarLeftImg(String title, int leftId, int leftPressedId, View.OnClickListener listener) {
        FrameLayout mHeaderLeft = (FrameLayout) findViewById(R.id.headbar_left_btn_container);
        ImageView mBackImg = (ImageView) findViewById(R.id.toolbar_back_img);
        TextView mTitleTxt = (TextView) findViewById(R.id.toolbar_title);
        mTitleTxt.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView mRightTxt = (TextView) findViewById(R.id.toolbar_right_txt);
        mRightTxt.setVisibility(View.GONE);
        ImageView mRightImg = (ImageView) findViewById(R.id.toolbar_right_img);
        mRightImg.setVisibility(View.GONE);
//        mBackImg.setImageResource(leftId);
        StateListDrawable drawable = DrawableUtil.newSelector(this, leftId, leftPressedId, leftPressedId, leftId);
        mBackImg.setBackgroundDrawable(drawable);
        mHeaderLeft.setOnClickListener(listener);
    }

    /**
     * 统一初始化titlebar右侧图片
     */
    protected void initToolBarRightImg(String title, int rightId, int rightPressedId, View.OnClickListener listener) {
        FrameLayout mHeaderLeft = (FrameLayout) findViewById(R.id.headbar_left_btn_container);
        FrameLayout mHeaderRight = (FrameLayout) findViewById(R.id.headbar_right_btn_container);
        ImageView mBackImg = (ImageView) findViewById(R.id.toolbar_back_img);
        TextView mTitleTxt = (TextView) findViewById(R.id.toolbar_title);
        mTitleTxt.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHeaderLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        TextView mRightTxt = (TextView) findViewById(R.id.toolbar_right_txt);
        mRightTxt.setVisibility(View.GONE);
        ImageView mRightImg = (ImageView) findViewById(R.id.toolbar_right_img);
        mRightImg.setVisibility(View.VISIBLE);
        StateListDrawable drawable = DrawableUtil.newSelector(this, rightId, rightPressedId, rightPressedId, rightId);
        mRightImg.setBackgroundDrawable(drawable);
        mHeaderRight.setOnClickListener(listener);
    }

    /**
     * 统一初始化titlebar左侧文字
     */
    protected void initToolBarLeftTxt(String title, String left, View.OnClickListener listener) {
        FrameLayout mHeaderLeft = (FrameLayout) findViewById(R.id.headbar_left_btn_container);
        FrameLayout mHeaderRight = (FrameLayout) findViewById(R.id.headbar_right_btn_container);
        ImageView mBackImg = (ImageView) findViewById(R.id.toolbar_back_img);
        TextView mTitleTxt = (TextView) findViewById(R.id.toolbar_title);
        mTitleTxt.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHeaderLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        ImageView mLeftImg = (ImageView) findViewById(R.id.toolbar_back_img);
        mLeftImg.setVisibility(View.GONE);
        TextView mRightTxt = (TextView) findViewById(R.id.toolbar_left_txt);
        mRightTxt.setVisibility(View.VISIBLE);
        mRightTxt.setText(left);
        mHeaderRight.setOnClickListener(listener);
    }

    /**
     * 统一初始化titlebar右侧文字
     */
    protected void initToolBarRightTxt(String title, String right, View.OnClickListener listener) {
        FrameLayout mHeaderLeft = (FrameLayout) findViewById(R.id.headbar_left_btn_container);
        FrameLayout mHeaderRight = (FrameLayout) findViewById(R.id.headbar_right_btn_container);
        ImageView mBackImg = (ImageView) findViewById(R.id.toolbar_back_img);
        TextView mTitleTxt = (TextView) findViewById(R.id.toolbar_title);
        mTitleTxt.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHeaderLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        ImageView mRightImg = (ImageView) findViewById(R.id.toolbar_right_img);
        mRightImg.setVisibility(View.GONE);
        TextView mRightTxt = (TextView) findViewById(R.id.toolbar_right_txt);
        mRightTxt.setVisibility(View.VISIBLE);
        mRightTxt.setText(right);
        mHeaderRight.setOnClickListener(listener);
    }
    /**
     * 统一初始化titlebar右侧文字
     */
    protected void initToolBarRightTxt(String title, String right, View.OnClickListener listener, boolean haveLeftBtn) {
        FrameLayout mHeaderLeft = (FrameLayout) findViewById(R.id.headbar_left_btn_container);
        FrameLayout mHeaderRight = (FrameLayout) findViewById(R.id.headbar_right_btn_container);
        ImageView mBackImg = (ImageView) findViewById(R.id.toolbar_back_img);
        TextView mTitleTxt = (TextView) findViewById(R.id.toolbar_title);
        mTitleTxt.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (haveLeftBtn) {
            mHeaderLeft.setVisibility(View.VISIBLE);
            mHeaderLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    back();
                }
            });
        } else {
            mHeaderLeft.setVisibility(View.GONE);
        }
        ImageView mRightImg = (ImageView) findViewById(R.id.toolbar_right_img);
        mRightImg.setVisibility(View.GONE);
        TextView mRightTxt = (TextView) findViewById(R.id.toolbar_right_txt);
        mRightTxt.setVisibility(View.VISIBLE);
        mRightTxt.setText(right);
        mHeaderRight.setOnClickListener(listener);
    }

    /**
     * 统一初始化titlebar 左侧和右侧文字
     */
    protected void initToolBarLeftRightTxt(String title, String left, String right, View.OnClickListener rightListener) {
        FrameLayout mHeaderLeft = (FrameLayout) findViewById(R.id.headbar_left_btn_container);
        FrameLayout mHeaderRight = (FrameLayout) findViewById(R.id.headbar_right_btn_container);
        ImageView mBackImg = (ImageView) findViewById(R.id.toolbar_back_img);
        mBackImg.setVisibility(View.GONE);
        TextView mLeftTxt = (TextView) findViewById(R.id.toolbar_left_txt);
        mLeftTxt.setVisibility(View.VISIBLE);
        mLeftTxt.setText(left);
        TextView mTitleTxt = (TextView) findViewById(R.id.toolbar_title);
        mTitleTxt.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHeaderLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        ImageView mRightImg = (ImageView) findViewById(R.id.toolbar_right_img);
        mRightImg.setVisibility(View.GONE);
        TextView mRightTxt = (TextView) findViewById(R.id.toolbar_right_txt);
        mRightTxt.setVisibility(View.VISIBLE);
        mRightTxt.setText(right);
        mHeaderRight.setOnClickListener(rightListener);
    }
    /**
     * 统一初始化titlebar 左侧和右侧文字
     */
    protected void initToolBarLeftRightTxt(String title, String left, String right, View.OnClickListener liftListener, View.OnClickListener rightListener) {
        FrameLayout mHeaderLeft = (FrameLayout) findViewById(R.id.headbar_left_btn_container);
        FrameLayout mHeaderRight = (FrameLayout) findViewById(R.id.headbar_right_btn_container);
        ImageView mBackImg = (ImageView) findViewById(R.id.toolbar_back_img);
        mBackImg.setVisibility(View.GONE);
        TextView mLeftTxt = (TextView) findViewById(R.id.toolbar_left_txt);
        mLeftTxt.setVisibility(View.VISIBLE);
        mLeftTxt.setText(left);
        TextView mTitleTxt = (TextView) findViewById(R.id.toolbar_title);
        mTitleTxt.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ImageView mRightImg = (ImageView) findViewById(R.id.toolbar_right_img);
        mRightImg.setVisibility(View.GONE);
        TextView mRightTxt = (TextView) findViewById(R.id.toolbar_right_txt);
        mRightTxt.setVisibility(View.VISIBLE);
        mRightTxt.setText(right);
        mHeaderLeft.setOnClickListener(liftListener);
        mHeaderRight.setOnClickListener(rightListener);
    }

    /**
     * 统一初始化titlebar 左侧图片和右侧文字
     */
    protected void initToolBarLeftImgRightTxt(String title, int leftId, String right, View.OnClickListener liftListener, View.OnClickListener rightListener) {
        FrameLayout mHeaderLeft = (FrameLayout) findViewById(R.id.headbar_left_btn_container);
        FrameLayout mHeaderRight = (FrameLayout) findViewById(R.id.headbar_right_btn_container);
        ImageView mBackImg = (ImageView) findViewById(R.id.toolbar_back_img);
        mBackImg.setVisibility(View.VISIBLE);
        TextView mLeftTxt = (TextView) findViewById(R.id.toolbar_left_txt);
        mLeftTxt.setVisibility(View.GONE);
        TextView mTitleTxt = (TextView) findViewById(R.id.toolbar_title);
        mTitleTxt.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHeaderLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        mBackImg.setImageResource(leftId);
        ImageView mRightImg = (ImageView) findViewById(R.id.toolbar_right_img);
        mRightImg.setVisibility(View.GONE);
        TextView mRightTxt = (TextView) findViewById(R.id.toolbar_right_txt);
        mRightTxt.setVisibility(View.VISIBLE);
        mRightTxt.setText(right);
        mHeaderLeft.setOnClickListener(liftListener);
        mHeaderRight.setOnClickListener(rightListener);
    }

    /**
     * 统一初始化titlebar 左侧图片和右侧图片
     */
    protected void initToolBarLeftImgRightImg(String title, int leftId, int rightId, View.OnClickListener liftListener, View.OnClickListener rightListener) {
        FrameLayout mHeaderLeft = (FrameLayout) findViewById(R.id.headbar_left_btn_container);
        FrameLayout mHeaderRight = (FrameLayout) findViewById(R.id.headbar_right_btn_container);
        ImageView mBackImg = (ImageView) findViewById(R.id.toolbar_back_img);
        mBackImg.setVisibility(View.VISIBLE);
        TextView mLeftTxt = (TextView) findViewById(R.id.toolbar_left_txt);
        mLeftTxt.setVisibility(View.GONE);
        TextView mTitleTxt = (TextView) findViewById(R.id.toolbar_title);
        mTitleTxt.setText(title);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mHeaderLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        mBackImg.setImageResource(leftId);
        ImageView mRightImg = (ImageView) findViewById(R.id.toolbar_right_img);
        mRightImg.setVisibility(View.VISIBLE);
        mRightImg.setImageResource(rightId);
        TextView mRightTxt = (TextView) findViewById(R.id.toolbar_right_txt);
        mRightTxt.setVisibility(View.GONE);
        mHeaderLeft.setOnClickListener(liftListener);
        mHeaderRight.setOnClickListener(rightListener);
    }

    protected void setToolBarBackground(int backgroundId) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundDrawable(getResources().getDrawable(backgroundId));
    }

    protected void setToolBarBackgroundColor(int backgroundId) {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setBackgroundResource(backgroundId);
    }

    protected void setToolBarTitleColor(int color) {
        TextView mTitleTxt = (TextView) findViewById(R.id.toolbar_title);
        mTitleTxt.setTextColor(getResources().getColor(color));
    }
    protected void setToolBarLeftTitleColor(int color) {
        TextView mLeftTxt = (TextView) findViewById(R.id.toolbar_left_txt);
        mLeftTxt.setTextColor(getResources().getColor(color));
    }
    protected void setToolBarRightTitleColor(int color) {
        TextView mRightTxt = (TextView) findViewById(R.id.toolbar_right_txt);
        mRightTxt.setTextColor(getResources().getColor(color));
    }

    protected void setToolBarBottomLine(boolean have) {
        View diver = findViewById(R.id.diver);
        if (have) {
            diver.setVisibility(View.VISIBLE);
        } else {
            diver.setVisibility(View.GONE);
        }
    }
}
