package ru.lantimat.photogallery.utils;

import android.app.Activity;
import android.content.res.Configuration;

public class Utils {

    public static boolean isPortraitMode(Activity activity) {
        if(activity.getResources()!=null) {
            return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT; //Возвращаем true если портретный режим
        } else return true;
    }
}
