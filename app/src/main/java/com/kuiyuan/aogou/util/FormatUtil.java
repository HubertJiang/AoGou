package com.kuiyuan.aogou.util;

import java.text.DecimalFormat;

/**
 * Created by liweihui on 2016/10/10.
 */

public class FormatUtil {
    public static String moneyFormat(double money) {
        DecimalFormat decimalFormat;
        if (money != 0 && (long) money == money) {
            decimalFormat = new DecimalFormat("#,###元");
        } else {
            decimalFormat = new DecimalFormat("#,##0.00元");
        }
        return decimalFormat.format(money);
    }
}
