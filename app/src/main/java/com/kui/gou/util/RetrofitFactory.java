
package com.kui.gou.util;

import com.kui.gou.BuildConfig;
import com.kui.gou.activity.AoApplication;
import com.kui.gou.smart.BasicCaching;
import com.kui.gou.smart.SmartCallFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.mob.tools.utils.Data.SHA1;


public class RetrofitFactory {

    private static CashService mGuDong;
    private static SmartCallFactory smartFactory;
    protected static final Object monitor = new Object();

    public static CashService getInstance() {
        synchronized (monitor) {
            if (mGuDong == null) {
                smartFactory = new SmartCallFactory(BasicCaching.fromCtx(AoApplication.getInstance()));
                HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
                // set your desired log level
                logging.setLevel(HttpLoggingInterceptor.Level.BODY);
                //设置OKHttpClient
                OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder()
                        .connectTimeout(2, TimeUnit.SECONDS)
                        .writeTimeout(60, TimeUnit.SECONDS)
                        .readTimeout(60, TimeUnit.SECONDS);//创建OKHttpClient的Builder
                if (BuildConfig.DEBUG) {
                    httpClientBuilder.addInterceptor(logging);
                }
                httpClientBuilder.addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        long now =System.currentTimeMillis();
                        Request request = null;
                        try {
                            request = chain.request()
                                    .newBuilder()
                                    .addHeader("X-APICloud-AppId", Constant.CLOUD_ID)
                                    .addHeader("X-APICloud-AppKey", SHA1(Constant.CLOUD_ID+"UZ"+Constant.CLOUD_KEY+"UZ"+now)+"."+now)
                                    .build();
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                        return chain.proceed(request);
                    }
                });
                //build OKHttpClient
                OkHttpClient okHttpClient = httpClientBuilder.build();
                Retrofit client = new Retrofit.Builder()
                        .baseUrl("https://d.apicloud.com/mcm/api/")
                        .client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create())
                        .addCallAdapterFactory(smartFactory)
                        .build();
                mGuDong = client.create(CashService.class);
            }
            return mGuDong;
        }
    }

    public static void clear() {
        if (mGuDong != null) {
            smartFactory.getCachingSystem().clear();
        }
    }


}
