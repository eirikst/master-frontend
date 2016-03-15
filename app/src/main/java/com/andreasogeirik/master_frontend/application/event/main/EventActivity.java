package com.andreasogeirik.master_frontend.application.event.main;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.application.event.main.participants.ParticipantsActivity;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.layout.adapter.EventMainAdapter;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.EventPost;
import com.andreasogeirik.master_frontend.model.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity implements EventView {

    // Containers
    @Bind(R.id.event_progress)
    View progressView;

    @Bind(R.id.event_list_view)
    ListView listView;

    private ImageView imageView;

    private EventMainAdapter adapter;


    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private View headerView;
    private View eventImageContainer;
    private Button attendButton;
    private Button unAttendButton;

    private TextView eventName;
    private TextView startTime;
    private TextView endTime;
    private TextView eventLocation;
    private TextView eventDescription;

    private TextView numberOfParticipants;


    private EventPresenter presenter;
    private ProgressBarManager progressBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        ButterKnife.bind(this);
        this.progressBarManager = new ProgressBarManager(this, listView, progressView);
        setupToolbar();

        try {
            this.presenter = new EventPresenterImpl(this, (Event) getIntent().getSerializableExtra("event"));
        } catch (ClassCastException e) {
            throw new ClassCastException(e + "/nObject in Intent bundle cannot " +
                    "be cast to User in " + this.toString());
        }
        this.presenter.initGui();
        this.presenter.updateView();
    }

    /*
     * Toolbar setup
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void updateMandatoryFields(String name, String location, String description, String startTime, String participants) {

        this.unAttendButton.setVisibility(View.GONE);
        this.attendButton.setVisibility(View.GONE);

        this.eventName.setText(name);
        this.eventLocation.setText(location);
        this.eventDescription.setText(description);
        this.startTime.setText(startTime);

        this.numberOfParticipants.setText(participants);
    }

    @Override
    public void updateEndTime(String endTime) {
        this.endTime.setText(endTime);
        this.endTime.setVisibility(View.VISIBLE);
    }

    @Override
    public void setAttendButton() {
        this.attendButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUnAttendButton() {
        this.unAttendButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showErrorMessage(String error) {
        Toast.makeText(EventActivity.this, error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void showProgress() {
        progressBarManager.showProgress(true);
    }

    @Override
    public void hideProgress() {
        progressBarManager.showProgress(false);
    }

    @Override
    public void setImage(Bitmap image) {
        this.imageView.setImageBitmap(image);
        this.eventImageContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void initGui() {
        headerView = getLayoutInflater().inflate(R.layout.event_list_header, null);
        listView.addHeaderView(headerView);
        this.numberOfParticipants = (TextView) headerView.findViewById(R.id.event_participants);
        this.unAttendButton = (Button) headerView.findViewById(R.id.event_unattend);
        this.unAttendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.unAttendEvent();
            }
        });
        this.attendButton = (Button) headerView.findViewById(R.id.event_attend);
        this.attendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.attendEvent();
            }
        });

        this.numberOfParticipants.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.navigateToParticipants();
            }
        });

        this.eventImageContainer = headerView.findViewById(R.id.event_image_container);
        this.imageView = (ImageView) headerView.findViewById(R.id.event_image);
        this.eventName = (TextView) headerView.findViewById(R.id.event_name);
        this.startTime = (TextView) headerView.findViewById(R.id.event_startTime);
        this.endTime = (TextView) headerView.findViewById(R.id.event_endTime);
        this.eventLocation = (TextView) headerView.findViewById(R.id.event_location);
        this.eventDescription = (TextView) headerView.findViewById(R.id.event_description);

        adapter = new EventMainAdapter(this, new ArrayList<EventPost>());
        listView.setAdapter(adapter);
    }

    @Override
    public void navigateToParticipants(Set<User> _users) {
        Intent i = new Intent(this, ParticipantsActivity.class);
        HashSet<User> users = new HashSet(_users);
        i.putExtra("users", users);
        startActivity(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!com.andreasogeirik.master_frontend.layout.Toolbar.onOptionsItemSelected(item, this)) {
            return super.onOptionsItemSelected(item);
        }
        return true;

    }
}
