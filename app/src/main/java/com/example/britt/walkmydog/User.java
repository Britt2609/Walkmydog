package com.example.britt.walkmydog;

import java.util.ArrayList;

/**
 * Create class for a user, with email, score and password.
 */

public class User {
    public String userType;
    public String name;
    public String email;
    // TODO: advert_status nog weghalen!!!!!!!!!!!!!!!!
    public Boolean advertState;
    public ArrayList<Dog> favorites;

    public User () {}

    public User(String userType, String name, String email, Boolean advertState, ArrayList<Dog> favorites) {
        this.userType = userType;
        this.name = name;
        this.email = email;
        this.advertState = advertState;
        this.favorites = favorites;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdvert_status() {
        return advertState;
    }

    public void setAdvert_status(Boolean advert_status) {
        this.advertState = advert_status;
    }

    public ArrayList<Dog> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<Dog> favorites) {
        this.favorites = favorites;
    }
}