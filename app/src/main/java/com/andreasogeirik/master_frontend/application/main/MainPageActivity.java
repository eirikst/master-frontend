package com.andreasogeirik.master_frontend.application.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.application.main.fragments.AttendingEventsFragment;
import com.andreasogeirik.master_frontend.application.settings.SettingsActivity;
import com.andreasogeirik.master_frontend.application.auth.entrance.EntranceActivity;
import com.andreasogeirik.master_frontend.application.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.application.user.profile.ProfileActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.application.event.create.CreateEventActivity;
import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.LogoutHandler;

import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainPageActivity extends AppCompatActivity implements EventView,
        AttendingEventsFragment.AttendingEventsListener {
    private EventPresenter presenter;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private AttendingEventsFragment attendingFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page_activity);
        ButterKnife.bind(this);

        //TODO:saveinstancestate

        presenter = new MainPagePresenterImpl(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void initGUI() {
        setupToolbar();
        setupAttendingFragment();
    }

    /*
     * Toolbar setup
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setupAttendingFragment() {
        RelativeLayout fragmentContainer = (RelativeLayout)findViewById(
                R.id.attending_events_fragment_container);
        attendingFragment = AttendingEventsFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(fragmentContainer.getId(),
                attendingFragment, "").commit();
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
        Intent i;
        switch (item.getItemId()) {
            case R.id.sign_out:
                LogoutHandler.getInstance().logOut();
                i = new Intent(this, EntranceActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                this.startActivity(i);
                return true;
            case R.id.create_event:
                i = new Intent(this, CreateEventActivity.class);
                this.startActivity(i);
                return true;
            case R.id.settings:
                i = new Intent(this, SettingsActivity.class);
                this.startActivity(i);
                return true;
            case R.id.my_profile:
                i = new Intent(this, ProfileActivity.class);
                i.putExtra("user", CurrentUser.getInstance().getUser());
                this.startActivity(i);
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void findImage(String imageUri) {
        presenter.findImage(imageUri);
    }

    @Override
    public void setAttendingEvents(Set<Event> events) {
        attendingFragment.setEventsList(events);
    }

    @Override
    public void setAttendingImage(String imageUri, Bitmap bitmap) {
        attendingFragment.setImage(imageUri, bitmap);
    }


}
