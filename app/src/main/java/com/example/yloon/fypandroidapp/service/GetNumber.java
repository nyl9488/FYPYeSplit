package com.example.yloon.fypandroidapp.service;

/**
 * Created by YLoon on 24/9/2015.
 */

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract.CommonDataKinds.Phone;

import com.example.yloon.fypandroidapp.model.PhoneContact;

import java.util.ArrayList;
import java.util.List;

public class GetNumber {

    public static List<PhoneContact> lists = new ArrayList<PhoneContact>();
    public static Cursor cursor = null;
    public static String getNumber(Context context){
        cursor = context.getContentResolver().query(Phone.CONTENT_URI, null, null, null, null);

        String phoneNumber;
        String phoneName;
        while (cursor.moveToNext()) {

            phoneNumber = cursor.getString(cursor.getColumnIndex(Phone.NUMBER));
            phoneName = cursor.getString(cursor.getColumnIndex(Phone.DISPLAY_NAME));

                    PhoneContact phoneInfo = new PhoneContact(phoneName, phoneNumber);

                lists.add(phoneInfo);
      //      System.out.println(""+lists.toString()+cursor.getCount());

        }

        return null;
    }

}
