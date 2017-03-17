package com.kui.gou.activity;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.kui.gou.R;

/**
 * Created by liweihui on 2017/3/17.
 */

public class BaseActivity extends AppCompatActivity {
    private LinearLayout contentView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_top);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {

        if (R.layout.view_top == layoutResID) {
            super.setContentView(R.layout.view_top);
            contentView = (LinearLayout) findViewById(R.id.linear_layout);
            if(contentView.getChildCount()>1){
                contentView.removeViews(1,contentView.getChildCount());
            }


        } else {
            View addView = LayoutInflater.from(this).inflate(layoutResID, null);
            contentView.addView(addView);
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
