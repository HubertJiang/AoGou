package com.kui.gou.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ViewFlipper;

import com.kui.gou.R;

/**
 * Created by liweihui on 2017/3/17.
 */

public class ResetPasswordActivity extends BaseActivity {
    private ViewFlipper viewFlipper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);
        getSupportActionBar().setTitle(R.string.get_password);
    }
}
