package com.andreasogeirik.master_frontend.application.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.AttendingEventsFragment;
import com.andreasogeirik.master_frontend.application.main.fragments.my_events.MyEventsFragment;
import com.andreasogeirik.master_frontend.application.auth.entrance.EntranceActivity;
import com.andreasogeirik.master_frontend.application.main.fragments.recommended_events.RecommendedEventsFragment;
import com.andreasogeirik.master_frontend.application.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.layout.adapter.MainPagerAdapter;


import butterknife.Bind;
import butterknife.ButterKnife;

public class MainPageActivity extends AppCompatActivity implements EventView,
        AttendingEventsFragment.AttendingEventsListener, MyEventsFragment.MyEventsListener,
        RecommendedEventsFragment.RecommendedEventsListener,
        ViewPager.OnPageChangeListener {
    private EventPresenter presenter;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.sliding_tabs)
    TabLayout tabLayout;
    private MainPagerAdapter pagerAdapter;


    //private AttendingEventsFragment attendingFragment;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            Toast.makeText(MainPageActivity.this, message, Toast.LENGTH_LONG).show();
            Log.d("receiver", "Got message: " + message);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_activity);
        ButterKnife.bind(this);
        presenter = new MainPagePresenterImpl(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void initGUI() {
        setupToolbar();

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        pagerAdapter = new MainPagerAdapter(getSupportFragmentManager(), this);
        viewPager.setAdapter(pagerAdapter);
        viewPager.addOnPageChangeListener(this);

        // Give the TabLayout the ViewPager
        tabLayout.post(new Runnable() {//TODO:Skrive om denne hvis mulig
            @Override
            public void run() {
                tabLayout.setupWithViewPager(viewPager);
            }
        });

    }

    /*
     * Toolbar setup
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    public void navigateToLogin() {
        Intent i = new Intent(this, EntranceActivity.class);
        startActivity(i);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    //TODO: Denne må inspiseres...
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (!com.andreasogeirik.master_frontend.layout.Toolbar.onOptionsItemSelected(item, this)) {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //do nothing
    }

    @Override
    public void onPageSelected(int position) {
        Fragment fragment = pagerAdapter.getRegisteredFragment(position);

        //this is done because image would not always load
        if(fragment != null) {
            if(fragment instanceof AttendingEventsFragment) {
                ((AttendingEventsFragment) fragment).updateListView();
            }
            else if(fragment instanceof RecommendedEventsFragment) {
                ((RecommendedEventsFragment) fragment).updateListView();
            }
            else if(fragment instanceof MyEventsFragment) {
                ((MyEventsFragment) fragment).updateListView();
            }
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        //do nothing
    }
}
