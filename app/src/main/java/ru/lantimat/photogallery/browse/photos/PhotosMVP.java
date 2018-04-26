package ru.lantimat.photogallery.browse.photos;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.widget.ImageView;

import java.util.ArrayList;

import ru.lantimat.photogallery.photosModel.Photo;
import ru.lantimat.photogallery.photosModel.Urls;
import ru.lantimat.photogallery.utils.BasePresenter;
import ru.lantimat.photogallery.utils.BaseView;

public interface PhotosMVP {

    interface Presenter extends BasePresenter<View> {
        void getPhotos();
        void loadMore();
        void onRefresh();
        void setPage(int page);
        void itemClick(Context context, int position, ImageView view);
        void backPressed(Context context, int position);

    }

    interface View extends BaseView {
        void showPhotos(ArrayList<Urls> ar);
        void onItemClick(Intent intent, ImageView imageView);
        void onBackPressed(Intent intent);
        void noMoreItems();
    }
}
