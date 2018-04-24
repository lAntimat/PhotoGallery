
package ru.lantimat.photogallery.collectionModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PreviewPhoto {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("urls")
    @Expose
    private Urls_ urls;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Urls_ getUrls() {
        return urls;
    }

    public void setUrls(Urls_ urls) {
        this.urls = urls;
    }

}
