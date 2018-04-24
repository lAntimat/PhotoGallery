package ru.lantimat.photogallery.API;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.lantimat.photogallery.collectionModel.Collection;
import ru.lantimat.photogallery.photosModel.Photo;

/**
 * Created by GabdrakhmanovII on 08.12.2017.
 */

public interface UnsplashAPI {

    @GET("photos")
    Observable<ArrayList<Photo>> getPhotos(@Query("page") int page,
                                                 @Query("per_page") int perPage,
                                                 @Query("order_by") String orderBy);


    @GET("collections")
    Observable<ArrayList<Collection>> getCollections(@Query("page") int page,
                                                     @Query("per_page") int perPage,
                                                     @Query("order_by") String orderBy);
    /*@GET("objects/{Id}/packets")
    Observable<ArrayList<TrackR>> getTrack(@Path("Id") String customerId,
                                           @Query("begin") long begin,
                                           @Query("end") long end);*/

}
