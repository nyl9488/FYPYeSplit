package com.example.yloon.fypandroidapp.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
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

import java.util.HashMap;

public class ExpensesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expenses);
        getBill();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_expenses, menu);
        return true;
        //    ((viewPager) getActivity()).setActionBarTitle("Chat");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //When Product action item is clicked
       if (id == R.id.action_deletebill) {
            //Create Intent for Product Activity
            new AlertDialog.Builder(ExpensesActivity.this)
                    .setTitle("Delete bill")
                    .setMessage("Are you sure you want to delete this bill?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            deletebill();

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

       else if (id == android.R.id.home) {
           //Create Intent for Product Activity
       finish();
           return true;

       }

       else if (id == R.id.action_editbill) {
           //Create Intent for Product Activity
           Intent productIntent = new Intent(getApplicationContext(),EditBill.class);
           //Start Product Activity
           startActivity(productIntent);
           return true;
       }

        return super.onOptionsItemSelected(item);
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
                Intent intent = getIntent();
                String billid = intent.getStringExtra("billid");
                String s = rh.sendGetRequestParam(config.url_viewBillDetails, billid);
                return s;
            }
        }
        getBill ge = new getBill();
        ge.execute();
    }

    private void showBill(String json){
        JSONObject jsonObject = null;

TextView txt_BillDate= (TextView)findViewById(R.id.txt_BillDate);
        TextView txt_BillName= (TextView)findViewById(R.id.txt_BillName);
        TextView txt_BillAmount= (TextView)findViewById(R.id.txt_BillAmount);

        try {
            jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String name = jo.getString("name");
                String date = jo.getString("date");
                String amount =jo.getString("amount");
                /*HashMap<String,String> employees = new HashMap<>();
                employees.put(config.KEY_GROUP_NAME,name);
                employees.put(config.KEY_GROUP_ID,type);
*/
              //  Toast.makeText(getApplicationContext(), "" + result.length(), Toast.LENGTH_LONG);
                txt_BillDate.setText(date);
                txt_BillName.setText(name);
                txt_BillAmount.setText(amount);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    //Adding an employee
    private void deletebill(){

        Intent intent = getIntent();
        final String billid = intent.getStringExtra("billid");


        class AddEmployee extends AsyncTask<Void,Void,String>{

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(ExpensesActivity.this,"Deletting...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Intent intent = getIntent();
                String billposition = intent.getStringExtra("billposition");
                GroupInfoActivity.bills.remove(Integer.parseInt(billposition)-1);

                Intent i = new Intent();
             //   intent.putExtra("edittextvalue","value_here");
                setResult(RESULT_OK, i);
                finish();

               // Toast.makeText(MainActivity.this, s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();
                params.put("id",billid);
            //    params.put(Config.KEY_EMP_DESG,desg);
            //    params.put(Config.KEY_EMP_SAL,sal);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(config.url_deletebill, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }


}
