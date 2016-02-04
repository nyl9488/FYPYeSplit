package com.example.yloon.fypandroidapp.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.adapter.ContactListAdapter;
import com.example.yloon.fypandroidapp.service.GetNumber;

public class inviteContact_Activity extends AppCompatActivity {

    private ListView lv;
    private ContactListAdapter adapter;

    private String a;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_contact);


        lv = (ListView) findViewById(R.id.lv);

        GetNumber.getNumber(getApplicationContext());
        adapter = new ContactListAdapter(GetNumber.lists, getApplicationContext());


        lv.setAdapter(adapter);

//        }


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        lv.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(), "You selected : " + GetNumber.lists.get(position).getName() + position, Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDestroy() {
        GetNumber.lists.clear(); //clear contact list to remove duplicated contact
        lv.setAdapter(null);
        super.onDestroy();

    }
}
