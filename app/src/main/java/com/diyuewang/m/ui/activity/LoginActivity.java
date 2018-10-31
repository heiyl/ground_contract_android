package com.diyuewang.m.ui.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.common.library.model.ResultDto;
import com.diyuewang.m.R;
import com.diyuewang.m.base.BaseToolBarActivity;
import com.diyuewang.m.constants.API;
import com.diyuewang.m.constants.Constants;
import com.diyuewang.m.model.UserDto;
import com.diyuewang.m.tools.UIUtils;
import com.diyuewang.m.tools.helper.AccountUtil;
import com.diyuewang.m.tools.helper.SmsUtil;
import com.google.gson.Gson;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.finalteam.okhttpfinal.BaseHttpRequestCallback;
import cn.finalteam.okhttpfinal.HttpRequest;
import cn.finalteam.okhttpfinal.RequestParams;
import cn.finalteam.toolsfinal.StringUtils;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * 登录
 * le-speaking-android
 * 2017/11/28.
 * yulong
 */

public class LoginActivity extends BaseToolBarActivity implements View.OnClickListener {

    //登录标题
    @BindString(R.string.account_login_title)
    String mLoginTitle;

    @BindView(R.id.btn_login)
    TextView btn_login;

    @BindView(R.id.username)
    AppCompatEditText edtUserNmae;

    @BindView(R.id.edt_sms_code)
    AppCompatEditText edtSmsCode;

    @BindView(R.id.tv_get_sms_code)
    TextView tvGetSmsCode;

    public static final int MSG_ID_DEFAULT = 0;

    //是否可以重新获取验证码
    private boolean canClickAble = true;
    private int countDownSecond = 60;
    private String mPhone;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_ID_DEFAULT:
                    if (tvGetSmsCode == null)
                        return;
                    String codeContent = String.format(UIUtils.getString(R.string.verificationcode_post_reset), countDownSecond--);
                    tvGetSmsCode.setText(codeContent);

                    if (countDownSecond >= 0) {
                        canClickAble = false;
                        mHandler.removeMessages(MSG_ID_DEFAULT);
                        mHandler.sendEmptyMessageDelayed(MSG_ID_DEFAULT, 1000);
                    } else {
                        countDownSecond = 60;
                        tvGetSmsCode.setEnabled(true);
                        canClickAble = true;
                        tvGetSmsCode.setText(getResources().getString(R.string.account_login_get_phone_code));
                        tvGetSmsCode.setTextColor(getResources().getColor(R.color.app_red_color));
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        initViews();
    }

    private void initViews() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        initStatusBar(false);
        ButterKnife.bind(this);
        tvGetSmsCode.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        edtUserNmae.addTextChangedListener(new EditChangedListener());
        edtSmsCode.addTextChangedListener(new EditChangedListener());


    }

    private class EditChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            String phone = edtUserNmae.getText().toString().trim();

            if (!TextUtils.isEmpty(phone) && StringUtils.isPhone(phone) && !TextUtils.isEmpty(edtSmsCode.getText().toString().trim())) {
                btn_login.setEnabled(true);
            } else {
                btn_login.setEnabled(false);
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_get_sms_code://获取验证码
                if (canClickAble) {
                    getRegCode();
                }
                break;
            case R.id.btn_login://登录
                login_btn_Click();
                break;
        }
    }

    private void login_btn_Click() {
        mPhone = edtUserNmae.getText().toString().trim();
        String sms = edtSmsCode.getText().toString().trim();

        RequestParams params = new RequestParams();
        params.addFormDataPart("phone", mPhone);
        params.addFormDataPart("pwd", sms);

        HttpRequest.post(API.USER_LOGIN, params, new BaseHttpRequestCallback<UserDto>() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onResponse(Response httpResponse, String response, Headers headers) {
                super.onResponse(httpResponse, response, headers);

            }

            @Override
            protected void onSuccess(UserDto resp) {
                super.onSuccess(resp);
                try {
                    if (resp.code == Constants.REQ_RESPOSE_CODE) {
                        Gson gson = new Gson();
                        String content = gson.toJson(resp);
                        try {
                            if (AccountUtil.saveLoginuserDto(content, LoginActivity.this)) {
                                AccountUtil.startApp(activity);
                                back();
                                overridePendingTransition(0, 0);
                            } else {
                                UIUtils.showToast("登陆失败");
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {

                        }

                    } else {
                        UIUtils.showToastInCenter(resp.msg);
//                        UIUtils.showToast(resp.errorMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int errorCode, String msg) {
                super.onFailure(errorCode, msg);
            }
        });


    }

    /**
     * 设置获取验证码按钮的倒计时状态
     */
    public void setGetCodeState() {
        // 判断是否是11位
        mHandler.removeMessages(MSG_ID_DEFAULT);
        mHandler.sendEmptyMessageDelayed(MSG_ID_DEFAULT, 1000);
        tvGetSmsCode.setTextColor(getResources().getColor(R.color.content_normal_color));
        tvGetSmsCode.setEnabled(false);
        canClickAble = false;
    }

    private void getRegisterFailure() {
        edtUserNmae.setEnabled(true);
        canClickAble = true;
    }

    private void getRegCode() {
        mPhone = edtUserNmae.getText().toString().trim();
        if (!StringUtils.isEmpty(mPhone) && StringUtils.isPhone(mPhone)) {
            canClickAble = false;
            SmsUtil.sendLoginSms(this, mPhone, new SmsUtil.SendSmsCallBack() {

                @Override
                public void sendLoginSmsCallBack(ResultDto status) {
                    if (status.code == Constants.REQ_RESPOSE_CODE) {
                        edtUserNmae.setEnabled(true);
                        setGetCodeState();
                    } else {
                        getRegisterFailure();
                    }
                }

            });
        } else {

        }
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(0, 0);
    }

    @Override
    protected void onDestroy() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}
