package com.example.yloon.fypandroidapp.fragment;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.adapter.CustomChatListAdapter;
import com.example.yloon.fypandroidapp.adapter.FilterRoasterListAdapter;
import com.example.yloon.fypandroidapp.adapter.GroupRecyclerAdapter;
import com.example.yloon.fypandroidapp.adapter.viewPagerFragmentAdapter;
import com.example.yloon.fypandroidapp.model.Group;
import com.example.yloon.fypandroidapp.model.Notice;
import com.example.yloon.fypandroidapp.server.RequestHandler;
import com.example.yloon.fypandroidapp.server.config;
import com.example.yloon.fypandroidapp.service.XxmpConnection;
import com.example.yloon.fypandroidapp.view.Add_group_Activity;
import com.example.yloon.fypandroidapp.view.add_chatRoom_Activity;

import org.jivesoftware.smack.Roster;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.ReportedData;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class viewPager extends AppCompatActivity implements
        OnTabChangeListener, OnPageChangeListener {
    public final static String ASK_QUESTION = "com.example.MultiChat.MESSAGE.ASK";
    public final static String ANSWER_QUESTION = "com.example.MultiChat.MESSAGE.ANSWER";
    public final static String QUESTION = "com.example.MultiChat.MESSAGE.QUESTION";
    private EditText rnameEditText;
    private ListView questionsListView;
    private HashMap<String, String> chatRoomMap = new HashMap<String, String>();
    public static MultiUserChat muc;
    private TabHost tabHost;
    private ViewPager viewPager;
    private viewPagerFragmentAdapter myViewPagerAdapter;
    public static ArrayList<String> strArr_Chat = new ArrayList<String>();
    public static List<Group> data2 ;
    RecyclerView mRecyclerView;
    GroupRecyclerAdapter mAdapter;
    private TextView emptyView;
    private TextView txtChatEmpty;
    private String username;
    public static String id;
    NotificationManager manager;
    Notification myNotication;

    private  List<Notice> data = new ArrayList<>();

    class FakeContent implements TabContentFactory {
        private final Context mContext;

        public FakeContent(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumHeight(0);
            v.setMinimumWidth(0);
            return v;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewer_pager);

        data2 = new ArrayList<>();
        // init tabhost
        this.initializeTabHost(savedInstanceState);

        // init ViewPager
        this.initializeViewPager();
        tabHost.setCurrentTab(1);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_mylauncher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        //setoffscreenpagelimit
        viewPager.setOffscreenPageLimit(10);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        username = sharedPreferences.getString(config.Email, "") ;

        getMemberID();

        manager = (NotificationManager)getSystemService(getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent = new Intent(getApplicationContext(), viewPager.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent, 0);

        Notification.Builder builder = new Notification.Builder(getApplicationContext());

        builder.setAutoCancel(false);
        builder.setTicker("Welcome message");
        builder.setContentTitle("YeSplit Notification");
        builder.setContentText("Welcome");
        builder.setSmallIcon(R.mipmap.ic_mylauncher);
        builder.setContentIntent(pendingIntent);
        builder.setOngoing(false); // set permanet
        builder.setSubText("This is app...");   //API level 16
        builder.setNumber(100);
        builder.build();

        myNotication = builder.getNotification();
        manager.notify(11, myNotication);

    }


    private void initializeViewPager() {
        List<Fragment> fragments = new Vector<Fragment>();

        fragments.add(new Notification_Fragment());
        fragments.add(new Home_Fragment());
        fragments.add(new ChatRoom_Fragment());
        fragments.add(new BuddyFragment());

        this.myViewPagerAdapter = new viewPagerFragmentAdapter(
                getSupportFragmentManager(), fragments);
        this.viewPager = (ViewPager) super.findViewById(R.id.viewPager);
        this.viewPager.setAdapter(this.myViewPagerAdapter);
        this.viewPager.addOnPageChangeListener(this);

         onRestart();

    }

    private void initializeTabHost(Bundle args) {

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();

        for (int i = 0; i <= 3; i++) {

            TabHost.TabSpec tabSpec;
            tabSpec = tabHost.newTabSpec("Tab " + i);
            if (i == 0) {
                tabSpec.setIndicator("", ResourcesCompat.getDrawable(getResources(), R.drawable.ic_notification, null));
            } else if (i == 1) {
                tabSpec.setIndicator("", ResourcesCompat.getDrawable(getResources(), R.drawable.ic_home, null));
            } else if (i == 2) {
                tabSpec.setIndicator("", ResourcesCompat.getDrawable(getResources(), R.drawable.ic_chat, null));
            } else if (i == 3) {
                tabSpec.setIndicator("", ResourcesCompat.getDrawable(getResources(), R.drawable.ic_friend, null));
            }

            tabSpec.setContent(new FakeContent(this));
            tabHost.addTab(tabSpec);

        }
        tabHost.setOnTabChangedListener(this);

    }


    @Override
    public void onTabChanged(String tabId) {
        int pos = this.tabHost.getCurrentTab();
        this.viewPager.setCurrentItem(pos);

        HorizontalScrollView hScrollView = (HorizontalScrollView) findViewById(R.id.hScrollView);
        View tabView = tabHost.getCurrentTabView();
        int scrollPos = tabView.getLeft()
                - (hScrollView.getWidth() - tabView.getWidth()) / 2;
        hScrollView.smoothScrollTo(scrollPos, 0);

    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        this.tabHost.setCurrentTab(position);

        if (position == 0)
            setTitle("Notices");
        else if (position == 1)
            setTitle("YeSplit");
        else if (position == 2)
            setTitle("Chats");
        else if (position == 3)
            setTitle("Buddy's");

    }

    private Boolean exit = false;

    @Override
    public void onBackPressed() {
        if (exit) {
            XxmpConnection.closeConnection();
            finishAffinity(); // finish activity
        } else {
            Toast.makeText(this, "Press Back again to Exit.",
                    Toast.LENGTH_SHORT).show();
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }

    }

    public void onGroupFAB(View v) {
        Intent intent = new Intent(getApplicationContext(), Add_group_Activity.class);
        startActivityForResult(intent, 2);

    }

    public void FAB_addChat(View v) {


        txtChatEmpty = (TextView) findViewById(R.id.txtChatEmpty);

        txtChatEmpty.setVisibility(View.GONE);

                Intent intent = new Intent(getApplicationContext(), add_chatRoom_Activity.class);
                startActivityForResult(intent, 1);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {

                final ListView lv = (ListView) findViewById(R.id.list_chatlist);

                String stredittext = data.getStringExtra("edittextvalue");
                strArr_Chat.add(stredittext);
                lv.setAdapter(new CustomChatListAdapter(this, strArr_Chat));
                //   adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(), stredittext, Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 2) {
            if (resultCode == RESULT_OK) {
                mRecyclerView = (RecyclerView) findViewById(R.id.list);
                emptyView = (TextView) findViewById(R.id.empty_view);
                //mRecyclerView.setHasFixedSize(true);
                //mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
                mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
                String groupName = data.getStringExtra("groupName");
                String groupType = data.getStringExtra("groupType");
                String groupID = data.getStringExtra("groupID");

                data2.add(new Group(groupName, groupID));
                data2.get(data2.size()-1).setId(groupID);
                mAdapter = new GroupRecyclerAdapter(data2, getApplication());
                mAdapter.notifyDataSetChanged();

                mRecyclerView.setAdapter(mAdapter);

                mRecyclerView.setVisibility(View.VISIBLE);
                emptyView.setVisibility(View.GONE);

            }
        }
    }

    public void addContactOnClick(View V) {

        /// Create Intent for SignUpActivity  and Start The Activity
        final ListView lv = (ListView) findViewById(R.id.list_contact);
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_roaster);
        dialog.setTitle("Add a new Buddy");

        final EditText ed_email = (EditText) dialog.findViewById(R.id.edRoasterEmail);
        final EditText ed_nickname = (EditText) dialog.findViewById(R.id.edRoasterNickname);

        Button btnSignUp = (Button) dialog.findViewById(R.id.registerBtn);

        // Set OnClick Listener on SignUp button

        btnSignUp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // TODO Auto-generated method stub
                String username = ed_email.getText().toString();

                String nickname = ed_nickname.getText().toString();

                configure(ProviderManager.getInstance());


                label:  try {
                    UserSearchManager search = new UserSearchManager(XxmpConnection.getConnection());

                    Form searchForm = search.getSearchForm("search." + XxmpConnection.getConnection().getServiceName());

                    Form answerForm = searchForm.createAnswerForm();
                    answerForm.setAnswer("Username", true);

                    answerForm.setAnswer("search", username);

                    org.jivesoftware.smackx.ReportedData data = search.getSearchResults(answerForm, "search." + XxmpConnection.getConnection().getServiceName());

                    if(data.getRows() != null)
                    {
                        Iterator<ReportedData.Row> it = data.getRows();
                        while(it.hasNext())
                        {
                            ReportedData.Row row = it.next();
                            Iterator iterator = row.getValues("jid");
                            if(iterator.hasNext())
                            {
                                String value = iterator.next().toString();
                                Log.i("Iteartor values......", " " + value);

                                ArrayList<String> strArr =new ArrayList<String>();

                                Roster roster = XxmpConnection.getConnection().getRoster();
                                Collection<RosterEntry> entries = roster.getEntries();


                                for (RosterEntry entry : entries) {

                                    strArr.add(entry.getUser());

                                }

                                strArr.add(username);
                                roster.createEntry(username, nickname, null);

                                lv.setAdapter(new FilterRoasterListAdapter(viewPager.this,strArr));
                                // jid: String, user: String, groups: String[]
                                Toast.makeText(getApplicationContext(), "New Contact Successfully Created ", Toast.LENGTH_LONG).show();

                                dialog.dismiss();

                                break label;
                            }
                            //Log.i("Iteartor values......"," "+value);
                        }

                        Toast.makeText(getApplicationContext(),"Username Not Exists",Toast.LENGTH_SHORT).show();


                    }


                } catch (XMPPException e) {
                    e.printStackTrace();
                }

            }
        });


        dialog.show();

    }

    public void configure(ProviderManager pm) {

//  Private Data Storage
        pm.addIQProvider("query", "jabber:iq:private", new PrivateDataManager.PrivateDataIQProvider());

//  Time
        try {
            pm.addIQProvider("query", "jabber:iq:time", Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (ClassNotFoundException e) {
            Log.w("TestClient", "Can't load class for org.jivesoftware.smackx.packet.Time");
        }

//  Roster Exchange
        pm.addExtensionProvider("x", "jabber:x:roster", new RosterExchangeProvider());

//  Message Events
        pm.addExtensionProvider("x", "jabber:x:event", new MessageEventProvider());

//  Chat State
        pm.addExtensionProvider("active", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("composing", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("paused", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("inactive", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());
        pm.addExtensionProvider("gone", "http://jabber.org/protocol/chatstates", new ChatStateExtension.Provider());

//  XHTML
        pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im", new XHTMLExtensionProvider());

//  Group Chat Invitations
        pm.addExtensionProvider("x", "jabber:x:conference", new GroupChatInvitation.Provider());

//  Service Discovery # Items
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#items", new DiscoverItemsProvider());

//  Service Discovery # Info
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#info", new DiscoverInfoProvider());

//  Data Forms
        pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

//  MUC User
        pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user", new MUCUserProvider());

//  MUC Admin
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin", new MUCAdminProvider());

//  MUC Owner
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner", new MUCOwnerProvider());

//  Delayed Delivery
        pm.addExtensionProvider("x", "jabber:x:delay", new DelayInformationProvider());

//  Version
        try {
            pm.addIQProvider("query", "jabber:iq:version", Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e) {
            //  Not sure what's happening here.
        }

//  VCard
        pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

//  Offline Message Requests
        pm.addIQProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageRequest.Provider());

//  Offline Message Indicator
        pm.addExtensionProvider("offline", "http://jabber.org/protocol/offline", new OfflineMessageInfo.Provider());

//  Last Activity
        pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

//  User Search
        pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

//  SharedGroupsInfo
        pm.addIQProvider("sharedgroup", "http://www.jivesoftware.org/protocol/sharedgroup", new SharedGroupsInfo.Provider());

//  JEP-33: Extended Stanza Addressing
        pm.addExtensionProvider("addresses", "http://jabber.org/protocol/address", new MultipleAddressesProvider());

//   FileTransfer
        pm.addIQProvider("si", "http://jabber.org/protocol/si", new StreamInitiationProvider());

        pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams", new BytestreamsProvider());

//  Privacy
        pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
        pm.addIQProvider("command", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider());
        pm.addExtensionProvider("malformed-action", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.MalformedActionError());
        pm.addExtensionProvider("bad-locale", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadLocaleError());
        pm.addExtensionProvider("bad-payload", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadPayloadError());
        pm.addExtensionProvider("bad-sessionid", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.BadSessionIDError());
        pm.addExtensionProvider("session-expired", "http://jabber.org/protocol/commands", new AdHocCommandDataProvider.SessionExpiredError());

    }

    private void getGroup(){
        class getGroup extends AsyncTask<Void,Void,String>{
            //ProgressDialog loading;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(viewPager.this, "Fetching...", "Wait...", false, false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                showGroup(s);
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();

                String s = rh.sendGetRequestParam(config.url_viewAllGroup, id);
                return s;
            }
        }
        getGroup ge = new getGroup();
        ge.execute();
    }

    private void showGroup(String json){
        JSONObject jsonObject = null;

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        emptyView = (TextView) findViewById(R.id.empty_view);
        //mRecyclerView.setHasFixedSize(true);

        try {
            jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String name = jo.getString(config.KEY_GROUP_NAME);
                String type = jo.getString(config.KEY_GROUP_TYPE);
                String groupid = jo.getString(config.KEY_GROUP_ID);

                data2.add(new Group(name, groupid));
                data2.get(i).setId(groupid);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new GroupRecyclerAdapter(data2, getApplication());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }
    private void SavePreferences(String key, String value){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();

    }


    private void getMemberID(){
        class GetMemberID extends AsyncTask<Void,Void,String>{

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //loading = ProgressDialog.show(viewPager.this,"Fetching...","Wait...",false,false);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                showMemberID(s);
                getGroup();
            }

            @Override
            protected String doInBackground(Void... params) {
                RequestHandler rh = new RequestHandler();

                String s = rh.sendGetRequestParam(config.url_getMember, username);
                return s;
            }
        }
        GetMemberID ge = new GetMemberID();
        ge.execute();
    }

    private void showMemberID(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);
            JSONObject c = result.getJSONObject(0);

            id = c.getString(config.TAG_ID);
            SavePreferences(config.MemberID, id);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}