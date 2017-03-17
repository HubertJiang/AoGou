package com.kui.gou.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.kui.gou.R;
import com.kui.gou.util.Constant;
import com.kui.gou.util.TimeCount;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

/**
 * Created by liweihui on 2017/3/17.
 */

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private ViewFlipper viewFlipper;
    private EditText numberText, codeText, passwordText, rePasswordText;
    private Button nextButton, sureButton;
    private TextView getCode;
    private String phone, code, password, rePassword;
    private SwipeRefreshLayout swipeRefreshLayout;
    private TimeCount timeCount;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().setTitle(R.string.get_password);
        numberText = (EditText) findViewById(R.id.number);
        codeText = (EditText) findViewById(R.id.code);
        passwordText = (EditText) findViewById(R.id.password);
        rePasswordText = (EditText) findViewById(R.id.re_password_text);
        nextButton = (Button) findViewById(R.id.next_button);
        sureButton = (Button) findViewById(R.id.sure_button);
        getCode = (TextView) findViewById(R.id.get_code_text);
        viewFlipper = (ViewFlipper) findViewById(R.id.view_flipper);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        getCode.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        sureButton.setOnClickListener(this);
        timeCount=new TimeCount(getCode,getString(R.string.can_resend_code),R.string.resend_code);

        SMSSDK.initSDK(this, Constant.SMS_APP_KEY, Constant.SMS_APP_SECRET);
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(final int event, final int result, final Object data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            //回调完成
                            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                //提交验证码成功
                                viewFlipper.showNext();
                                getSupportActionBar().setTitle(R.string.reset_password);
                            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                                //获取验证码成功
                                timeCount.start();
                                AoApplication.showToast(R.string.code_send);
                            } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                                //返回支持发送验证码的国家列表
                            }
                        } else {
getCode.setEnabled(true);
                            try {
                                Throwable throwable = (Throwable) data;
                                throwable.printStackTrace();
                                JSONObject object = new JSONObject(throwable.getMessage());
                                String des = object.optString("detail");//错误描述
                                int status = object.optInt("status");//错误代码
                                switch (status) {
                                    case 468:
                                        des = getString(R.string.code_error);
                                        break;
                                    default:
                                        break;
                                }
                                AoApplication.showToast(des);
                            } catch (JSONException e) {
                                CrashReport.postCatchedException(e);
                            }

                        }
                    }
                });

            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.next_button:
                phone = numberText.getText().toString();
                code = codeText.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    numberText.setError(getString(R.string.error_field_required));
                    numberText.requestFocus();
                } else if (TextUtils.isEmpty(code)) {
                    codeText.setError(getString(R.string.input_code));
                    codeText.requestFocus();
                } else {
                    swipeRefreshLayout.setRefreshing(true);
                    SMSSDK.submitVerificationCode("86", phone, code);
                }
                break;
            case R.id.sure_button:
                password = passwordText.getText().toString();
                rePassword = rePasswordText.getText().toString();
                if (TextUtils.isEmpty(password)) {

                } else if (TextUtils.isEmpty(rePassword)) {

                } else if (!password.equals(rePassword)) {

                } else {
                    BmobUser user = BmobUser.getCurrentUser();
                    user.setPassword(password);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                AoApplication.showToast("更新用户信息成功");
                                finish();
                            } else {
                                AoApplication.showToast("更新用户信息失败:" + e.getMessage());
                            }
                        }
                    });
                }
                break;
            case R.id.get_code_text:
                String phone = numberText.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    numberText.setError(getString(R.string.error_field_required));
                } else {
                    v.setEnabled(false);
                    swipeRefreshLayout.setRefreshing(true);
                    SMSSDK.getVerificationCode("86", phone);
                }
                break;
        }
    }
}
