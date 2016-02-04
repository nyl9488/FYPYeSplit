package com.example.yloon.fypandroidapp.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.fragment.viewPager;
import com.example.yloon.fypandroidapp.server.config;
import com.example.yloon.fypandroidapp.service.XxmpConnection;

import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.packet.Presence;

public class LoginActivity extends Activity implements OnClickListener {
   // Executor executor = Executors.newSingleThreadExecutor();
    private EditText useridText, pwdText;
    private LinearLayout layout1, layout2;
    private Button btnLoginnow;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //���ڵ�¼
        this.layout1 = (LinearLayout) findViewById(R.id.formlogin_layout1);
        //��¼����
      this.layout2 = (LinearLayout) findViewById(R.id.formlogin_layout2);
        useridText = (EditText) findViewById(R.id.editLogTel);
        pwdText = (EditText) findViewById(R.id.editLogPass);

        SmackAndroid smack = SmackAndroid.init(this);
        LoadPreferences();

        btnLoginnow = (Button) findViewById(R.id.btnLogin);
        btnLoginnow.setOnClickListener(this);
        smack.onDestroy();
    }

   private void LoadPreferences(){

       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       final String savedemail = sharedPreferences.getString(config.Email, "") ;
       final String  savedpass = sharedPreferences.getString(config.Pass, "") ;


     if(savedemail!=null){
         useridText.setText(savedemail);
     }
       if(savedpass!=null){
           pwdText.setText(savedpass);
       }

       Thread thread = new Thread()
       {
           @Override
           public void run() {
               handler.sendEmptyMessage(1);
               try {

                   XxmpConnection.getConnection().login(savedemail, savedpass);
                   Log.i("XMPPClient", "Logged in as " + XxmpConnection.getConnection().getUser());
                   // status

                   Presence presence = new Presence(Presence.Type.available);

                   String n = useridText.getText().toString();
                   String e = pwdText.getText().toString();


                   SavePreferences(config.Email, n);
                   SavePreferences(config.Pass,e);

                   XxmpConnection.getConnection().sendPacket(presence);
                   Intent intent = new Intent(getApplication(), viewPager.class);
                   intent.putExtra("USERID", savedemail);
                   startActivity(intent);

                   finishAffinity();
                   handler.sendEmptyMessage(2);
               }  catch (XMPPException e) {
                   XxmpConnection.closeConnection();
                   handler.sendEmptyMessage(3);
               }
           }
       };





       if(savedemail.isEmpty()) {
           Thread.interrupted();
       }
       else if(haveNetworkConnection()){
           thread.start();
       }
       else{
           Thread.interrupted();
           Toast.makeText(getApplicationContext(),"Please connect to internet.",Toast.LENGTH_LONG).show();
       }


   }

    private void SavePreferences(String key, String value){
       SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
       SharedPreferences.Editor editor = sharedPreferences.edit();
       editor.putString(key, value);
       editor.commit();

   }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogin:
                final String USERID = useridText.getText().toString();
                final String PWD = pwdText.getText().toString();


                Thread thread = new Thread()
                {

                    @Override
                    public void run() {
                        handler.sendEmptyMessage(1);
                        try {
                            XxmpConnection.getConnection().login(USERID, PWD);
                            Log.i("XMPPClient", "Logged in as " + XxmpConnection.getConnection().getUser());
                            // status

                            Presence presence = new Presence(Presence.Type.available);

                            String n = useridText.getText().toString();
                            String e = pwdText.getText().toString();


                            SavePreferences(config.Email, n);
                            SavePreferences(config.Pass,e);

                            XxmpConnection.getConnection().sendPacket(presence);
                            Intent intent = new Intent(getApplication(), viewPager.class);
                            intent.putExtra("USERID", USERID);
                            startActivity(intent);

                            finishAffinity();
                            handler.sendEmptyMessage(2);
                        }  catch (XMPPException e) {
                            XxmpConnection.closeConnection();
                            handler.sendEmptyMessage(3);
                        }
                    }
                };

                if(haveNetworkConnection()){
                thread.start();
            }
            else{
                    thread.interrupted();
                Toast.makeText(getApplicationContext(),"Please connect to internet.",Toast.LENGTH_LONG).show();
            }

                break;
            // case R.id.formlogin_btcancel:
            //    finish();
            //   break;

        }
    }

    public Handler handler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            switch(msg.what){
                case 1:
                      layout1.setVisibility(View.VISIBLE);
                    layout2.setVisibility(View.GONE);


                    break;
                case 2:

                    Toast.makeText(getApplicationContext(), "Login success!!! =)", Toast.LENGTH_LONG).show();

                    break;
                default:
                    layout1.setVisibility(View.GONE);
                    layout2.setVisibility(View.VISIBLE);
                    Toast.makeText(getApplicationContext(), "Invalid Email or password !!! =)",
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };



    public void onForgetClick(View v) {
        startActivity(new Intent(this, ForgetPassword_Activity.class));

    }
    private boolean haveNetworkConnection() {
        boolean haveConnectedWifi = false;
        boolean haveConnectedMobile = false;

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] netInfo = cm.getAllNetworkInfo();
        for (NetworkInfo ni : netInfo) {
            if (ni.getTypeName().equalsIgnoreCase("WIFI"))
                if (ni.isConnected())
                    haveConnectedWifi = true;
            if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
                if (ni.isConnected())
                    haveConnectedMobile = true;
        }
        return haveConnectedWifi || haveConnectedMobile;
    }

}