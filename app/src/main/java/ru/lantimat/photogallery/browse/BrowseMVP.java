package ru.lantimat.photogallery.browse;

import java.util.ArrayList;

import ru.lantimat.photogallery.models.Photo;
import ru.lantimat.photogallery.utils.BasePresenter;
import ru.lantimat.photogallery.utils.BaseView;

public interface BrowseMVP {

    interface Presenter extends BasePresenter<View> {
        void getPhotos();
        void loadMore();
        void onRefresh();
    }

    interface View extends BaseView {
        void showPhotos(ArrayList<Photo> ar);
    }
}
