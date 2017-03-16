package com.kui.gou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kui.gou.R;
import com.kui.gou.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;


public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    // UI references.
    private EditText numberText, codeText,passwordText;
    private TextView getCode;
    private String phone, code;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.sign_up);
        numberText = (EditText) findViewById(R.id.number);
        codeText = (EditText) findViewById(R.id.code);
        passwordText = (EditText) findViewById(R.id.password);
//        populateAutoComplete();

        getCode = (TextView) findViewById(R.id.get_code_text);
        getCode.setOnClickListener(this);
        passwordText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
//                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
//        mEmailSignInButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                attemptLogin();
//            }
//        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        swipeRefreshLayout.setProgressViewOffset(true, 0, 500);
        swipeRefreshLayout.setEnabled(false);
        SMSSDK.initSDK(this, "16f89e72e9f32", "1672abf13cf56ac79ed75280d008eae9");
        final EventHandler eh = new EventHandler() {

            @Override
            public void afterEvent(int event, int result, Object data) {

                Message msg = new Message();
                msg.arg1 = event;
                msg.arg2 = result;
                msg.obj = data;

                System.out.println("result-----" + result);
                System.out.println("data-----" + data);

                handler.sendMessage(msg);


            }
        };
        SMSSDK.registerEventHandler(eh); //注册短信回调
findViewById(R.id.sign_in_button).setOnClickListener(this);
//        getCodeButton.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String phone = mEmailView.getText().toString();
//                if (TextUtils.isEmpty(phone)) {
//                    mEmailView.setError(getString(R.string.error_field_required));
//                } else {
//                    getCodeButton.setEnabled(false);
//                    SMSSDK.getVerificationCode("86", phone);
//                }
//            }
//        });
    }

    Handler handler = new Handler() {

        @Override
        public void handleMessage(final Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;

            swipeRefreshLayout.setRefreshing(false);
            if (result == SMSSDK.RESULT_COMPLETE) {
                //回调完成
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    //提交验证码成功
                    User user = new User();
                    user.setUsername(phone);
                    user.setPassword("123456");

                    user.login(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (user != null) {
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                finish();
                            } else {
                                Message message = new Message();
                                message.arg1 = 9;
                                message.arg2 = SMSSDK.RESULT_COMPLETE;
                                handler.sendMessage(message);

                            }
                        }
                    });
                } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                    //获取验证码成功
//                    getCodeButton.setEnabled(true);
                    AoApplication.showToast(R.string.code_send);
                } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                    //返回支持发送验证码的国家列表
                    Log.d("--------", "_________");
                } else if (event == 9) {
                    User user = new User();
                    user.setUsername(phone);
                    user.setPassword("123456");
                    user.signUp(new SaveListener<User>() {
                        @Override
                        public void done(User user, BmobException e) {
                            if (user != null) {
                                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                                finish();
                            } else {
                                AoApplication.showToast(e.toString());
                            }
                        }
                    });
                }
            } else {
                swipeRefreshLayout.setRefreshing(false);
                try {
                    JSONObject json = new JSONObject(((Throwable) data).getMessage());
                    AoApplication.showToast(json.getString("detail"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }


        }

    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.get_code_text:
                String phone = numberText.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    numberText.setError(getString(R.string.error_field_required));
                } else {
                    v.setEnabled(false);
                    SMSSDK.getVerificationCode("86", phone);
                }
                break;
            case R.id.sign_in_button:
                attemptLogin();
                break;
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {


        // Reset errors.
        numberText.setError(null);
        passwordText.setError(null);

        // Store values at the time of the login attempt.
        phone = numberText.getText().toString();
        code = codeText.getText().toString();

        boolean cancel = false;
        View focusView = null;


        // Check for a valid email address.
        if (TextUtils.isEmpty(phone)) {
            numberText.setError(getString(R.string.error_field_required));
            focusView = numberText;
            cancel = true;
        } else if (TextUtils.isEmpty(code)) {
            codeText.setError(getString(R.string.input_password));
            focusView = codeText;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
//            showProgress(true);
            swipeRefreshLayout.setRefreshing(true);
//            SMSSDK.submitVerificationCode("86", phone, code);
            User user = new User();
            user.setUsername(phone);
            user.setPassword(code);
            user.signUp(new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    swipeRefreshLayout.setRefreshing(false);
                    if(e==null){
                        AoApplication.showToast("注册成功:");
                    }else{
                        AoApplication.showToast(e.toString());
                    }
                }
            });

//            user.login(new SaveListener<User>() {
//                @Override
//                public void done(User user, BmobException e) {
//                    swipeRefreshLayout.setRefreshing(false);
//                    if (user != null) {
//                        startActivity(new Intent(SignUpActivity.this, MainActivity.class));
//                        finish();
//                    } else {
//                        AoApplication.showToast(R.string.login_error);
//
//                    }
//                }
//            });
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

