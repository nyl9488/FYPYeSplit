package com.example.yloon.fypandroidapp.view;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.adapter.CustomParticipantListAdapter;
import com.example.yloon.fypandroidapp.adapter.CustomPayerListAdapter;
import com.example.yloon.fypandroidapp.adapter.CustomRoasterListAdapter;
import com.example.yloon.fypandroidapp.model.Participant;
import com.example.yloon.fypandroidapp.model.Payer;
import com.example.yloon.fypandroidapp.server.RequestHandler;
import com.example.yloon.fypandroidapp.server.config;
import com.example.yloon.fypandroidapp.service.XxmpConnection;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;




/**
 * The <code>TabsViewPagerFragmentActivity</code> class implements the Fragment activity that maintains a TabHost using a ViewPager.
 * @author mwho
 */
public class AddBillActivity extends AppCompatActivity {
    private EditText billTitle;
    private EditText billAmount;
    private TextView billDate;
    private static final String TAG = AddBillActivity.class.getSimpleName();

    // private static final int INTENT_REQUEST_GET_IMAGES = 13;
    private static final int INTENT_REQUEST_GET_N_IMAGES = 14;

    //  private Context mContext;

    TextView DateText;
    private ViewGroup mSelectedImagesContainer;
    HashSet<Uri> mMedia = new HashSet<Uri>();
    private ArrayList<String> strArr ;
    public ArrayList<Participant> strArr_participant=  new ArrayList<Participant>();
    public ArrayList<Payer> strArr_payer =  new ArrayList<Payer>();
    private EditText txtTotal;
    public static ListView lvPay;
    public static ListView lvParticipant;
    public CustomPayerListAdapter payerListAdapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Inflate the layout
        setContentView(R.layout.activity_addbill);
        billDate= (TextView)findViewById(R.id.txtAddBillDate);
        billTitle= (EditText)findViewById(R.id.edAddBillTitle);
        billAmount= (EditText)findViewById(R.id.edAddTotalAmount);



