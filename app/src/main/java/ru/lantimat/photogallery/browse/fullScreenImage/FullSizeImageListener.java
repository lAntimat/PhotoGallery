package ru.lantimat.photogallery.browse.fullScreenImage;

import java.util.ArrayList;

import ru.lantimat.photogallery.photosModel.Photo;

public interface FullSizeImageListener {

    void onAdd(ArrayList<Photo> ar);

    void setPosition(int position);
}

