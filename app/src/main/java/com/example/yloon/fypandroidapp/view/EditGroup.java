package com.example.yloon.fypandroidapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.yloon.fypandroidapp.R;

public class EditGroup extends AppCompatActivity {
    String[] text0 = { "Select Type", "House","Apartment","Trip","Other"};
    Spinner spinnerDropDown;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_group);

        spinnerDropDown =(Spinner)findViewById(R.id.sp_editGroupType);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
                R.layout.simple_spinner_dropdown_item ,text0);

        spinnerDropDown.setAdapter(adapter);

    }
}
