package com.example.britt.walkmydog;

// TODO: hierin commenten en evt functies weghalen?

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object obj) {
        boolean retVal = false;

        if (obj instanceof Dog){
            Dog ptr = (Dog) obj;

            // compare IDs of the owner
            retVal = ptr.id.equals(this.id);
        }

        return retVal;
    }

    /* https://stackoverflow.com/questions/8322129/arraylists-custom-contains-method */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
}
