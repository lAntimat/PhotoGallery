package ru.lantimat.photogallery.browse.photos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.widget.ImageView;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import ru.lantimat.photogallery.API.ApiUtils;
import ru.lantimat.photogallery.API.UnsplashAPI;
import ru.lantimat.photogallery.browse.fullScreenImage.FullScreenImageActivity;
import ru.lantimat.photogallery.photosModel.Photo;
import ru.lantimat.photogallery.photosModel.Urls;
import ru.lantimat.photogallery.utils.Constants;

public class Presenter implements PhotosMVP.Presenter {

    //Виды сортировки фотографий
    public static String ORDER_BY = "orderBy";
    public static String SORT_LATEST = "latest"; //Новейшие
    public static String SORT_OLDEST = "oldest"; //Старейшие
    public static String SORT_POPULAR = "popular"; //Популярные
    private String orderBy = "";
    private String id;

    private static int PER_PAGE = 50; //Количество Items за один запрос
    private PhotosMVP.View view;
    private UnsplashAPI api;
    private DisposableObserver disposable;
    private int page = 1;
    private boolean isLoading = false;
    private static boolean isOnRefresh = false;
    private ArrayList<Photo> ar = new ArrayList<>();
    private ArrayList<Urls> arUrls = new ArrayList<>();
    private boolean isLoadById = false;

    public Presenter(String orderBy) {
        api = ApiUtils.getUnsplashAPI();
        this.orderBy = orderBy;
        isLoadById = false;
    }

    public Presenter(String id, boolean isLoadById) {
        api = ApiUtils.getUnsplashAPI();
        this.id = id;
        this.isLoadById = isLoadById;
    }

    public Presenter(String orderBy, ArrayList<Urls> ar, int page) {
        api = ApiUtils.getUnsplashAPI();
        this.orderBy = orderBy;
        this.arUrls.addAll(ar);
        this.page = page;
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
        view.showLoading(false);
    }

    @Override
    public void loadMore() {
        if (!isLoading) {
            isLoading = true;
            page++;
            Log.d("page", "page=" + page);
            loadPhotos(page);
            view.showLoading(true);
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
    public void setPage(int page) {
        this.page = page;
    }

    @Override
    public void itemClick(Context context, int position, ImageView imageView) {
        Intent intent = new Intent(context, FullScreenImageActivity.class);
        intent.putExtra(Constants.PARAM_AR, arUrls);
        intent.putExtra(Constants.PARAM_POSITION, position);
        intent.putExtra(Constants.PARAM_PAGE, page);
        intent.putExtra(Constants.PARAM_ORDER_BY, orderBy);
//        intent.putExtra(EXTRA_ANIMAL_IMAGE_TRANSITION_NAME, ViewCompat.getTransitionName(imageView));

        view.onItemClick(intent, imageView);
    }

    @Override
    public void backPressed(Context context, int position) {
        Intent intent = new Intent();
        intent.putExtra(Constants.PARAM_AR ,arUrls);
        intent.putExtra(Constants.PARAM_POSITION, position);
        intent.putExtra(Constants.PARAM_PAGE, page);
        intent.putExtra(Constants.PARAM_ORDER_BY, orderBy);
        view.onBackPressed(intent);
    }

    @Override
    public void saveInstance(Bundle bundle) {
        bundle.putParcelableArrayList(Constants.PARAM_AR ,arUrls);
        bundle.putInt(Constants.PARAM_PAGE, page);
        bundle.putString(Constants.PARAM_ORDER_BY, orderBy);
        view.onSaveInstance(bundle);
    }

    private void loadPhotos(int page) {
        if (!isOnRefresh) { //При обновлении при помощи SwipeRefreshLayout ProgressBar не будет показыватсья
            if(page==1) view.showLoading(false);
            else view.showLoading(true);
        }
        disposable = new DisposableObserver<ArrayList<Photo>>() {
            @Override
            public void onNext(ArrayList<Photo> photos) {
                isLoading = false;
                isOnRefresh = false;
                ar.addAll(photos);
                view.hideLoading();

                //массив содержащий только url
                //arUrls.clear();
                arUrls.addAll(makeUrlsArrayList(photos));
                view.showPhotos(arUrls);
                if(photos.size()==0) view.noMoreItems();

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

        if(isLoadById) {
            api.getCollectionPhotos(id, page, PER_PAGE)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(disposable);
        } else {
            api.getPhotos(page, PER_PAGE, orderBy)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(disposable);
        }
    }

    private ArrayList<Urls> makeUrlsArrayList(ArrayList<Photo> ar) {
        ArrayList<Urls> arUrlTemp = new ArrayList<>();
        for (int i = 0; i < ar.size(); i++) {
            arUrlTemp.add(ar.get(i).getUrls());
        }
        return arUrlTemp;
    }
}
