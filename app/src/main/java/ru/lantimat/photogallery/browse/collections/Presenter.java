package ru.lantimat.photogallery.browse.collections;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.lantimat.photogallery.API.ApiUtils;
import ru.lantimat.photogallery.API.UnsplashAPI;
import ru.lantimat.photogallery.browse.photos.CategoryImagesListActivity;
import ru.lantimat.photogallery.browse.photos.ImagesListFragment;
import ru.lantimat.photogallery.collectionModel.Collection;

public class Presenter implements CollectionMVP.Presenter {

    //Виды сортировки фотографий
    public static String SORT_LATEST = "latest"; //Новейшие
    public static String SORT_OLDEST = "oldest"; //Старейшие
    public static String SORT_POPULAR = "popular"; //Популярные
    private String orderBy = SORT_LATEST; //По умолчанию latest

    private static int PER_PAGE = 50; //Количество Items за один запрос
    private CollectionMVP.View view;
    private UnsplashAPI api;
    private DisposableObserver disposable;
    private int page = 1;
    private boolean isLoading = false;
    private static boolean isOnRefresh = false;
    private ArrayList<Collection> ar = new ArrayList<>();

    public Presenter(String orderBy) {
        this.orderBy = orderBy;
        api = ApiUtils.getUnsplashAPI();
    }

    @Override
    public void attachView(CollectionMVP.View view) {
        this.view = view;
    }

    @Override
    public void detachView() {

    }

    @Override
    public void getCollections() {
        ar.clear();
        page = 1;
        loadCollections(page);
        view.showLoading(false);
    }

    @Override
    public void loadMore() {
        if(!isLoading) {
            isLoading = true;
            page++;
            loadCollections(page);
            view.showLoading(true);
        }
    }

    @Override
    public void onRefresh() {
        ar.clear();
        page = 1;
        isOnRefresh = true;
        loadCollections(page);
    }

    @Override
    public void itemClick(Context context, int position) {
        Intent intent = new Intent(context, CategoryImagesListActivity.class);
        intent.putExtra(ImagesListFragment.ID, ar.get(position).getId().toString());
        view.showCategoryImagesList(intent);
    }

    private void loadCollections(int page) {
        disposable = new DisposableObserver<ArrayList<Collection>>() {
            @Override
            public void onNext(ArrayList<Collection> collections) {
                isLoading = false;
                isOnRefresh = false;
                ar.addAll(collections);
                view.hideLoading();
                view.showCollections(ar);
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

        api.getCollections(page, PER_PAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(disposable);

    }


}
