package com.example.britt.walkmydog;

import java.util.ArrayList;

/**
 * Create class for a user, with email, score and password.
 */

public class User {
    public String userType;
    public String name;
    public String email;
    public Boolean advert_status;
    public ArrayList favorites;

    public User () {}

    public User(String userType, String name, String email, Boolean advert_status, ArrayList favorites) {
        this.userType = userType;
        this.name = name;
        this.email = email;
        this.advert_status = advert_status;
        this.favorites = favorites;
    }
}