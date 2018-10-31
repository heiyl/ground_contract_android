package com.diyuewang.m.ui.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.contrarywind.listener.OnItemSelectedListener;
import com.contrarywind.view.WheelView;
import com.diyuewang.m.R;

import java.util.Arrays;
import java.util.List;


public class SelectPopupWindow extends PopupWindow {

    private final List<String> mOptionsItems;
    private TextView tv_cancle, tv_sure;
    private WheelView wheelview;
    WheelViewListener listener;
    private String content;

    public interface WheelViewListener{
        void getContent(String content);
    }

    public SelectPopupWindow(final Context mContext, View parent, final WheelViewListener listener) {
        super(mContext);
        View view = View.inflate(mContext, R.layout.dialog_select_popupwindows, null);
        view.invalidate();
        view.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_ins));
        LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        RelativeLayout root = (RelativeLayout) view.findViewById(R.id.root);
        ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.push_bottom_in));

        setWidth(LayoutParams.FILL_PARENT);
        setHeight(LayoutParams.FILL_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        // update();

        wheelview = (WheelView) view.findViewById(R.id.wheelview);
        wheelview.setCyclic(false);

        Resources res = mContext.getResources();
        String[] city = res.getStringArray(R.array.select_type);
        mOptionsItems = Arrays.asList(city);
        content = mOptionsItems.get(0);
        wheelview.setAdapter(new ArrayWheelAdapter(mOptionsItems));
        wheelview.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                content = mOptionsItems.get(index);
//                Toast.makeText(mContext, "" + mOptionsItems.get(index), Toast.LENGTH_SHORT).show();
            }
        });

        tv_cancle = (TextView) view.findViewById(R.id.tv_cancle);
        tv_sure = (TextView) view.findViewById(R.id.tv_sure);
        tv_cancle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });
        tv_sure.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                if(listener != null){
                    listener.getContent(content);
                }
                dismiss();
            }
        });
        root.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

    }

}
