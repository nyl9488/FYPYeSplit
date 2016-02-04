package com.example.yloon.fypandroidapp.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.example.yloon.fypandroidapp.Card;
import com.example.yloon.fypandroidapp.R;
import com.example.yloon.fypandroidapp.adapter.NoticeRecyclerAdapter;
import com.example.yloon.fypandroidapp.model.Notice;
import com.example.yloon.fypandroidapp.view.About_Activity;
import com.example.yloon.fypandroidapp.view.Feedback;
import com.example.yloon.fypandroidapp.view.SettingsActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notification_Fragment extends Fragment implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {

    RecyclerView mRecyclerViewNotice;
    NoticeRecyclerAdapter mAdapterNotice;
    private SliderLayout mDemoSlider;
    private  List<Notice> data = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        mRecyclerViewNotice = (RecyclerView) view.findViewById(R.id.list_notice);
       /* btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                manager.cancel(11);
            }
        });*/

        mDemoSlider = (SliderLayout)view.findViewById(R.id.slider);

        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Hannibal", "http://static2.hypable.com/wp-content/uploads/2013/12/hannibal-season-2-release-date.jpg");
        url_maps.put("Big Bang Theory", "http://tvfiles.alphacoders.com/100/hdclearart-10.png");
        url_maps.put("House of Cards", "http://cdn3.nflximg.net/images/3093/2043093.jpg");
        url_maps.put("Game of Thrones", "http://images.boomsbeat.com/data/images/full/19640/game-of-thrones-season-4-jpg.jpg");

        HashMap<String,Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("Forest", R.drawable.one);
        file_maps.put("River", R.drawable.two);
        file_maps.put("Android", R.drawable.three);
        file_maps.put("Mario", R.drawable.five);

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(getActivity());
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
        mDemoSlider.setPresetTransformer("Background2Foreground");



        mRecyclerViewNotice = (RecyclerView) view.findViewById(R.id.list_notice);

        mRecyclerViewNotice.setLayoutManager(new LinearLayoutManager(getActivity()));

    //    mRecyclerViewNotice.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL_LIST));
      //  mRecyclerViewNotice.setHasFixedSize(true);
     //   mRecyclerViewNotice.setItemAnimator(new DefaultItemAnimator());

        data.add(new Notice("Welcome", "sa "));
        data.add(new Notice("Welcome", "sa "));

        mAdapterNotice = new NoticeRecyclerAdapter(data, getContext());
        mAdapterNotice.notifyDataSetChanged();
        mRecyclerViewNotice.setAdapter(mAdapterNotice);




        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_notification, menu);
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
            Intent productIntent = new Intent(getActivity(), About_Activity.class);
            //Start Product Activity
            startActivity(productIntent);
            return true;
        } else if (id == R.id.action_settings) {
            //Create Intent for Product Activity
            Intent productIntent = new Intent(getActivity(), SettingsActivity.class);
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
        else if (id == R.id.action_feedback) {
            //Create Intent for Product Activity
            Intent productIntent = new Intent(getActivity(), Feedback.class);
            //Start Product Activity
            startActivity(productIntent);
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getActivity(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();

        if(slider.getBundle().get("extra")=="Android"){
            String url = "http://developer.android.com/sdk/index.html";
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        }
    }
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {}
}





