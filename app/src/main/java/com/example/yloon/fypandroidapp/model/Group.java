package com.example.yloon.fypandroidapp.model;

/**
 * Created by YLoon on 15/12/2015.
 */
public class Group {
    public String title;
    public String groupType;
    public String id;


    public Group(String title, String description) {
        this.title = title;
        this.groupType = description;

    }

    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }
}