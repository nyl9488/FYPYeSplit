package com.example.yloon.fypandroidapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.yloon.fypandroidapp.R;

/**
 * Created by YLoon on 15/12/2015.
 */
public class View_Holder_Notice extends RecyclerView.ViewHolder {


    TextView title;
    TextView type;
    //ImageView imageView;

    View_Holder_Notice(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.titleNotice);
        type = (TextView) itemView.findViewById(R.id.typenotice);

        // imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }

}