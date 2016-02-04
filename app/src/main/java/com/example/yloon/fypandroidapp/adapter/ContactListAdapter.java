package com.example.yloon.fypandroidapp.adapter;

/**
 * Created by YLoon on 24/9/2015.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.model.PhoneContact;

import java.util.List;

public class ContactListAdapter extends BaseAdapter{

    private List<PhoneContact> lists;
    private Context context;


    public ContactListAdapter(List<PhoneContact> lists, Context context) {
        this.lists = lists;
        this.context = context;

    }

    @Override
    public int getCount() {
        return lists.size();
    }

    @Override
    public Object getItem(int position) {
        return lists.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem_contact, null);
            holder = new ViewHolder();
            holder.nametv = (TextView) convertView.findViewById(R.id.name);
            holder.numbertv = (TextView) convertView.findViewById(R.id.number);
            holder.nametv.setText(lists.get(position).getName());
            holder.numbertv.setText(lists.get(position).getNumber());
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
            holder.nametv.setText(lists.get(position).getName());
            holder.numbertv.setText(lists.get(position).getNumber());
        }
        return convertView;
    }
    private static class ViewHolder{
        TextView nametv;
        TextView numbertv;
    }

}
