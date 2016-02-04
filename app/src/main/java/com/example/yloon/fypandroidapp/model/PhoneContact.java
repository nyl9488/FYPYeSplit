package com.example.yloon.fypandroidapp.model;

/**
 * Created by YLoon on 24/9/2015.
 */
public class PhoneContact {
    private String name;
    private String number;

    public PhoneContact(String name, String number) {
        setNumber(number);
        setName(name);
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }



}
