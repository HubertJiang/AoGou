package com.kui.gou.util;

import android.os.CountDownTimer;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/5/6.
 */
public class TimeCount extends CountDownTimer {
    public static final int TIME_INTERVAL = 1000;
    private TextView textView;
    private int endStrRid;
    private String message;

    public TimeCount(TextView textView, String message, int reId){
      super(Constant.FUTURE_TIME,TIME_INTERVAL);
        this.textView=textView;
        this.endStrRid=reId;
        this.message=message;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        textView.setEnabled(false);
        textView.setText(millisUntilFinished / 1000 + message);
    }

    @Override
    public void onFinish() {
        textView.setText(endStrRid);
        textView.setEnabled(true);

    }


}
