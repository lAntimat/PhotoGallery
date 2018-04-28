package ru.lantimat.photogallery.browse.fullScreenImage;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import ru.lantimat.photogallery.photosModel.Urls;

public class GalleryPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<Urls> images;

    public GalleryPagerAdapter(FragmentManager fm, ArrayList<Urls> images) {
        super(fm);
        this.images = images;
    }

    @Override
    public Fragment getItem(int position) {
        Urls image = images.get(position);
        return ImageDetailFragment.newInstance(image);
    }

    @Override
    public int getCount() {
        return images.size();
    }
}
