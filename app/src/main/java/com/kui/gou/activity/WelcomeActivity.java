package com.kui.gou.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kui.gou.R;

public class WelcomeActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        if (BmobUser.getCurrentUser() == null) {
//            startActivity(new Intent(this, SignInActivity.class));
//        } else {
            startActivity(new Intent(this, MainActivity.class));
//        }

        finish();
    }


}
