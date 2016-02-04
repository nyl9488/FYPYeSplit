package com.example.yloon.fypandroidapp.model;

/**
 * Created by YLoon on 15/12/2015.
 */
public class Bill {
    public String title;
    public String date;
    public String id;


    public Bill(String title, String date) {
        this.title = title;
        this.date = date;

    }
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

}