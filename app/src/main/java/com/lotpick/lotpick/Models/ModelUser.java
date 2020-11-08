package com.lotpick.lotpick.Models;

public class ModelUser {
    private String id,username,imageurl;

    public ModelUser(String id, String username, String imageurl) {
        this.id = id;
        this.username = username;
        this.imageurl = imageurl;

    }

    public ModelUser(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
