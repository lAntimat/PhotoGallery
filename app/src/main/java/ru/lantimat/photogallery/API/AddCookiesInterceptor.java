package ru.lantimat.photogallery.API;


import android.util.Log;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Interceptor.Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        //client_id для доступа к API
        String client_id = "Client-ID 4b64ec52a8d87652628428615a940cc0f71e3a88711adc585f3e7b3010069c9b";

            builder.addHeader("Authorization", client_id);
            Log.v("OkHttp", "Adding Header: " + client_id);
        return chain.proceed(builder.build());
    }
}
