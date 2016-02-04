package com.example.yloon.fypandroidapp.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.server.RequestHandler;
import com.example.yloon.fypandroidapp.server.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GroupProfile extends AppCompatActivity {
    TextView txt_GroupType;
    TextView txt_GroupName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_profile);
        txt_GroupType = (TextView)findViewById(R.id.txt_GroupType);
        txt_GroupName = (TextView)findViewById(R.id.txt_GroupName);
        getProfile();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_groupprofile, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //When Product action item is clicked
        if (id == R.id.action_deletegroup) {
            //Create Intent for Product Activity
            new AlertDialog.Builder(GroupProfile.this)
                    .setTitle("Delete entry")
                    .setMessage("Are you sure you want to delete this group?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // continue with delete
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // do nothing
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return true;
        }

        else if (id == R.id.action_editgroup) {
            //Create Intent for Product Activity
            Intent productIntent = new Intent(getApplicationContext(),EditGroup.class);
            //Start Product Activity
            startActivity(productIntent);
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    private void getProfile(){
        class getBill extends AsyncTask<Void,Void,String> {
            //ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(viewPager.this, "Fetching...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                showProfile(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                // final String memberid = sharedPreferences.getString(config.MemberID, "") ;
                Intent intent = getIntent();
                String groupTitle = intent.getStringExtra("G_ID");
                String s = rh.sendGetRequestParam(config.url_viewGroup, groupTitle);
                return s;
            }
        }
        getBill ge = new getBill();
        ge.execute();
    }

    private void showProfile(String json){
        JSONObject jsonObject = null;



        try {
            jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String name = jo.getString("name");
                String type = jo.getString("type");
                /*HashMap<String,String> employees = new HashMap<>();
                employees.put(config.KEY_GROUP_NAME,name);
                employees.put(config.KEY_GROUP_ID,type);
*/
                txt_GroupType.setText(type);
                txt_GroupName.setText(name);


            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
