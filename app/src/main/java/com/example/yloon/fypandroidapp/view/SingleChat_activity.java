package com.example.yloon.fypandroidapp.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.service.XxmpConnection;

import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.filter.MessageTypeFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.util.StringUtils;

import java.util.ArrayList;

public class SingleChat_activity extends AppCompatActivity {
    private Handler mHandler = new Handler();
    private EditText mRecipient;
    private EditText mSendText;
    private ListView mList;
    private XxmpConnection connection;
    private ArrayList<String> messages = new ArrayList();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activity);


        mRecipient = (EditText) findViewById(R.id.recipient);
        Log.i("XMPPClient", "mRecipient = " + mRecipient);
        mSendText = (EditText)findViewById(R.id.sendText);
        Log.i("XMPPClient", "mSendText = " + mSendText);
        mList = (ListView) findViewById(R.id.listMessages);
        Log.i("XMPPClient", "mList = " + mList);
        setListAdapter();
        Intent intent = getIntent();
        String recipient_name = intent.getStringExtra("chatname");
        mRecipient.setText(recipient_name);
        // Set a listener to send a chat text message
        Button send = (Button)findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                String to = mRecipient.getText().toString();
                String text = mSendText.getText().toString();

                Log.i("XMPPClient", "Sending text [" + text + "] to [" + to + "]");
                Message msg = new Message(to, Message.Type.chat);
                msg.setBody(text);
                XxmpConnection.getConnection().sendPacket(msg);
                messages.add(XxmpConnection.getConnection().getUser() + ":");
                messages.add(text);
                setListAdapter();
            }
        });

        //receive message
        PacketFilter filter = new MessageTypeFilter(Message.Type.chat);
        XxmpConnection.getConnection().addPacketListener(new PacketListener() {
            public void processPacket(Packet packet) {
                Message message = (Message) packet;
                if (message.getBody() != null) {
                    String fromName = StringUtils.parseBareAddress(message.getFrom());
                    Log.i("XMPPClient", "Got text [" + message.getBody() + "] from [" + fromName + "]");
                    messages.add(fromName + ":");
                    messages.add(message.getBody());
                    // Add the incoming message to the list view
                    mHandler.post(new Runnable() {
                        public void run() {
                            setListAdapter();
                        }
                    });
                }
            }
        }, filter);
    }


    private void setListAdapter
            () {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getApplicationContext(),
                R.layout.listitem_chatmessage,
                messages);
        mList.setAdapter(adapter);
    }
}
