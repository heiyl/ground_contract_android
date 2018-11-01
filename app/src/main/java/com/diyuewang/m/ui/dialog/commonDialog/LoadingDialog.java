package com.diyuewang.m.ui.dialog.commonDialog;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.diyuewang.m.R;


/**
 * 请求对话框
 */
public class LoadingDialog extends Dialog {

    public LoadingDialog(Context context) {
        super(context, R.style.loading_dialog_style);
        setContentView(R.layout.dialog_loading);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }
    public LoadingDialog(Context context, String tip) {
        super(context, R.style.loading_dialog_style);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_loading, null);
        TextView tvTip = (TextView) layout.findViewById(R.id.loading_text);
        tvTip.setText(tip);
        setContentView(layout);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);
    }

    @Override
    public void show() {
        super.show();
    }

}