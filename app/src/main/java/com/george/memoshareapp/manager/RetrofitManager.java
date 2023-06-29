package com.george.memoshareapp.manager;

import com.george.memoshareapp.BuildConfig;
import com.george.memoshareapp.properties.AppProperties;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitManager {

    private static RetrofitManager mInstance;

    private final Retrofit mRetrofit;

    private RetrofitManager() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(AppProperties.LOCAL_SERVER_SUNNY)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkhttpClient())
                .build();

    }

    public static RetrofitManager getInstance() {
        if (mInstance == null) {
            synchronized (RetrofitManager.class) {
                if (mInstance == null) {
                    mInstance = new RetrofitManager();
                }
            }
        }
        return mInstance;
    }

    private OkHttpClient getOkhttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (BuildConfig.DEBUG) {
            builder.addInterceptor(getHttpLoggingInterceptor()); //日志拦截器
        }
        return builder
                .addInterceptor(getInterceptor()) //通用拦截器
                .connectTimeout(20, TimeUnit.SECONDS) //设置连接超时时间
                .readTimeout(20, TimeUnit.SECONDS) //设置读取超时时间
                .retryOnConnectionFailure(true)
                .build();

    }


    /**
     * 日志拦截器
     *
     * @return
     */
    private Interceptor getHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.NONE);
        return interceptor;
    }

    /**
     * 通用拦截器
     * 根据自己项目的需求添加公共的请求头和请求参数
     *
     * @return
     */
    private Interceptor getInterceptor() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request()
                        .newBuilder()
                        .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
                        .addHeader("Accept-Encoding", "gzip, deflate")
                        .addHeader("Connection", "keep-alive")
                        .addHeader("Accept", "*/*")
                        .build();
                return chain.proceed(request);
            }
        };
        return interceptor;
    }

    public <T> T create(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }
}

