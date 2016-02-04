package com.example.yloon.fypandroidapp.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.yloon.fypandroidapp.R;

/**
 * Created by YLoon on 15/12/2015.
 */
public class View_Holder_Group extends RecyclerView.ViewHolder {


    TextView title;
    TextView description;
    //ImageView imageView;

    View_Holder_Group(View itemView) {
        super(itemView);

        title = (TextView) itemView.findViewById(R.id.title);
        description = (TextView) itemView.findViewById(R.id.txtgroupType);
       // imageView = (ImageView) itemView.findViewById(R.id.imageView);
    }

}