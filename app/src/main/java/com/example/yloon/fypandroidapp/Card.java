package com.example.yloon.fypandroidapp;


import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class Card extends AppCompatActivity {

    ImageView imgFront;
    ImageView imgBack;
    Button btnFlip;

    boolean isBackVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

        imgFront = (ImageView)findViewById(R.id.imgFront);
        imgBack = (ImageView)findViewById(R.id.imgBack);
       // btnFlip = (Button)findViewById(R.id.btnFlip);

        final AnimatorSet setRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flip_right_out);


        final AnimatorSet setLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(getApplicationContext(),
                R.animator.flight_left_in);


        imgFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isBackVisible) {
                    setRightOut.setTarget(imgFront);
                    setLeftIn.setTarget(imgBack);
                    setRightOut.start();
                    setLeftIn.start();
                    isBackVisible = true;
                } else {
                    setRightOut.setTarget(imgBack);
                    setLeftIn.setTarget(imgFront);
                    setRightOut.start();
                    setLeftIn.start();
                    isBackVisible = false;
                }
            }
        });
    }




    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