     //   Toast.makeText(getApplicationContext(),""+groupTitle,Toast.LENGTH_LONG).show()
     // Initialise the TabHost
        lvPay = (ListView) findViewById(R.id.all_payerList);
        lvParticipant = (ListView) findViewById(R.id.all_participantList);

        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());

        DateText = (TextView)findViewById(R.id.txtAddBillDate);
        DateText.setText(formattedDate);


        // Intialise ViewPager

      //  mSelectedImagesContainer = (ViewGroup) findViewById(R.id.selected_photos_container);



        txtTotal = (EditText) findViewById(R.id.edAddTotalAmount);

        txtTotal.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                // you can call or do what you want with your EditText here
                View v;

                EditText et;
                for (int i = 0; i < lvPay.getCount(); i++) {
                    v = lvPay.getChildAt(i);
                    et = (EditText) v.findViewById(R.id.edPpAmount);


                    double w;

                    try {
                        w = new Double(Double.parseDouble(txtTotal.getText().toString()) / lvPay.getCount());
                        et.setText(String.format("%.2f", w));
                    } catch (NumberFormatException e) {
                        w = 0; // your default value
                        et.setText(String.format("%.2f", w));
                    }


                }

                View v2;
                lvParticipant = (ListView) findViewById(R.id.all_participantList);
                EditText et2;
                for (int i = 0; i < lvParticipant.getCount(); i++) {
                    v2 = lvParticipant.getChildAt(i);
                    et2 = (EditText) v2.findViewById(R.id.edPpAmount);

                    double w;

                    try {
                        w = new Double(Double.parseDouble(txtTotal.getText().toString()) / lvParticipant.getCount());
                        et2.setText(String.format("%.2f", w));
                    } catch (NumberFormatException e) {
                        w = 0; // your default value
                        et2.setText(String.format("%.2f", w));
                    }


                }

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {


            }
        });






    }


    public void addBillOnClick(View V) {
        addBill();



    }


    public void AddPayerOnClick(View V) {

        /// Create Intent for SignUpActivity  and Start The Activity

        final Dialog dialog = new Dialog(this);


        dialog.setContentView(R.layout.dialog_pp);
        dialog.setTitle("Add a Payer");

        ListView lv = (ListView) dialog.findViewById(R.id.pp_list);

        Roster roster = XxmpConnection.getConnection().getRoster();

        Collection<RosterEntry> entries = roster.getEntries();
        strArr = new ArrayList<String>();
        strArr.add("ME");
        for (RosterEntry entry : entries) {

            strArr.add(entry.getUser());


        }


        lv.setAdapter(new CustomRoasterListAdapter(this, strArr));

        dialog.show();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView text = (TextView) view.findViewById(R.id.buddyText);

                String selectedFromList = text.getText().toString().trim();


                strArr_payer.add(new Payer(selectedFromList, "0"));


                payerListAdapter = new CustomPayerListAdapter(AddBillActivity.this, strArr_payer);
                lvPay.setAdapter(payerListAdapter);
                setListViewHeightBasedOnItems(lvPay);


                for (int i = 0; i < strArr_payer.size(); i++) {
                    double w2;

                    try {
                        w2 = new Double(Double.parseDouble(txtTotal.getText().toString()) / lvPay.getCount());
                        strArr_payer.get(i).setAmount(String.format("%.2f", w2));
                    } catch (NumberFormatException e) {
                        w2 = 0; // your default value
                        strArr_payer.get(i).setAmount(String.format("%.2f", w2));
                    }
                }


                dialog.dismiss();

            }


        });




    }

    public void removePPOnClick(View v){
        setListViewHeightBasedOnItems(lvPay);
    }


    public void AddParticipantOnClick(View V) {

        /// Create Intent for SignUpActivity  and Start The Activity

        final Dialog dialog = new Dialog(this);


        dialog.setContentView(R.layout.dialog_pp);
        dialog.setTitle("Add a Participant");

        ListView lv = (ListView) dialog.findViewById(R.id.pp_list);
        strArr = new ArrayList<String>();

        Roster roster = XxmpConnection.getConnection().getRoster();

        Collection<RosterEntry> entries = roster.getEntries();
        strArr.add("ME");
        for (RosterEntry entry : entries) {

            strArr.add(entry.getUser());

        }

        lv.setAdapter(new CustomRoasterListAdapter(this, strArr));


        dialog.show();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                TextView text = (TextView) view.findViewById(R.id.buddyText);

                String selectedFromList = text.getText().toString().trim();


                strArr_participant.add(new Participant(selectedFromList, "0"));


                lvParticipant.setAdapter(new CustomParticipantListAdapter(AddBillActivity.this, strArr_participant));
                setListViewHeightBasedOnItems(lvParticipant);

                for (int i = 0; i < strArr_participant.size(); i++) {
                    double w2;

                    try {
                        w2 = new Double(Double.parseDouble(txtTotal.getText().toString()) / lvParticipant.getCount());
                        strArr_participant.get(i).setAmount(String.format("%.2f", w2));
                    } catch (NumberFormatException e) {
                        w2 = 0; // your default value
                        strArr_participant.get(i).setAmount(String.format("%.2f", w2));
                    }
                }


                dialog.dismiss();

            }
        });




    }

    public static boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                item.measure(0, 0);
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight;
            listView.setLayoutParams(params);
            listView.requestLayout();

            return true;

        } else {
            return false;
        }

    }




    private void addBill(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        final String  savedmemberID = sharedPreferences.getString(config.MemberID, "") ;

        //  final String groupName =topicName.getText().toString();

        //  final String groupType =spinnerDropDown.getSelectedItem().toString();


        class addBill extends AsyncTask<Void,Void,String> {
            String billTit= billTitle.getText().toString();
            String billDated= billDate.getText().toString();
            String billAmoun= billAmount.getText().toString();

             ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                 loading = ProgressDialog.show(AddBillActivity.this, "Adding...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                showBill(s);
                  loading.dismiss();
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();

                Intent intent= getIntent();
                String groupId = intent.getStringExtra("G_ID");


                params.put(config.KEY_GROUP_ID,groupId);
                params.put(config.KEY_BILL_NAME,billTit);
                params.put(config.KEY_BILL_AMOUNT,billAmoun);
                params.put(config.KEY_BILL_DATE,billDated);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(config.url_addbill, params);
                return res;
            }
        }

        addBill ae = new addBill();
        ae.execute();
    }

    private void showBill(String json){


        String billTit= billTitle.getText().toString();
        String billDated= billDate.getText().toString();



        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);

            String id = c.getString("id");
            // Toast.makeText(getApplicationContext(), "Successfully create bill"+id, Toast.LENGTH_LONG).show();
            Intent intent = new Intent();
            intent.putExtra("billid",id);
            intent.putExtra("billTitle", billTit);
            intent.putExtra("billDate", billDated);

            setResult(RESULT_OK, intent);
            finish();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}