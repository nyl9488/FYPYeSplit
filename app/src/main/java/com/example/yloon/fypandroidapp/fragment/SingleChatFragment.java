package com.example.yloon.fypandroidapp.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.adapter.CustomChatListAdapter;
import com.example.yloon.fypandroidapp.view.SingleChat_activity;

public class SingleChatFragment extends Fragment {

    private ListView lv;
    private TextView txtChatName;
    private TextView txtChatEmpty;

    public SingleChatFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_onetoone, container, false);


        lv = (ListView) view.findViewById(R.id.list_chatlist);


        if(viewPager.strArr_Chat.isEmpty()){
            txtChatEmpty=(TextView)view.findViewById(R.id.txtChatEmpty);

            txtChatEmpty.setVisibility(View.VISIBLE);
        }
        else
        {
            lv.setAdapter(new CustomChatListAdapter(getActivity(), viewPager.strArr_Chat));
        }


        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                txtChatName=(TextView)view.findViewById(R.id.txtChatName);
                String value=txtChatName.getText().toString();
                Intent intent = new Intent(getActivity(), SingleChat_activity.class);
                intent.putExtra("chatname", value+"@kristng");
                startActivity(intent);

            }
        });
        return view;
    }




}
