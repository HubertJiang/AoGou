package com.kui.gou.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.kui.gou.R;
import com.kui.gou.entity.User;
import com.kui.gou.util.Constant;
import com.tencent.bugly.crashreport.CrashReport;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class SignUpActivity extends BaseActivity implements View.OnClickListener {

    private EditText numberText, codeText, passwordText, nicknameText;
    private TextView getCode;
    private String phone, code, password, nickname;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setTitle(R.string.sign_up);
        numberText = (EditText) findViewById(R.id.number);
        codeText = (EditText) findViewById(R.id.code);
        passwordText = (EditText) findViewById(R.id.password);
        nicknameText = (EditText) findViewById(R.id.nickname);

        getCode = (TextView) findViewById(R.id.get_code_text);
        getCode.setOnClickListener(this);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1, R.color.refresh_progress_2, R.color.refresh_progress_3);
        swipeRefreshLayout.setProgressViewOffset(true, 0, 500);
        swipeRefreshLayout.setEnabled(false);
        SMSSDK.initSDK(this, Constant.SMS_APP_KEY, Constant.SMS_APP_SECRET);
        EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(final int event, final int result, final Object data) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        if (result == SMSSDK.RESULT_COMPLETE) {
                            //回调完成
                            if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                                //提交验证码成功
                                User user = new User();
                                user.setUsername(phone);
                                user.setPassword(password);
                                user.setNickname(nickname);
                                user.setMobilePhoneNumberVerified(true);
                                user.setMobilePhoneNumber(phone);
                                user.signUp(new SaveListener<User>() {
                                    @Override
                                    public void done(User user, BmobException e) {
                                        swipeRefreshLayout.setRefreshing(false);
                                        if (e == null) {
                                            AoApplication.showToast(R.string.sign_up_success);
                                            finish();
                                        } else {
                                            switch (e.getErrorCode()){
                                                case 202:
                                                    AoApplication.showToast(R.string.user_exist);
                                                    break;
                                                default:
                                                    AoApplication.showToast(e.toString());
                                                    break;
                                            }

                                        }
                                    }
                                });
                            } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                                //获取验证码成功
                                swipeRefreshLayout.setRefreshing(false);
                                AoApplication.showToast(R.string.code_send);
                            } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                                swipeRefreshLayout.setRefreshing(false);
                                //返回支持发送验证码的国家列表
                            }
                        } else {
                            swipeRefreshLayout.setRefreshing(false);
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
        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.sign_in_button:
                attemptLogin();
                break;
        }
    }


    private void attemptLogin() {
        // Reset errors.
        numberText.setError(null);
        codeText.setError(null);
        nicknameText.setError(null);
        passwordText.setError(null);

        // Store values at the time of the login attempt.
        phone = numberText.getText().toString();
        code = codeText.getText().toString();
        password = passwordText.getText().toString();
        nickname = nicknameText.getText().toString();

        boolean cancel = false;
        View focusView = null;
        // Check for a valid email address.
        if (TextUtils.isEmpty(nickname)) {
            nicknameText.setError(getString(R.string.prompt_nickname));
            focusView = nicknameText;
            cancel = true;
        } else if (TextUtils.isEmpty(phone)) {
            numberText.setError(getString(R.string.error_field_required));
            focusView = numberText;
            cancel = true;
        } else if (TextUtils.isEmpty(code)) {
            codeText.setError(getString(R.string.input_password));
            focusView = codeText;
            cancel = true;
        } else if (TextUtils.isEmpty(password)) {
            passwordText.setError(getString(R.string.input_password));
            focusView = passwordText;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            swipeRefreshLayout.setRefreshing(true);
            SMSSDK.submitVerificationCode("86", phone, code);

        }
    }


}

