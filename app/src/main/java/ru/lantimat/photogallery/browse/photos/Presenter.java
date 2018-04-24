package ru.lantimat.photogallery.browse.photos;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.lantimat.photogallery.API.ApiUtils;
import ru.lantimat.photogallery.API.UnsplashAPI;
import ru.lantimat.photogallery.browse.photos.PhotosMVP;
import ru.lantimat.photogallery.photosModel.Photo;

public class Presenter implements PhotosMVP.Presenter {

    //Виды сортировки фотографий
    public static String SORT_LATEST = "latest"; //Новейшие
    public static String SORT_OLDEST = "oldest"; //Старейшие
    public static String SORT_POPULAR = "popular"; //Популярные
    private String orderBy = SORT_LATEST; //По умолчанию latest

    private static int PER_PAGE = 50; //Количество Items за один запрос
    private PhotosMVP.View view;
    private UnsplashAPI api;
    private DisposableObserver disposable;
    private int page = 1;
    private boolean isLoading = false;
    private static boolean isOnRefresh = false;
    private ArrayList<Photo> ar = new ArrayList<>();

    public Presenter(String orderBy) {
        this.orderBy = orderBy;
        api = ApiUtils.getUnsplashAPI();
    }

    @Override
    public void attachView(PhotosMVP.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void getPhotos() {
        ar.clear();
        page = 1;
        loadPhotos(page);
    }

    @Override
    public void loadMore() {
        if(!isLoading) {
            isLoading = true;
            page++;
            loadPhotos(page);
        }
    }

    @Override
    public void onRefresh() {
        ar.clear();
        page = 1;
        isOnRefresh = true;
        loadPhotos(page);
    }

    @Override
    public void itemClick(int position) {
        view.onItemClick(position);
    }

    private void loadPhotos(int page) {
        if (!isOnRefresh) view.showLoading(); //При обновлении при помощи SwipeRefreshLayout ProgressBar не будет показыватсья
        disposable = new DisposableObserver<ArrayList<Photo>>() {
            @Override
            public void onNext(ArrayList<Photo> photos) {
                isLoading = false;
                isOnRefresh = false;
                ar.addAll(photos);
                view.hideLoading();
                view.showPhotos(ar);
            }

            @Override
            public void onError(Throwable e) {
                view.hideLoading();
                view.showError(e.getLocalizedMessage());
            }

            @Override
            public void onComplete() {

            }
        };

        api.getPhotos(page, PER_PAGE, orderBy)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(disposable);
    }


}
