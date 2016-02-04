package com.example.yloon.fypandroidapp.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceGroup;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yloon.fypandroidapp.ChangePassword;
import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.server.RequestHandler;
import com.example.yloon.fypandroidapp.server.config;
import com.example.yloon.fypandroidapp.service.XxmpConnection;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;


public class SettingsActivity extends PreferenceActivity implements
        SharedPreferences.OnSharedPreferenceChangeListener {

    ImageView profile_image;

    private Bitmap bitmap;

    private Uri filePath;
    private int PICK_IMAGE_REQUEST = 1;

    public static final String GET_IMAGE_URL="http://192.168.0.106:800/fyp_connect/getprofilepic.php";

    /** Called when the activity is first created. */
             @Override
     public void onCreate(Bundle savedInstanceState) {
                 super.onCreate(savedInstanceState);


              //   Toast.makeText(getApplicationContext(), savedid, Toast.LENGTH_LONG).show();

                 addPreferencesFromResource(R.xml.preference_setting);
                // PreferenceManager.setDefaultValues(SettingsActivity.this, R.xml.preference_setting,
                //         false);
                 initSummary(getPreferenceScreen());

                 Preference set_image = findPreference("set_image");
                 set_image.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                     public boolean onPreferenceClick(Preference preference) {
                         profile_image = (ImageView) findViewById(R.id.profile_image);
                         showFileChooser();
                         return false;
                     }
                 });

                 Preference fooBarPref = findPreference("foo_bar_pref");
                 fooBarPref.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                     public boolean onPreferenceClick(Preference preference) {

                         startActivity(new Intent(SettingsActivity.this, ChangePassword.class));
                         return false;
                     }
                 });

                 Preference set_logout = findPreference("set_logout");
                 set_logout.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                     public boolean onPreferenceClick(Preference preference) {

                         new AlertDialog.Builder(SettingsActivity.this)
                                 .setTitle("Log out")
                                 .setMessage("Are you sure you want to log out?")
                                 .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                     public void onClick(DialogInterface dialog, int which) {
                                         XxmpConnection.closeConnection();


                                         SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(SettingsActivity.this);
                                         sharedPreferences.edit().clear().commit();

                                         Intent productIntent = new Intent(SettingsActivity.this, MainScreen.class);
                                         //Start Product Activity
                                         startActivity(productIntent);
                                         finishAffinity();
                                     }
                                 })
                                 .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                     public void onClick(DialogInterface dialog, int which) {
                                         // do nothing
                                     }
                                 })
                                 .setIcon(android.R.drawable.ic_dialog_alert)
                                 .show();
                         return false;
                     }
                 });
             }

    private void initSummary(Preference p) {
        if (p instanceof PreferenceGroup) {
            PreferenceGroup pGrp = (PreferenceGroup) p;
            for (int i = 0; i < pGrp.getPreferenceCount(); i++) {
                initSummary(pGrp.getPreference(i));
            }
        } else {
            updatePrefSummary(p);
        }
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onResume() {
        super.onResume();
        // Set up a listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(SettingsActivity.this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the listener whenever a key changes
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(SettingsActivity.this);
    }

    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        updatePrefSummary(findPreference(key));
    }

    private void updatePrefSummary(Preference p)
    {

        if (p instanceof EditTextPreference) {
            EditTextPreference editTextPref = (EditTextPreference) p;
            if (p.getTitle().toString().contains("my_individual_name"))
            {
                p.setSummary("******");
            } else {
                p.setSummary(editTextPref.getText());
            }
        }
       /* if (p instanceof ListPreference) {
            ListPreference listPref = (ListPreference) p;
            p.setSummary(listPref.getEntry());
        }*/

    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Toolbar bar;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
            bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
            bar.setNavigationIcon(getResources().getDrawable( R.drawable.ic_back ));

            root.addView(bar, 0); // insert at top
        } else {
            ViewGroup root = (ViewGroup) findViewById(android.R.id.content);
            ListView content = (ListView) root.getChildAt(0);

            root.removeAllViews();

            bar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);


            int height;
            TypedValue tv = new TypedValue();
            if (getTheme().resolveAttribute(R.attr.actionBarSize, tv, true)) {
                height = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
            }else{
                height = bar.getHeight();
            }

            content.setPadding(0, height, 0, 0);

            root.addView(content);
            root.addView(bar);
        }

        bar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Random rnd = new Random();
        bar.setTitleTextColor(getResources().getColor(R.color.White));

        // Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_image.setImageBitmap(bitmap);
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImage(){
        class UploadImage extends AsyncTask<Bitmap,Void,String>{

            ProgressDialog loading;
            RequestHandler rh = new RequestHandler();
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String savedid = sharedPreferences.getString(config.MemberID, "") ;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(SettingsActivity.this, "Uploading...", null,true,true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            }

            @Override
            protected String doInBackground(Bitmap... params) {
                Bitmap bitmap = params[0];
                String uploadImage = getStringImage(bitmap);

                HashMap<String,String> data = new HashMap<>();

                data.put("image", uploadImage);
               data.put("id", savedid);
                String result = rh.sendPostRequest(config.UPLOAD_URL,data);

                return result;
            }
        }

        UploadImage ui = new UploadImage();
        ui.execute(bitmap);
    }


    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

}