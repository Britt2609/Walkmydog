package com.example.britt.walkmydog;

import android.widget.ImageView;

/**
 * Created by britt on 10-1-2018.
 */

public class Dogs {
    public String name;
    public String description;
    //public ImageView photo;
    public Double lat;
    public Double lon;
    public Long id;

    public Dogs() {}

    public Dogs(String name, String description, Double lat, Double lon, Long id) {
        this.name = name;
        this.description = description;
        //this.photo = photo;
        this.lat = lat;
        this.lon = lon;
        this.id = id;
    }
}