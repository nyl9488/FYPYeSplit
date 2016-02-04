package com.example.yloon.fypandroidapp.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yloon.fypandroidapp.R;

public class Feedback extends AppCompatActivity {
private EditText ed_fbBody;
    private EditText ed_fbTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
    }

    public void sendOnClick(View V) {
        ed_fbBody= (EditText) findViewById(R.id.ed_fbBody);
        ed_fbTitle= (EditText) findViewById(R.id.ed_fbTitle);


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"nyl9488.yln@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, ed_fbTitle.getText().toString());
        i.putExtra(Intent.EXTRA_TEXT   , ed_fbBody.getText().toString());
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Feedback.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
