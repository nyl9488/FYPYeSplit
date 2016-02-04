package com.example.yloon.fypandroidapp.fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yloon.fypandroidapp.Calculator;
import com.example.yloon.fypandroidapp.Card;
import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.adapter.GroupRecyclerAdapter;
import com.example.yloon.fypandroidapp.model.Group;
import com.example.yloon.fypandroidapp.server.RequestHandler;
import com.example.yloon.fypandroidapp.server.config;
import com.example.yloon.fypandroidapp.service.RecyclerItemClickListener;
import com.example.yloon.fypandroidapp.view.About_Activity;
import com.example.yloon.fypandroidapp.view.Feedback;
import com.example.yloon.fypandroidapp.view.GroupInfoActivity;
import com.example.yloon.fypandroidapp.view.SettingsActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home_Fragment extends Fragment {


  //  private RecyclerView recyclerView;
   RecyclerView mRecyclerView;
    GroupRecyclerAdapter mAdapter;
    private TextView emptyView;
    TextView groupTitle;

    SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        //mRecyclerView.setHasFixedSize(true);
      //  mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        emptyView = (TextView) view.findViewById(R.id.empty_view);
     //   mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.GONE);
        emptyView.setVisibility(View.VISIBLE);
       // recyclerView = (RecyclerView) view.findViewById(R.id.fab_recycler_view);


        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent i = new Intent(getActivity(), GroupInfoActivity.class);

                        String id =viewPager.data2.get(position).getId();
                        //Start Product Activity
                        groupTitle = (TextView) view.findViewById(R.id.title);
                        String gTitle = groupTitle.getText().toString();
                        i.putExtra("GTITLE",id);
                        startActivity(i);
                        // Toast.makeText(getActivity(), "sssdsdsds" + position, Toast.LENGTH_LONG).show();
                        //Log.e("@@@@@", "" + position);
                    }
                })
        );
       // setupRecyclerView();

        mSwipeRefreshLayout=(SwipeRefreshLayout)view.findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                getGroup();
                refreshItems();
            }
        });


        return view;


    }
    void refreshItems() {
        // Load items
        // ...

        // Load complete
        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        mSwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //When Product action item is clicked
        if (id == R.id.action_about) {
            //Create Intent for Product Activity
            Intent productIntent = new Intent(getActivity(),About_Activity.class);
            //Start Product Activity
            startActivity(productIntent);
            return true;
        }

        else if (id == R.id.action_feedback) {
            //Create Intent for Product Activity
            Intent productIntent = new Intent(getActivity(),Feedback.class);
            //Start Product Activity
            startActivity(productIntent);
            return true;
        }
        else if (id == R.id.action_card) {
            //Create Intent for Product Activity
            Intent productIntent = new Intent(getActivity(),Card.class);
            //Start Product Activity
            startActivity(productIntent);
            return true;
        }
        else if (id == R.id.action_total) {
            //Create Intent for Product Activity
            Intent productIntent = new Intent(getActivity(),Calculator.class);
            //Start Product Activity
            startActivity(productIntent);
            return true;
        }


        else if (id == R.id.action_settings) {
            //Create Intent for Product Activity
            Intent productIntent = new Intent(getActivity(),SettingsActivity.class);
            //Start Product Activity
            startActivity(productIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void getGroup(){
        class getGroup extends AsyncTask<Void,Void,String> {
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

                String s = rh.sendGetRequestParam(config.url_viewAllGroup, viewPager.id);
                return s;
            }
        }
        getGroup ge = new getGroup();
        ge.execute();
    }

    private void showGroup(String json){
        JSONObject jsonObject = null;
        viewPager.data2 = new ArrayList<>();
        mRecyclerView = (RecyclerView) getView().findViewById(R.id.list);
        emptyView = (TextView) getView().findViewById(R.id.empty_view);
        //mRecyclerView.setHasFixedSize(true);

        try {
            jsonObject = new JSONObject(json);
            JSONArray result = jsonObject.getJSONArray(config.TAG_JSON_ARRAY);

            for(int i = 0; i<result.length(); i++){
                JSONObject jo = result.getJSONObject(i);
                String name = jo.getString(config.KEY_GROUP_NAME);
                String type = jo.getString(config.KEY_GROUP_TYPE);
                String groupid = jo.getString(config.KEY_GROUP_ID);

                viewPager.data2.add(new Group(name, groupid));
                viewPager.data2.get(i).setId(groupid);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new GroupRecyclerAdapter(viewPager.data2, getContext());
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.GONE);
    }

}





