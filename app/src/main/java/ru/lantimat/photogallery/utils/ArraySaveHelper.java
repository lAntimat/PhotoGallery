package ru.lantimat.photogallery.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import ru.lantimat.photogallery.collectionModel.Collection;

public class ArraySaveHelper {

    /**
     *     Save and get ArrayList in SharedPreference
     */

    public void saveArrayList(Context context, ArrayList<Collection> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<Collection> getArrayList(Context context, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<Collection>>() {}.getType();
        ArrayList<Collection> ar = (ArrayList<Collection>) gson.fromJson(json, type);
        return ar;
    }

}
