package com.example.yloon.fypandroidapp.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.adapter.SingleChatListAdapter;
import com.example.yloon.fypandroidapp.service.XxmpConnection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import java.util.ArrayList;
import java.util.Collection;

public class add_chatRoom_Activity extends AppCompatActivity {
    private ListView lv;
    private ArrayList<String> strArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_chat_room);

        TextView textView = new TextView(getApplicationContext());

        textView.setTextColor(getResources().getColor(R.color.cardview_dark_background));
        textView.setText("  CHOOSE ONE FROM YOUR CONTACT LIST.");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(20, 20, 20, 20);
        textView.setBackgroundColor(Color.parseColor("#dcf7e2"));


        lv = (ListView) findViewById(R.id.list_inviteContact);
        lv.addHeaderView(textView);

        strArr = new ArrayList<String>();



        Roster roster = XxmpConnection.getConnection().getRoster();

        Collection <RosterEntry> entries = roster.getEntries();

        for (RosterEntry entry : entries) {

            strArr.add(entry.getUser());

        }

        lv.setAdapter(new SingleChatListAdapter(this, strArr));


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView text = (TextView) view.findViewById(R.id.buddyText);

                String selectedFromList = text.getText().toString().trim();

                Intent intent = new Intent();
                intent.putExtra("edittextvalue", selectedFromList);
                setResult(RESULT_OK, intent);
                Toast.makeText(getApplicationContext(), "" + selectedFromList, Toast.LENGTH_LONG).show();
                finish();


            }
        });
    }


}
