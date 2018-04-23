package ru.lantimat.photogallery.API;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by GabdrakhmanovII on 08.12.2017.
 */

public class RetrofitClient {

    private static OkHttpClient.Builder okHttpClient;
    private static Retrofit retrofit = null;

    public static void initClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        // задаем уровень логирования
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        okHttpClient = new OkHttpClient().newBuilder()
                .connectTimeout(60 * 5, TimeUnit.SECONDS)
                .readTimeout(60 * 5, TimeUnit.SECONDS)
                .writeTimeout(60 * 5, TimeUnit.SECONDS);
        okHttpClient.addInterceptor(new AddCookiesInterceptor());
        okHttpClient.addInterceptor(logging);
    }

    public static Retrofit getClient(String baseUrl) {

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
