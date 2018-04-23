package ru.lantimat.photogallery.browse;

import java.util.ArrayList;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.lantimat.photogallery.API.ApiUtils;
import ru.lantimat.photogallery.API.UnsplashAPI;
import ru.lantimat.photogallery.models.Photo;

public class Presenter implements BrowseMVP.Presenter {

    private static int PER_PAGE = 30; //Количество Items за один запрос
    private BrowseMVP.View view;
    private UnsplashAPI api;
    private DisposableObserver disposable;
    private int page = 1;
    private boolean isLoading = false;
    private static boolean isOnRefresh = false;
    private ArrayList<Photo> ar = new ArrayList<>();

    public Presenter() {
        api = ApiUtils.getUnsplashAPI();
    }

    @Override
    public void attachView(BrowseMVP.View view) {
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

        api.getRandomPhotos(page, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(disposable);
    }


}
