package ru.lantimat.photogallery.ui.category;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;

import ru.lantimat.photogallery.models.Collection;
import ru.lantimat.photogallery.utils.BasePresenter;
import ru.lantimat.photogallery.utils.BaseView;

public interface CategoryMVP {

    interface Presenter extends BasePresenter<View> {
        void getCollections();
        void loadMore();
        void onRefresh();
        void itemClick(Context context, int position);
        void saveInstance(Context context, Bundle bundle);

    }

    interface View extends BaseView {
        void showCollections(ArrayList<Collection> ar);
        void showCategoryImagesList(Intent intent);
        void onSaveInstance(Bundle bundle);
    }
}
