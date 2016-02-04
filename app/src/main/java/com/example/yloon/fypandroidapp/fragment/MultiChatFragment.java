package com.example.yloon.fypandroidapp.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.service.XxmpConnection;
import com.example.yloon.fypandroidapp.view.MultiChatRoom;

import org.jivesoftware.smack.SmackAndroid;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.muc.RoomInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class MultiChatFragment extends Fragment {

    public final static String ASK_QUESTION = "com.example.yloon.fypandroidapp.MESSAGE.ASK";
    public final static String ANSWER_QUESTION = "com.example.yloon.fypandroidapp.MESSAGE.ANSWER";
    public final static String QUESTION = "com.example.yloon.fypandroidapp.MESSAGE.QUESTION";
    private EditText rnameEditText;
    private ListView questionsListView;
    private HashMap<String, String> chatRoomMap = new HashMap<String, String>();
    public static MultiUserChat muc;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SmackAndroid.init(getActivity());
        View view = inflater.inflate(R.layout.fragment_multi, container, false);
       // rnameEditText = (EditText)view.findViewById(R.id.question_input);
        questionsListView = (ListView)view.findViewById(R.id.questions);
        try{

            Collection<HostedRoom> rooms = MultiUserChat.getHostedRooms(XxmpConnection.getConnection(), "conference." + XxmpConnection.getConnection().getServiceName());
            List<String> questions = new ArrayList<String>();
            if(rooms != null && !rooms.isEmpty())
            {
                for(HostedRoom entry : rooms){
                    RoomInfo info = MultiUserChat.getRoomInfo(XxmpConnection.getConnection(), entry.getJid());
                    questions.add(info.getDescription());
                    chatRoomMap.put(info.getDescription(), entry.getJid());//store them into the hashmap
                }
            }

            //show them into the users
            questionsListView.setAdapter(new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,questions));
            //add the listener to every question!
            questionsListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                        long arg3) {
                    String s = questionsListView.getItemAtPosition(arg2).toString();
                    Toast.makeText(getContext(), "你选择了第" + arg2 + "个Item，itemTitle的值是：" + s, Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MultiChatRoom.class);
                    intent.putExtra(ANSWER_QUESTION,chatRoomMap.get(s));
                    intent.putExtra(QUESTION, s);
                    startActivity(intent);
                }

            });
        } catch (XMPPException e1) {
            e1.printStackTrace();
        }

        view.findViewById(R.id.FAB_addmulti).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // final ListView lv = (ListView) v.findViewById(R.id.questions);
                final Dialog dialog = new Dialog(getActivity());
                dialog.setContentView(R.layout.dialog_chatselection);
                dialog.setTitle("Add a Multi Chat");

                final EditText title = (EditText) dialog.findViewById(R.id.edmultiName);


                Button createmultibtn = (Button) dialog.findViewById(R.id.createmultibtn);

                // Set OnClick Listener on SignUp button

                createmultibtn.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        String message = title.getText().toString();
                        if (message.equals("")) {
                            Toast.makeText(getActivity(), "Your question shouldn't be NULL!!", Toast.LENGTH_SHORT).show();
                        } else {


                            Intent intent = new Intent(getActivity(), MultiChatRoom.class);
                            if (chatRoomMap.containsKey(message)) {
                                Toast.makeText(getActivity(), "Your question had been asked by other person," +
                                        "you can communicate with him", Toast.LENGTH_LONG).show();
                                intent.putExtra(ANSWER_QUESTION, chatRoomMap.get(message));
                                intent.putExtra(QUESTION, message);
                            } else {
                                intent.putExtra(ASK_QUESTION, message);
                            }
                            dialog.dismiss();
                            startActivity(intent);
                        }
                    }
                });


                dialog.show();
            }
        });
        return view;

    }
}
