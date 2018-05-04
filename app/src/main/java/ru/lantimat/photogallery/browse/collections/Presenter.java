package ru.lantimat.photogallery.browse.collections;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.lantimat.photogallery.API.ApiUtils;
import ru.lantimat.photogallery.API.UnsplashAPI;
import ru.lantimat.photogallery.browse.fullScreenImage.FullScreenImageActivity;
import ru.lantimat.photogallery.browse.photos.CategoryImagesListActivity;
import ru.lantimat.photogallery.browse.photos.ImagesListFragment;
import ru.lantimat.photogallery.collectionModel.Collection;
import ru.lantimat.photogallery.photosModel.Urls;
import ru.lantimat.photogallery.utils.ArraySaveHelper;
import ru.lantimat.photogallery.utils.Constants;
import ru.lantimat.photogallery.utils.Utils;

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

    public Presenter(String orderBy, ArrayList<Collection> ar, int page) {
        api = ApiUtils.getUnsplashAPI();
        this.orderBy = orderBy;
        this.ar.addAll(ar);
        this.page = page;
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
        intent.putExtra(CategoryImagesListActivity.TITLE, ar.get(position).getTitle());
        view.showCategoryImagesList(intent);
    }

    @Override
    public void saveInstance(Context context, Bundle bundle) {
        ArraySaveHelper saveHelper = new ArraySaveHelper();
        saveHelper.saveArrayList(context, ar, Constants.PARAM_AR);
        bundle.putInt(Constants.PARAM_PAGE, page);
        bundle.putString(Constants.PARAM_ORDER_BY, orderBy);
        view.onSaveInstance(bundle);
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
