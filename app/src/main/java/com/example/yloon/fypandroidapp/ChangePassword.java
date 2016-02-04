package com.example.yloon.fypandroidapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yloon.fypandroidapp.service.XxmpConnection;

import org.jivesoftware.smack.XMPPException;

public class ChangePassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Button btn = (Button) findViewById(R.id.btn_changepass);

        btn.setOnClickListener(new View.OnClickListener() {

           EditText edChNewPass =  (EditText) findViewById(R.id.edChNewPass);

            @Override
            public void onClick(View v) {

                String newpass = edChNewPass.getText().toString();
                modifyPassword(newpass);
                Toast.makeText(getApplicationContext(), "Your password has change", Toast.LENGTH_LONG).show();
            }
        });
    }

    public boolean modifyPassword(String password) {
        try {
            XxmpConnection.getConnection().getAccountManager().changePassword(password);
            return true;
        } catch (XMPPException e) {
            e.printStackTrace();
        }
        return false;
    }
}
