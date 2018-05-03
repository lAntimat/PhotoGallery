package ru.lantimat.photogallery;

import android.app.Application;

import com.appizona.yehiahd.fastsave.FastSave;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.module.AppGlideModule;

import ru.lantimat.photogallery.API.RetrofitClient;


public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RetrofitClient.initClient(getApplicationContext());
        FastSave.init(getApplicationContext());
    }
}
