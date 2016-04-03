package com.andreasogeirik.master_frontend.application.user.profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.AttendingEventsFragment;
import com.andreasogeirik.master_frontend.application.user.profile.fragments.MyProfileHeader;

public class AttendingEventsActivity extends AppCompatActivity implements
        AttendingEventsFragment.AttendingEventsListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_events);

        RelativeLayout fragmentContainer = (RelativeLayout)findViewById(
                R.id.attending_events_fragment_container);
        AttendingEventsFragment fragment = AttendingEventsFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(fragmentContainer.getId(),
                fragment, "").commit();

    }
}
