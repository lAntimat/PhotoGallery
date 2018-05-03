package ru.lantimat.photogallery.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Utils {

    public static boolean isPortraitMode(Activity activity) {
        if(activity.getResources()!=null) {
            return activity.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT; //Возвращаем true если портретный режим
        } else return true;
    }
}
