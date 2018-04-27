package ru.lantimat.photogallery.utils;

public interface BaseView {

    void showLoading(boolean isLoadMore);

    void hideLoading();

    void showError(String error);
}
