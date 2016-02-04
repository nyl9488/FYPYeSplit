package com.example.yloon.fypandroidapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.model.SBuddy;

import java.util.ArrayList;

/**
 * Created by YLoon on 9/12/2015.
 */
public class CustomSelectMberListAdapter extends ArrayAdapter<SBuddy> {

    private ArrayList<SBuddy> groupList;

    public CustomSelectMberListAdapter(Context context, int textViewResourceId,
                                       ArrayList<SBuddy> contactList) {
        super(context, textViewResourceId, contactList);
        this.groupList = new ArrayList<SBuddy>();
        this.groupList.addAll(contactList);
    }

    private class ViewHolder {
        //   TextView code;
        TextView name;
        CheckBox selected;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Log.v("ConvertView", String.valueOf(position));

        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater)getContext().getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(R.layout.listitem_addmember, null);

            holder = new ViewHolder();
            // holder.code = (TextView) convertView.findViewById(R.id.code);
            holder.name = (TextView) convertView.findViewById(R.id.select_contact_text);
            holder.selected = (CheckBox) convertView.findViewById(R.id.cb_selectMember);
            convertView.setTag(holder);

            holder.selected.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                    CheckBox cb = (CheckBox) v;
                    SBuddy buddy = (SBuddy) cb.getTag();
                    buddy.setSelected(cb.isChecked());

                }
            });


        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        SBuddy group = groupList.get(position);
        //    holder.code.setText(" (" +  group.getCode() + ")");
        holder.name.setText(group.getName());
        holder.selected.setChecked(group.isSelected());
        holder.selected.setTag(group);


        return convertView;

    }

}