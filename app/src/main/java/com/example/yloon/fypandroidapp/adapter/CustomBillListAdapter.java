package com.example.yloon.fypandroidapp.adapter;

/**
 * Created by YLoon on 5/12/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.model.Bill;

import java.util.ArrayList;

/**
 * Created by YLoon on 5/12/2015.
 */

public class CustomBillListAdapter extends ArrayAdapter<Bill> {


    public CustomBillListAdapter(Context context, ArrayList<Bill> bills) {
        super(context, 0, bills);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Bill bill = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_bill, parent, false);
        }
        // Lookup view for data population
        TextView tvName = (TextView) convertView.findViewById(R.id.txtBill);
        TextView tvHome = (TextView) convertView.findViewById(R.id.txtBillDate);
        // Populate the data into the template view using the data object
        tvName.setText(bill.title);
        tvHome.setText(bill.date);
        // Return the completed view to render on screen
        return convertView;
    }
}