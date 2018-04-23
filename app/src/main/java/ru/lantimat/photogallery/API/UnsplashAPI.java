package ru.lantimat.photogallery.API;

import com.google.gson.Gson;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.lantimat.photogallery.models.Photo;

/**
 * Created by GabdrakhmanovII on 08.12.2017.
 */

public interface UnsplashAPI {

    @GET("photos")
    Observable<ArrayList<Photo>> getRandomPhotos(@Query("page") int page,
                                                 @Query("per_page") int perPage);

    /*@GET("objects/{Id}/packets")
    Observable<ArrayList<TrackR>> getTrack(@Path("Id") String customerId,
                                           @Query("begin") long begin,
                                           @Query("end") long end);*/

}
