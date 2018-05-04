package ru.lantimat.photogallery.API;

import java.util.ArrayList;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.lantimat.photogallery.models.Collection;
import ru.lantimat.photogallery.models.Photo;

/**
 * Created by GabdrakhmanovII on 08.12.2017.
 */

public interface UnsplashAPI {

    @GET("photos")
    Observable<ArrayList<Photo>> getPhotos(@Query("page") int page,
                                                 @Query("per_page") int perPage,
                                                 @Query("order_by") String orderBy);


    @GET("collections/featured")
    Observable<ArrayList<Collection>> getCollections(@Query("page") int page,
                                                     @Query("per_page") int perPage);
    @GET("collections/{Id}/photos")
    Observable<ArrayList<Photo>> getCollectionPhotos(@Path("Id") String collectionId,
                                                     @Query("page") int page,
                                                     @Query("per_page") int perPage);

}
