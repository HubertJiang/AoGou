package com.kuiyuan.aogou.activity;

import android.app.Application;
import android.widget.Toast;

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
        Bmob.initialize(this, "3e6164ca438e7534e0174f7c6ae78247");
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

}
