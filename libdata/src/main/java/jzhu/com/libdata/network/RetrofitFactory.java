package jzhu.com.libdata.network;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class RetrofitFactory {

    private static volatile RetrofitFactory sInstance;

    private Retrofit retrofit;

    private RetrofitFactory() {
        retrofit = new Retrofit.Builder()
                .baseUrl(HttpConfig.BASE_URL)      //访问主机地址
                .addConverterFactory(GsonConverterFactory.create())  //解析方式
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(genericClient())
                .build();

    }

    public static RetrofitFactory getInstance() {
        if (null == sInstance) {
            synchronized (RetrofitFactory.class) {
                if (null == sInstance) {
                    sInstance = new RetrofitFactory();
                }
            }
        }
        return sInstance;
    }

    public <T> T create(final Class<T> service) {
        return retrofit.create(service);
    }

    private static OkHttpClient genericClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(InterceptorUtil.HeaderInterceptor())
                .addInterceptor(InterceptorUtil.LogInterceptor())
                .addNetworkInterceptor(InterceptorUtil.stethoInterceptor())
                .connectTimeout(HttpConfig.HTTP_OUT_TIME, TimeUnit.SECONDS)
                .readTimeout(HttpConfig.HTTP_OUT_TIME, TimeUnit.SECONDS)
                .writeTimeout(HttpConfig.HTTP_OUT_TIME, TimeUnit.SECONDS)
                .build();
        return httpClient;
    }

}
