package com.example.britt.walkmydog;

import android.widget.ImageView;

/**
 * Created by britt on 10-1-2018.
 */

public class Dog {
    public String name;
    public String description;
    public ImageView photo;
    public Double lat;
    public Double lon;

    public Dog() {}

    public Dog(String name, String description, ImageView photo, Double lat, Double lon) {
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.lat = lat;
        this.lon = lon;
    }
}
