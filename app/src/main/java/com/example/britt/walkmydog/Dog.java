package com.example.britt.walkmydog;

import android.widget.ImageView;

/**
 * Created by britt on 10-1-2018.
 */

public class Dog {
    public String name;
    public String description;
    public String photo;
    public Double lat;
    public Double lon;
    public String id;

    public Dog() {}

    public Dog(String name, String description, String photo, Double lat, Double lon, String id) {
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.lat = lat;
        this.lon = lon;
        this.id = id;
    }
}
