package com.andreasogeirik.master_frontend.application.main;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.AttendingEventsFragment;
import com.andreasogeirik.master_frontend.application.main.fragments.my_events.MyEventsFragment;
import com.andreasogeirik.master_frontend.application.auth.entrance.EntranceActivity;
import com.andreasogeirik.master_frontend.application.main.fragments.recommended_events.RecommendedEventsFragment;
import com.andreasogeirik.master_frontend.application.main.interfaces.MainPagePresenter;
import com.andreasogeirik.master_frontend.application.main.interfaces.MainPageView;
import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.main.notification.NotificationCenterDialogFragment;
import com.andreasogeirik.master_frontend.gcm.QuickstartPreferences;
import com.andreasogeirik.master_frontend.gcm.RegistrationIntentService;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.layout.adapter.MainPagerAdapter;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;


import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainPageActivity extends AppCompatActivity implements MainPageView,
        AttendingEventsFragment.AttendingEventsListener, MyEventsFragment.MyEventsListener,
        RecommendedEventsFragment.RecommendedEventsListener,
        ViewPager.OnPageChangeListener, NotificationCenterDialogFragment.NotificationCenterListener {
    private String tag = getClass().getSimpleName();
    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private BroadcastReceiver registrationBroadcastReceiver;
    public boolean isReceiverRegistered;

    private MainPagePresenter presenter;

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

        registrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SharedPreferences sharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(context);
                boolean sentToken = sharedPreferences
                        .getBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false);
                if (sentToken) {
                    //ok
                } else {
                    //TODO:possibly handle this
                }
            }
        };

        registerReceiver();

        if (checkPlayServices()) {
            presenter = new MainPagePresenterImpl(this);
        }
        else {
            Log.w(tag, "No Play Services on device");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int code = api.isGooglePlayServicesAvailable(this);

        if (code == ConnectionResult.SUCCESS) {
            presenter.findFriendships();
        }
        else {
            GooglePlayServicesUtil.getErrorDialog(ConnectionResult.SERVICE_MISSING, this, 1).show();
        }

        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(registrationBroadcastReceiver);
        isReceiverRegistered = false;
    }

    private void registerReceiver(){
        if(!isReceiverRegistered) {
            LocalBroadcastManager.getInstance(this).registerReceiver(registrationBroadcastReceiver,
                    new IntentFilter(QuickstartPreferences.REGISTRATION_COMPLETE));
            isReceiverRegistered = true;
        }
    }

    @Override
    public void initGUI() {

        setContentView(R.layout.main_page_activity);
        // Start IntentService to register this application with GCM.
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);

        ButterKnife.bind(this);
        setupToolbar();
        progressBarManager = new ProgressBarManager(this, mainContainer, progressView);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-event-name"));

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


    public void navigateToEntrance() {
        Intent i = new Intent(this, EntranceActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.fadeout, R.anim.fadein);
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
        if(count > 0) {
            notificationCount.setText("" + count);
            notificationCount.setVisibility(View.VISIBLE);
        }
        else {
            notificationCount.setVisibility(View.GONE);
        }
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


    //TODO:Fjern
    void notifyMe() {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_directions_run_black_24dp)
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainPageActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainPageActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1232, mBuilder.build());
    }

    /**
     * Check the device to make sure it has the Google Play Services APK. If
     * it doesn't, display a dialog that allows users to download the APK from
     * the Google Play Store or enable it in the device's system settings.
     */
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
                        .show();
            } else {
                Log.i(tag, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }
}
