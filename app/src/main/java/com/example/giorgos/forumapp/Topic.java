package com.example.giorgos.forumapp;

/**
 * Created by makan on 28/1/2017.
 */

public class Topic {

    private String name;
    private String author;


    public void Set(String name, String author) {
        this.name=name;
        this.author=author;
    }
    public String Get_name() {
        return this.name;


    }

    public String Get_author() {
        return this.author;


    }


}
