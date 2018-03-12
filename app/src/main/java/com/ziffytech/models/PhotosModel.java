package com.ziffytech.models;

/**
 * Created by subhashsanghani on 5/22/17.
 */

public class PhotosModel {
    private String id;
    private String bus_id;
    private String photo_title;
    private String photo_image;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBus_id() {
        return bus_id;
    }

    public void setBus_id(String bus_id) {
        this.bus_id = bus_id;
    }

    public String getPhoto_title() {
        return photo_title;
    }

    public void setPhoto_title(String photo_title) {
        this.photo_title = photo_title;
    }

    public String getPhoto_image() {
        return photo_image;
    }

    public void setPhoto_image(String photo_image) {
        this.photo_image = photo_image;
    }
}
