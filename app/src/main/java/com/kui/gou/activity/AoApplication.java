package com.kui.gou.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

import com.kui.gou.R;
import com.sobot.chat.SobotApi;
import com.tencent.bugly.Bugly;

import cn.bmob.v3.Bmob;


/**
 * Created by jiangkuiyuan on 16/9/10.
 */
public class AoApplication extends Application {
    private static Application instance;
    private static Toast toast;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Bugly.init(getApplicationContext(), "900027438", false);
        Bmob.initialize(this, "3e6164ca438e7534e0174f7c6ae78247");

        SobotApi.setNotificationFlag(getApplicationContext(),true, R.drawable.sobot_logo_small_icon,R.drawable.sobot_logo_icon);
    }

    public static void showToast(String string) {
        if (toast == null) {
            toast = Toast.makeText(instance, string, Toast.LENGTH_SHORT);
        } else {
            toast.setText(string);
        }
        toast.show();
    }


    public static void showToast(int resId) {
        if (toast == null) {
            toast = Toast.makeText(instance, resId, Toast.LENGTH_SHORT);
        } else {
            toast.setText(resId);
        }
        toast.show();
    }

    public static Application getInstance() {
        return instance;
    }

    /**
     * 获得当前进程的名字
     *
     * @param context
     * @return
     */
    public static String getCurProcessName(Context context) {

        int pid = android.os.Process.myPid();

        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager
                .getRunningAppProcesses()) {

            if (appProcess.pid == pid) {

                return appProcess.processName;
            }
        }
        return null;
    }
}
