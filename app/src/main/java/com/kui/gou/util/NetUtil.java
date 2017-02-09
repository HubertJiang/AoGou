package com.kui.gou.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * Created by liweihui on 2016/10/12.
 */

public class NetUtil {
    private static NetUtil instance;
    private OkHttpClient okHttpClient;

    public NetUtil() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .build();
    }

    public static NetUtil getInstance() {
        if (instance == null) {
            synchronized (NetUtil.class) {
                if (instance == null) {
                    instance = new NetUtil();
                }
            }
        }
        return instance;
    }


    /**
     * 一般的get请求 对于一般的请求，我们希望给个url，然后取的返回的String。
     */
    public String get(String url) {
        return get(url, null);
    }

    public String get(String url, Map<String, String> params) {
        StringBuilder sb = new StringBuilder();
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            sb.append("?");
            for (Map.Entry<String, String> entry : entrySet) {
                sb.append(entry.getKey());
                sb.append("=");
                try {
                    sb.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                sb.append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        Request request = new Request.Builder().url(url + sb.toString()).get().build();
        Call call = okHttpClient.newCall(request);
        try {
            return call.execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 一般的post请求 对于一般的请求，我们希望给个url和封装参数的Map，然后取的返回的String。
     */
    public String post(String url, Map<String, String> params) {
        FormBody.Builder formBodyBuilder = new FormBody.Builder();
        if (params != null && params.size() > 0) {
            Set<Map.Entry<String, String>> entrySet = params.entrySet();
            for (Map.Entry<String, String> entry : entrySet) {
                formBodyBuilder.add(entry.getKey(), entry.getValue());
            }
        }
        String key = "lmxuhwagxhi2d", time = String.valueOf(System.currentTimeMillis()/1000);
        Request request= new Request.Builder().url(url)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("App-Key", key)
                .addHeader("Nonce", "14314")
                .addHeader("Timestamp", time)
                .addHeader("Signature", stringMD5("9WcMheZoH8VYX"+"14314"+time))
                .post(formBodyBuilder.build()).build();

        Call call = okHttpClient.newCall(request);
        try {
            return call.execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String stringMD5(String pw) {
        try {

            // 拿到一个MD5转换器（如果想要SHA1参数换成”SHA1”）
            MessageDigest messageDigest =MessageDigest.getInstance("SHA1");
            // 输入的字符串转换成字节数组
            byte[] inputByteArray = pw.getBytes();
            // inputByteArray是输入字符串转换得到的字节数组
            messageDigest.update(inputByteArray);
            // 转换并返回结果，也是字节数组，包含16个元素
            byte[] resultByteArray = messageDigest.digest();
            // 字符数组转换成字符串返回
            return byteArrayToHex(resultByteArray);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    public static String byteArrayToHex(byte[] byteArray) {

        // 首先初始化一个字符数组，用来存放每个16进制字符
        char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9', 'A','B','C','D','E','F' };
        // new一个字符数组，这个就是用来组成结果字符串的（解释一下：一个byte是八位二进制，也就是2位十六进制字符（2的8次方等于16的2次方））
        char[] resultCharArray =new char[byteArray.length * 2];
        // 遍历字节数组，通过位运算（位运算效率高），转换成字符放到字符数组中去
        int index = 0;
        for (byte b : byteArray) {
            resultCharArray[index++] = hexDigits[b>>> 4 & 0xf];
            resultCharArray[index++] = hexDigits[b& 0xf];
        }
        // 字符数组组合成字符串返回
        return new String(resultCharArray);
    }

}
