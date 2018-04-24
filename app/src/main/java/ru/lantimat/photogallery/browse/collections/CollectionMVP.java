package ru.lantimat.photogallery.browse.collections;

import java.util.ArrayList;

import ru.lantimat.photogallery.collectionModel.Collection;
import ru.lantimat.photogallery.photosModel.Photo;
import ru.lantimat.photogallery.utils.BasePresenter;
import ru.lantimat.photogallery.utils.BaseView;

public interface CollectionMVP {

    interface Presenter extends BasePresenter<View> {
        void getCollections();
        void loadMore();
        void onRefresh();
    }

    interface View extends BaseView {
        void showCollections(ArrayList<Collection> ar);
    }
}
