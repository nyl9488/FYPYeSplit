package com.example.yloon.fypandroidapp.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.adapter.CustomBillListAdapter;
import com.example.yloon.fypandroidapp.model.Bill;
import com.example.yloon.fypandroidapp.server.RequestHandler;
import com.example.yloon.fypandroidapp.server.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GroupInfoActivity extends AppCompatActivity {
    TextView txtEmptyBill;
    public static ArrayList<Bill> bills;
    private TextView txtBillTitle;
    CustomBillListAdapter mAdapter;
    ListView lv;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_group_info);

        bills = new ArrayList<Bill>();
        mAdapter = new CustomBillListAdapter(getApplicationContext(), bills);
        lv = (ListView) findViewById(R.id.list_bill);
        intent= getIntent();
        String groupTitle = intent.getStringExtra("GTITLE");

        //groupname = (TextView)findViewById(R.id.txtGroupName);
        //groupname.setText("sss" + easyPuzzle);
        setTitle(groupTitle);

        TextView textView = new TextView(getApplicationContext());

        textView.setTextColor(getResources().getColor(R.color.cardview_dark_background));
        textView.setText("  ALL EXPENSES: BILL LIST");
        textView.setTypeface(null, Typeface.BOLD);
        textView.setPadding(20, 20, 20, 20);
        textView.setBackgroundColor(Color.parseColor("#dcf7e2"));

        txtEmptyBill = (TextView) findViewById(R.id.txtEmptyBill);

        lv.addHeaderView(textView, null, false);
        if( lv.getAdapter()==null){
            lv.setAdapter(null);

            txtEmptyBill.setVisibility(View.VISIBLE);
        }
        getBill();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                txtBillTitle = (TextView) view.findViewById(R.id.txtBill);
                String value = txtBillTitle.getText().toString();

                Intent intent = new Intent(GroupInfoActivity.this, ExpensesActivity.class);
                intent.putExtra("expensesName", value);
                intent.putExtra("billposition", Integer.toString(position));
                intent.putExtra("billid", bills.get(position - 1).getId());


                startActivityForResult(intent, 3);



            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_groupinfo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        String gID = intent.getStringExtra("GTITLE");
        //When Product action item is clicked
        if (id == R.id.action_addbill) {
            //Create Intent for Product Activity



            Intent i = new Intent(getApplicationContext(), AddBillActivity.class);
            i.putExtra("G_ID", gID);
            startActivityForResult(i, 1);

            return true;
        }

        else if (id == R.id.action_groupinfo) {
            //Create Intent for Product Activity

            Intent i = new Intent(getApplicationContext(), GroupProfile.class);
            i.putExtra("G_ID", gID);
            startActivityForResult(i, 2);

            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                final ListView lv = (ListView) findViewById(R.id.list_bill);

                String stredittext = data.getStringExtra("billTitle");
                String strBillDate = data.getStringExtra("billDate");
                String strid = data.getStringExtra("billid");

                bills.add(new Bill(stredittext, strBillDate));
                bills.get(bills.size() - 1).setId(strid);
                mAdapter = new CustomBillListAdapter(this, bills);
                mAdapter.notifyDataSetChanged();
                lv.setAdapter(mAdapter);

                Toast.makeText(getApplicationContext(), "Successfully create bill"+strid, Toast.LENGTH_LONG).show();


                if( lv.getAdapter()!=null){


                    txtEmptyBill.setVisibility(View.GONE);
                }
            }
        }
        else   if (requestCode == 3) {
            if (resultCode == RESULT_OK) {

                final ListView lv = (ListView) findViewById(R.id.list_bill);


                mAdapter = new CustomBillListAdapter(this, bills);
                mAdapter.notifyDataSetChanged();
                lv.setAdapter(mAdapter);


                Toast.makeText(getApplicationContext(), "Successfully create bill", Toast.LENGTH_LONG).show();


                if( lv.getAdapter()!=null){


                    txtEmptyBill.setVisibility(View.GONE);
                }
            }
        }

    }

    private void getBill(){
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


                showBill(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();
                // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                // final String memberid = sharedPreferences.getString(config.MemberID, "") ;
                String groupTitle = intent.getStringExtra("GTITLE");
                String s = rh.sendGetRequestParam(config.url_viewBill, groupTitle);
                return s;
            }
        }
        getBill ge = new getBill();
        ge.execute();
    }

    private void showBill(String json){
        JSONObject jsonObject = null;



        try {
            jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String name = jo.getString("name");
                String date = jo.getString("date");
                String id =jo.getString("bid");
                /*HashMap<String,String> employees = new HashMap<>();
                employees.put(config.KEY_GROUP_NAME,name);
                employees.put(config.KEY_GROUP_ID,type);
*/
                // Toast.makeText(getApplicationContext(),""+result.length(),Toast.LENGTH_LONG);
                bills.add(new Bill(name, date));

                bills.get(i).setId(id);

                lv.setVisibility(View.VISIBLE);
                txtEmptyBill.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        mAdapter = new CustomBillListAdapter(this, bills);
        mAdapter.notifyDataSetChanged();
        lv.setAdapter(mAdapter);

    }
}
