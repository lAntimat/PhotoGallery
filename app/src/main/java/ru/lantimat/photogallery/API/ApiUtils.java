package ru.lantimat.photogallery.API;

/**
 * Created by GabdrakhmanovII on 08.12.2017.
 */

public class ApiUtils {

    public static final String BASE_URL = "https://api.unsplash.com/";

    public static UnsplashAPI getUnsplashAPI() {
        return RetrofitClient.getClient(BASE_URL).create(UnsplashAPI.class);
    }
}
