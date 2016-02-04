package com.example.yloon.fypandroidapp.view;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;

import com.example.yloon.fypandroidapp.R;


public class SplashScreen extends FragmentActivity{

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        LoadPreferences();

    }
    private void LoadPreferences(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String  data = sharedPreferences.getString("emailKey", "") ;

        mHandler = new Handler();
        if (data!="") {
         //   Toast.makeText(getApplicationContext(),""+data,Toast.LENGTH_LONG);
            mHandler.postDelayed(gotoLoginAct, 3000);
        }
        else {
         //   Toast.makeText(getApplicationContext(),""+data,Toast.LENGTH_LONG);
            mHandler.postDelayed(gotoMainAct, 3000);
        }
    }
    Runnable gotoLoginAct = new Runnable() {

        @Override
        public void run() {
            startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            finish();
        }
    };

    Runnable gotoMainAct = new Runnable() {

        @Override
        public void run() {
            startActivity(new Intent(SplashScreen.this, MainScreen.class));
            finish();
        }
    };




}
