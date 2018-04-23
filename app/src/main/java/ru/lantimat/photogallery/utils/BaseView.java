package ru.lantimat.photogallery.utils;

public interface BaseView {

    void showLoading();

    void hideLoading();

    void showError(String error);
}
