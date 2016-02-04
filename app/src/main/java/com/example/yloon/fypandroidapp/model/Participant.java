package com.example.yloon.fypandroidapp.model;

/**
 * Created by YLoon on 15/12/2015.
 */
public class Participant {
    public String name;
    public String amount;


    public Participant(String name, String amount) {
        this.name = name;
        this.amount = amount;

    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }


}