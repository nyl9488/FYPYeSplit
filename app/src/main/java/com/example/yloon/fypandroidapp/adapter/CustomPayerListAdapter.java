package com.example.yloon.fypandroidapp.adapter;

/**
 * Created by YLoon on 5/12/2015.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.model.Payer;
import com.example.yloon.fypandroidapp.view.AddBillActivity;

import java.util.ArrayList;

/**
 * Created by YLoon on 5/12/2015.
 */

public class CustomPayerListAdapter extends ArrayAdapter<Payer> {
    Context context;
    ArrayList<Payer> result;
    public CustomPayerListAdapter(Context context1, ArrayList<Payer> payers) {
        super(context1, 0, payers);
        result=payers;
        context=context1;


    }


    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        //  ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Payer payer = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.listitem_pp, parent, false);
        }
        // holder.img.setImageResource(imageId[position]);
        TextView tvName = (TextView) convertView.findViewById(R.id.txtPPName);
        EditText tvHome = (EditText) convertView.findViewById(R.id.edPpAmount);
        // Populate the data into the template view using the data object
        tvName.setText(payer.name);
        tvHome.setText(payer.amount);

        Button btn=(Button)convertView.findViewById(R.id.btn_removepp);
        btn.setTag(position);
        btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Integer index = (Integer) v.getTag();
                //items.remove(index.intValue());

                AddBillActivity addBillActivity = new AddBillActivity();

                result.remove(position);
                notifyDataSetChanged();
                AddBillActivity.setListViewHeightBasedOnItems(AddBillActivity.lvPay);
                AddBillActivity.setListViewHeightBasedOnItems(AddBillActivity.lvParticipant);

            }
        });

        return convertView;
    }



}