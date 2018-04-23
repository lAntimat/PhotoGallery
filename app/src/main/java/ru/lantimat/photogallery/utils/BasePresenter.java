package ru.lantimat.photogallery.utils;


public interface BasePresenter<V> {
    void attachView(V view);
    void detachView();
}
