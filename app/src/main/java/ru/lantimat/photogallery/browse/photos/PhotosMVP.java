package ru.lantimat.photogallery.browse.photos;

import java.util.ArrayList;

import ru.lantimat.photogallery.photosModel.Photo;
import ru.lantimat.photogallery.utils.BasePresenter;
import ru.lantimat.photogallery.utils.BaseView;

public interface PhotosMVP {

    interface Presenter extends BasePresenter<View> {
        void getPhotos();
        void loadMore();
        void onRefresh();
        void itemClick(int position);
    }

    interface View extends BaseView {
        void showPhotos(ArrayList<Photo> ar);
        void onItemClick(int position, ArrayList<Photo> ar);
    }
}
