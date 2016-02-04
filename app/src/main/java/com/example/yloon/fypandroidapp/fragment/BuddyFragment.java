package com.example.yloon.fypandroidapp.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.yloon.fypandroidapp.Card;
import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.adapter.CustomRoasterListAdapter;
import com.example.yloon.fypandroidapp.service.XxmpConnection;
import com.example.yloon.fypandroidapp.view.About_Activity;
import com.example.yloon.fypandroidapp.view.Feedback;
import com.example.yloon.fypandroidapp.view.SettingsActivity;
import com.example.yloon.fypandroidapp.view.inviteContact_Activity;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;

import java.util.ArrayList;
import java.util.Collection;


public class BuddyFragment extends Fragment  {

    private ListView lv;

    Roster roster = XxmpConnection.getConnection().getRoster();
    Collection <RosterEntry> entries = roster.getEntries();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_buddy, container, false);
        lv = (ListView) view.findViewById(R.id.list_contact);

        ArrayList<String> strArr=new ArrayList<String>();


        for (RosterEntry entry : entries) {

            strArr.add(entry.getUser());

        }

        lv.setAdapter(new CustomRoasterListAdapter(getContext(), strArr));
                //Log.i("---", "tyep: "+entry.getType());
                //Log.i("---", "status: "+entry.getStatus());
                //Log.i("---", "groups: "+entry.getGroups());

        setHasOptionsMenu(true);
        return view;

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_contact, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
     @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //When Product action item is clicked
        if (id == R.id.action_about) {
            //Create Intent for Product Activity
            Intent productIntent = new Intent(getActivity(),About_Activity.class);
            //Start Product Activity
            startActivity(productIntent);
            return true;
        }
        else if (id == R.id.action_feedback) {
            //Create Intent for Product Activity
            Intent productIntent = new Intent(getActivity(),Feedback.class);
            //Start Product Activity
            startActivity(productIntent);
            return true;
        }
        else if (id == R.id.action_card) {
            //Create Intent for Product Activity
            Intent productIntent = new Intent(getActivity(),Card.class);
            //Start Product Activity
            startActivity(productIntent);
            return true;
        }
        else if (id == R.id.action_settings) {
            //Create Intent for Product Activity
            Intent productIntent = new Intent(getActivity(),SettingsActivity.class);
            //Start Product Activity
            startActivity(productIntent);
            return true;
        }
        else if (id == R.id.action_invitation) {
             //Create Intent for Product Activity
             Intent productIntent = new Intent(getActivity(),inviteContact_Activity.class);
             //Start Product Activity
             startActivity(productIntent);
             return true;
         }


        return super.onOptionsItemSelected(item);
    }

}
