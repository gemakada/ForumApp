package com.example.giorgos.forumapp;

/**
 * Created by makan on 11/2/2017.
 */

public class Friend {
    private String Name;
    private String key;


    public Friend(String name, String key) {
        this.Name=name;
        this.key=key;
    }

    public String getKey() {
        return this.key;
    }
    public String getName() {
        return this.Name;
    }

}
