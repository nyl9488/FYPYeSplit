package com.example.yloon.fypandroidapp.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.yloon.fypandroidapp.R;

public class MainScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onRegisterClick(View v){
        startActivity(new Intent(this, RegisterActivity.class));
    }

    public void onMainLoginClick(View v){
        startActivity(new Intent(this, LoginActivity.class));

    }
}
