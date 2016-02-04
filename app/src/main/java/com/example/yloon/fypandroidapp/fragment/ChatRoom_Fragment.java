package com.example.yloon.fypandroidapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTabHost;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import com.example.yloon.fypandroidapp.Card;
import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.view.About_Activity;
import com.example.yloon.fypandroidapp.view.Feedback;
import com.example.yloon.fypandroidapp.view.SettingsActivity;

public class ChatRoom_Fragment extends Fragment {
    private FragmentTabHost mTabHost;

    //Mandatory Constructor
    public ChatRoom_Fragment() {
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat_room_,container, false);
        setHasOptionsMenu(true);
        mTabHost = (FragmentTabHost)rootView.findViewById(android.R.id.tabhost);
        mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.realtabcontent);

        mTabHost.addTab(mTabHost.newTabSpec("One-to-one").setIndicator("One-to-one"),
                SingleChatFragment.class, null);
        mTabHost.addTab(mTabHost.newTabSpec("Multi").setIndicator("Multi"),
                MultiChatFragment.class, null);



        return rootView;

        //  strArr = new ArrayList<String>();

      /*  lv = (ListView) mTabHost.findViewById(R.id.list_chatlist);


        txtChatEmpty=(TextView)mTabHost.findViewById(R.id.txtChatEmpty);

        txtChatEmpty.setVisibility(View.VISIBLE);

        //lv.setAdapter(new CustomChatListAdapter(getContext(), strArr));
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                txtChatName=(TextView)view.findViewById(R.id.txtChatName);
                String value=txtChatName.getText().toString();
                Intent intent = new Intent(getActivity(), SingleChat_activity.class);
                intent.putExtra("chatname", value+"@kristng");
                startActivity(intent);

            }
        });*/

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mTabHost = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_chat_room_, menu);
        super.onCreateOptionsMenu(menu, inflater);
        //    ((viewPager) getActivity()).setActionBarTitle("Chat");
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
        else if (id == R.id.action_card) {
            //Create Intent for Product Activity
            Intent productIntent = new Intent(getActivity(),Card.class);
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
        else if (id == R.id.action_settings) {
            //Create Intent for Product Activity
            Intent productIntent = new Intent(getActivity(),SettingsActivity.class);
            //Start Product Activity
            startActivity(productIntent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }


}
