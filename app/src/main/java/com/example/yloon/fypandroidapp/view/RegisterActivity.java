package com.example.yloon.fypandroidapp.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.server.RequestHandler;
import com.example.yloon.fypandroidapp.server.config;
import com.example.yloon.fypandroidapp.service.XxmpConnection;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;

import java.util.HashMap;

public class RegisterActivity extends Activity implements OnClickListener {

    private EditText email;
    private EditText password;
    private EditText username;
    private EditText retype_pass;
    private Button regester;
    RequestHandler requestHandler = new RequestHandler();

    private XMPPConnection connection;

    Handler handler = new Handler(Looper.getMainLooper()) {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    Toast.makeText(getApplicationContext(), "Connection fail!!! =)",
                            Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    Toast.makeText(getApplicationContext(), "Success!!! =)",
                            Toast.LENGTH_LONG).show();
                    break;
                case 2:
                    Toast.makeText(getApplicationContext(), "Already have account!!! =)",
                            Toast.LENGTH_LONG).show();
                    break;
                case 3:
                    Toast.makeText(getApplicationContext(), "Register fail!!! =)",
                            Toast.LENGTH_LONG).show();
                    break;

                case 4:
                    Toast.makeText(getApplicationContext(), "Password must match!!! =)",
                            Toast.LENGTH_LONG).show();
                    break;
                case 5:
                    Toast.makeText(getApplicationContext(), "Password must more than 6 characters!!! =)",
                            Toast.LENGTH_LONG).show();
                    break;
                case 6:
                    Toast.makeText(getApplicationContext(), "Password cannot be empty!!! =)",
                            Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        email = (EditText) findViewById(R.id.edRegEmail);
        username = (EditText) findViewById(R.id.edRegUsername);
        password = (EditText) findViewById(R.id.edRegPass);
        retype_pass = (EditText) findViewById(R.id.edRegRetype);
        // nickname = (EditText) findViewById(R.id.reg_nickname);
        regester = (Button) findViewById(R.id.bt_regester);
        regester.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        final String usernameStr = username.getText().toString().trim();
        final String emailStr = email.getText().toString().trim();

        final String passwordStr = password.getText().toString().trim();
        final String retypeStr = retype_pass.getText().toString().trim();
        new Thread(new Runnable() {
            @Override
            public void run() {
                connection = XxmpConnection.getConnection();
                regester(usernameStr,emailStr,passwordStr, retypeStr);
            }
        }).start();

    }

    private void regester(String usernameStr, String emailStr,String passwordStr, String retypeStr) {
        if (connection == null) {
            handler.sendEmptyMessage(0);
        }

        Log.d("kkk", connection.getServiceName());

        if (retypeStr.equals(passwordStr) && passwordStr.length() >= 6 && !passwordStr.isEmpty()) {

                Registration registration = new Registration();
                registration.setType(IQ.Type.SET);
                registration.setTo(connection.getServiceName());
                registration.setUsername(usernameStr);

                  registration.setPassword(passwordStr);
                registration.addAttribute("android", "geolo_createUser_android");
                registration.addAttribute("email", emailStr);

                PacketFilter filter = new AndFilter(new PacketIDFilter(
                        registration.getPacketID()), new PacketTypeFilter(IQ.class));
                PacketCollector collector = connection.createPacketCollector(filter);
                connection.sendPacket(registration);

                IQ resultIQ = (IQ) collector.nextResult(SmackConfiguration
                        .getPacketReplyTimeout());
                collector.cancel();// 鍋滄璇锋眰results锛堟槸鍚︽垚鍔熺殑缁撴灉锛?




                if (resultIQ == null) {
                    Log.e("RegistActivity", "No response from server.");
                    handler.sendEmptyMessage(0);
                } else if (resultIQ.getType() == IQ.Type.RESULT) {
                    addEmployee();
                    // check log cat fro response
                   // Log.d("Create Response", json.toString());




                    finish();
                }


                else {
                    if (resultIQ.getError().toString()
                            .equalsIgnoreCase("conflict(409)")) {
                        Log.e("RegistActivity", "IQ.Type.ERROR: "
                                + resultIQ.getError().toString());
                        handler.sendEmptyMessage(2);
                    }

                    else {
                        Log.e("RegistActivity", "IQ.Type.ERROR: "
                                + resultIQ.getError().toString());
                        handler.sendEmptyMessage(3);
                    }
                }


        }

        else if (!retypeStr.equals(passwordStr))
            handler.sendEmptyMessage(4);
        else if (passwordStr.length()<6&&!passwordStr.isEmpty())
            handler.sendEmptyMessage(5);
        else if (passwordStr.isEmpty())
            handler.sendEmptyMessage(6);



        }



    private void addEmployee(){


        final String sal = username.getText().toString().trim();

        class AddEmployee extends AsyncTask<Void,Void,String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
               // loading = ProgressDialog.show(RegisterActivity.this,"Adding...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
               // loading.dismiss();
                handler.sendEmptyMessage(1);
            }

            @Override
            protected String doInBackground(Void... v) {
                HashMap<String,String> params = new HashMap<>();

                params.put("username",sal);

                RequestHandler rh = new RequestHandler();
                String res = rh.sendPostRequest(config.url_register, params);
                return res;
            }
        }

        AddEmployee ae = new AddEmployee();
        ae.execute();
    }

    }
