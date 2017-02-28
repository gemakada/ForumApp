package com.example.giorgos.forumapp;

/**
 * Created by makan on 29/1/2017.
 */

public class Discussion {

    private String message;
    private String user;


    public void Set(String message, String user) {
        this.message=message;
        this.user=user;
    }
    public String Get_user() {
        return this.user;


    }

    public String Get_message() {
        return this.message;


    }


}