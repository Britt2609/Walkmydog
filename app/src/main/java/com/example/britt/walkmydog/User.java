package com.example.britt.walkmydog;

import java.util.ArrayList;


/**
 * Create class for a user, with email, name, user type, advert state and a list of walked dogs.
 */
public class User {
    public String userType;
    public String name;
    public String email;
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
}