package com.kui.gou.activity;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

import com.kui.gou.R;
import com.kui.gou.util.SPUtils;
import com.sobot.chat.SobotApi;
import com.tencent.bugly.Bugly;



/**
 * Created by jiangkuiyuan on 16/9/10.
 */
public class AoApplication extends Application {
    private static Application instance;
    private static Toast toast;
    private static String userId;
    private static String userName;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Bugly.init(getApplicationContext(), "900027438", false);

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

    public static String getUserId() {
        if(TextUtils.isEmpty(userId)){
            userId= SPUtils.getString(instance,SPUtils.UID);
        }
        return userId;
    }

    public static void setUserId(String userId) {
        AoApplication.userId = userId;
        SPUtils.setString(instance,SPUtils.UID,userId);
    }

    public static String getUserName() {
        if(TextUtils.isEmpty(userName)){
            userName= SPUtils.getString(instance,SPUtils.NAME);
        }
        return userName;
    }

    public static void setUserName(String userName) {
        AoApplication.userName = userName;
        SPUtils.setString(instance,SPUtils.NAME,userName);
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
