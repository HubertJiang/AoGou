package com.kui.gou.listener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by jiangkuiyuan on 2017/3/18.
 */

public class ServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int noReadNum = intent.getIntExtra("noReadCount", 0);
        String content = intent.getStringExtra("content"); //未读消息数
    }
}
