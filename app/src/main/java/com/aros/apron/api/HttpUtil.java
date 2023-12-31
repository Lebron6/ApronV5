package com.aros.apron.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpUtil {
    /**
     * 初始化okhttpclient.
     * @return okhttpClient
     */
    public OkHttpClient okhttpclient() {
//        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger(getClass().getSimpleName()));
//        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor())
                .build();
        return mOkHttpClient;
    }

    public UavApi createRequest() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl.ipAddress)
                .client(okhttpclient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UavApi vamApi = retrofit.create(UavApi.class);
        return vamApi;
    }
}
