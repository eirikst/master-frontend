package com.andreasogeirik.master_frontend.application.main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
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
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.AttendingEventsFragment;
import com.andreasogeirik.master_frontend.application.main.fragments.my_events.MyEventsFragment;
import com.andreasogeirik.master_frontend.application.auth.entrance.EntranceActivity;
import com.andreasogeirik.master_frontend.application.main.fragments.recommended_events.RecommendedEventsFragment;
import com.andreasogeirik.master_frontend.application.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.main.notification.NotificationCenterDialogFragment;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.layout.adapter.MainPagerAdapter;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;


import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainPageActivity extends AppCompatActivity implements EventView,
        AttendingEventsFragment.AttendingEventsListener, MyEventsFragment.MyEventsListener,
        RecommendedEventsFragment.RecommendedEventsListener,
        ViewPager.OnPageChangeListener {
    private EventPresenter presenter;

    @Bind(R.id.progress)
    View progressView;
    @Bind(R.id.main_container)
    View mainContainer;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.viewpager)
    ViewPager viewPager;
    @Bind(R.id.sliding_tabs)
    TabLayout tabLayout;
    @Bind(R.id.notification_img)
    ImageView notificationImg;
    @Bind(R.id.notification_count)
    TextView notificationCount;

    private MainPagerAdapter pagerAdapter;
    private ProgressBarManager progressBarManager;


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

        setupToolbar();
        progressBarManager = new ProgressBarManager(this, mainContainer, progressView);
        presenter = new MainPagePresenterImpl(this);

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));
    }

    //TODO:remove if not used bro
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void initGUI() {

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

        notificationImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.accessNotificationCenter();
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

    @Override
    public void showNotificationCenter(Set<Object> notifications) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = NotificationCenterDialogFragment.newInstance(
                new HashSet<Object>(notifications));
        newFragment.show(ft, "dialog");
    }


    public void navigateToLogin() {
        Intent i = new Intent(this, EntranceActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
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
    public void setNotificationCount(int count) {
        notificationCount.setText("" + count);
        notificationCount.setVisibility(View.VISIBLE);
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

    @Override
    public void showProgress() {
        this.progressBarManager.showProgress(true);
    }

    @Override
    public void hideProgress() {
        this.progressBarManager.showProgress(false);
    }
}
