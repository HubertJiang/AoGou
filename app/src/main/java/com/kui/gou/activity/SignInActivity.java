package com.kui.gou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kui.gou.R;
import com.kui.gou.entity.User;
import com.kui.gou.util.RetrofitFactory;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A login screen that offers login via email/password.
 */
public class SignInActivity extends BaseActivity implements OnClickListener {
    private EditText mPasswordView, mEmailView;
    private String phone, code;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        getSupportActionBar().setTitle(R.string.action_sign_in);
        mEmailView = (EditText) findViewById(R.id.number);
        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh_progress_1,R.color.refresh_progress_2,R.color.refresh_progress_3);
        swipeRefreshLayout.setProgressViewOffset(true, 0, 500);
        swipeRefreshLayout.setEnabled(false);
        findViewById(R.id.sign_up).setOnClickListener(this);
        findViewById(R.id.get_password).setOnClickListener(this);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);
        phone = mEmailView.getText().toString();
        code = mPasswordView.getText().toString();
        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(phone)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (TextUtils.isEmpty(code)) {
            mPasswordView.setError(getString(R.string.input_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            swipeRefreshLayout.setRefreshing(true);
            User user = new User();
            RetrofitFactory.getInstance().signIn(phone,code).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                   if(response.isSuccessful()){
                       AoApplication.setUserId(response.body().userId);
                       AoApplication.setUserName(phone);
                       finish();
                   }else {
                       AoApplication.showToast(R.string.login_error);
                   }
                    swipeRefreshLayout.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    swipeRefreshLayout.setRefreshing(false);
                    AoApplication.showToast(R.string.no_network);
                }
            });
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sign_up:
                startActivity(new Intent(this, SignUpActivity.class));
                break;
            case R.id.get_password:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
        }
    }
}

