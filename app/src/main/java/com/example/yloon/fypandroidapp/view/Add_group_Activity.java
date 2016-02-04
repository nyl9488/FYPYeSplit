package com.example.yloon.fypandroidapp.view;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.adapter.CustomSelectMberListAdapter;
import com.example.yloon.fypandroidapp.model.SBuddy;
import com.example.yloon.fypandroidapp.server.RequestHandler;
import com.example.yloon.fypandroidapp.server.config;
import com.example.yloon.fypandroidapp.service.XxmpConnection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Add_group_Activity extends AppCompatActivity {

    private EditText topicName,ed_test;
    CustomSelectMberListAdapter dataAdapter = null;
    String[] text0 = { "Select Type", "House","Apartment","Trip","Other"};
    Spinner spinnerDropDown;
    ArrayList<SBuddy> countryList = new ArrayList<SBuddy>();
    private String username;
    private String id;
    public static String groupid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        spinnerDropDown =(Spinner)findViewById(R.id.groupTypeSpinner);

        ArrayAdapter<String> adapter= new ArrayAdapter<String>(this,android.
                R.layout.simple_spinner_dropdown_item ,text0);

        spinnerDropDown.setAdapter(adapter);

        topicName = (EditText) findViewById(R.id.edTopicName);



        final ListView lv = (ListView) findViewById(R.id.addGroup_list);
        TextView textView = new TextView(getApplicationContext());

        textView.setTextColor(getResources().getColor(R.color.cardview_dark_background));
        textView.setText("  ADD GROUP MEMBERS");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(20, 20, 20, 20);
        textView.setBackgroundColor(Color.parseColor("#dcf7e2"));

        lv.addHeaderView(textView);
        displayListView();


    }

    private void displayListView() {


        Roster roster = XxmpConnection.getConnection().getRoster();
        Collection<RosterEntry> entries = roster.getEntries();

        // jid: String, user: String, groups: String[]
        for (RosterEntry entry : entries) {
            SBuddy group = new SBuddy(entry.getUser(),false);
            countryList.add(group);

        }
        //create an ArrayAdaptar from the String Array
        dataAdapter = new CustomSelectMberListAdapter(this,
                R.layout.listitem_addmember, countryList);
        ListView listView = (ListView) findViewById(R.id.addGroup_list);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        // strArr.add(nickname);
        dataAdapter.notifyDataSetChanged();


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                SBuddy group = (SBuddy) parent.getItemAtPosition(position);
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + group.getName(),
                        Toast.LENGTH_LONG).show();
            }
        });

    }




    public void onAddClick(View v){




        addGroup();


    }

    private void addGroup(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String  savedmemberID = sharedPreferences.getString(config.MemberID, "") ;

        final String groupName =topicName.getText().toString();

        final String groupType =spinnerDropDown.getSelectedItem().toString();


        class AddGroup extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Add_group_Activity.this, "Adding...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                showGroup(s);
                loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();




                StringBuffer responseText = new StringBuffer();

                for (int i = 0; i < countryList.size();i++){
                    SBuddy buddy = countryList.get(i);
                    if(buddy.isSelected()){
                        if(responseText.toString().equals("")){
                            responseText.append(buddy.getName());
                        }
                        else
                            responseText.append(","+buddy.getName());
                    }
                }
                params.put(config.KEY_GROUP_NAME,groupName);
                params.put(config.KEY_GROUP_TYPE, groupType);
                params.put(config.KEY_MEMBER_ID,savedmemberID);
                params.put("member",""+responseText);
                //     Toast.makeText(getApplicationContext(),
                //           responseText, Toast.LENGTH_LONG).show();

                Log.e("s", "Activity started" + responseText);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(config.url_addgroup, params);
                return res;
            }
        }

        AddGroup ae = new AddGroup();
        ae.execute();
    }

    private void showGroup(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);

            groupid = c.getString("lastid");
            final String groupName =topicName.getText().toString();
            final String groupType =spinnerDropDown.getSelectedItem().toString();

            Intent intent = new Intent();
            intent.putExtra("groupName",groupName);
            intent.putExtra("groupType", groupType);
            intent.putExtra("groupID", groupid);

            // intent.putExtra("groupID", groupid);
            setResult(RESULT_OK, intent);
            // Toast.makeText(getApplicationContext(), "" + groupName, Toast.LENGTH_LONG).show();


            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



}
