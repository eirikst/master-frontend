package com.andreasogeirik.master_frontend.application.user.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.AttendingEventsFragment;
import com.andreasogeirik.master_frontend.application.user.profile.fragments.MyProfileHeader;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AttendingEventsActivity extends AppCompatActivity implements
        AttendingEventsFragment.AttendingEventsListener {
    @Bind(R.id.home)
    Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attending_events);

        ButterKnife.bind(this);

        RelativeLayout fragmentContainer = (RelativeLayout)findViewById(
                R.id.attending_events_fragment_container);
        AttendingEventsFragment fragment = AttendingEventsFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(fragmentContainer.getId(),
                fragment, "").commit();

    }

    @OnClick(R.id.home)
    public void navigateToHome() {
        Intent intent = new Intent(this, MainPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
}
