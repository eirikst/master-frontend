package com.andreasogeirik.master_frontend.application.event.main;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.edit.EditEventActivity;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.application.event.main.participants.ParticipantsActivity;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.layout.adapter.EventMainAdapter;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.EventPost;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EventActivity extends AppCompatActivity implements EventView, OnClickListener {

    // Containers
    @Bind(R.id.event_progress)
    View progressView;

    @Bind(R.id.event_list_view)
    ListView listView;

    private ImageView imageView;

    private EventMainAdapter adapter;


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.home)
    Button homeBtn;

    private View easyDiff;
    private View mediumDiff;
    private View hardDiff;

    private View headerView;
    private View eventImageContainer;
    private Button attendButton;
    private Button unAttendButton;
    private Button editButton;
    private Button deleteButton;

    private TextView eventName;
    private TextView startTime;
    private View endTimePanel;
    private TextView endTime;
    private TextView eventLocation;
    private TextView eventDescription;

    private TextView numberOfParticipants;


    private EventPresenter presenter;
    private ProgressBarManager progressBarManager;

    private static int EDIT_EVENT_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        ButterKnife.bind(this);
        this.progressBarManager = new ProgressBarManager(this, listView, progressView);
        setupToolbar();

        try {
            this.presenter = new EventPresenterImpl(this, (Event) getIntent().getSerializableExtra("event"));
            this.presenter.initGui();
            this.presenter.setEventAttributes();
        } catch (ClassCastException e) {
            throw new ClassCastException(e + "/nObject in Intent bundle cannot " +
                    "be cast to User in " + this.toString());
        }
    }

    /*
     * Toolbar setup
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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

    @Override
    public void setEventAttributes(String name, String location, String description, String startTime, String participants) {

        this.unAttendButton.setVisibility(View.GONE);
        this.attendButton.setVisibility(View.GONE);

        this.eventName.setText(name);
        this.eventLocation.append(location);
        this.eventDescription.append(description);
        this.startTime.append(startTime);

        this.numberOfParticipants.setText(participants);
    }

    @Override
    public void updateEndTime(String endTime) {
        this.endTime.append(endTime);
        this.endTimePanel.setVisibility(View.VISIBLE);
    }

    @Override
    public void setParticipants(String participants) {
        this.numberOfParticipants.setText(participants);
    }

    @Override
    public void setAttendButton() {
        this.attendButton.setVisibility(View.VISIBLE);
        this.unAttendButton.setVisibility(View.GONE);
    }

    @Override
    public void setEditButton() {
        this.editButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setDeleteButton() {
        this.deleteButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setUnAttendButton() {
        this.unAttendButton.setVisibility(View.VISIBLE);
        this.attendButton.setVisibility(View.GONE);
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
    public void setImage(String imageUri) {
        if(imageUri != null && !imageUri.isEmpty()) {
            Picasso.with(this)
                    .load(imageUri)
                    .error(R.drawable.default_event)
                    .resize(Constants.EVENT_IMAGE_WIDTH, Constants.EVENT_IMAGE_HEIGHT)
                    .into(imageView);
        }
        else {
            Picasso.with(this)
                    .load(R.drawable.default_event)
                    .resize(Constants.EVENT_IMAGE_WIDTH, Constants.EVENT_IMAGE_HEIGHT)
                    .into(imageView);
        }
        this.eventImageContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void setDifficultyView(int difficulty) {
        if(difficulty == Constants.EVENT_DIFFICULTY_MEDIUM) {
            easyDiff.setVisibility(View.GONE);
            mediumDiff.setVisibility(View.VISIBLE);
            hardDiff.setVisibility(View.GONE);

        }
        else if(difficulty == Constants.EVENT_DIFFICULTY_HARD) {
            easyDiff.setVisibility(View.GONE);
            mediumDiff.setVisibility(View.GONE);
            hardDiff.setVisibility(View.VISIBLE);

        }
        else {
            easyDiff.setVisibility(View.VISIBLE);
            mediumDiff.setVisibility(View.GONE);
            hardDiff.setVisibility(View.GONE);

        }
    }

    @Override
    public void initGui() {
        headerView = getLayoutInflater().inflate(R.layout.event_list_header, null);
        listView.addHeaderView(headerView);
        this.numberOfParticipants = (TextView) headerView.findViewById(R.id.event_participants);
        this.attendButton = (Button) headerView.findViewById(R.id.event_attend);
        this.unAttendButton = (Button) headerView.findViewById(R.id.event_unattend);
        this.editButton = (Button) headerView.findViewById(R.id.event_edit);
        this.deleteButton = (Button) headerView.findViewById(R.id.event_delete);
        this.numberOfParticipants.setOnClickListener(this);
        this.attendButton.setOnClickListener(this);
        this.unAttendButton.setOnClickListener(this);
        this.editButton.setOnClickListener(this);
        this.deleteButton.setOnClickListener(this);

        this.eventImageContainer = headerView.findViewById(R.id.event_image_container);
        this.imageView = (ImageView) headerView.findViewById(R.id.event_image);
        this.eventName = (TextView) headerView.findViewById(R.id.event_name);
        this.startTime = (TextView) headerView.findViewById(R.id.event_startTime);
        this.endTime = (TextView) headerView.findViewById(R.id.event_end_time);
        this.endTimePanel = headerView.findViewById(R.id.event_end_time_panel);
        this.eventLocation = (TextView) headerView.findViewById(R.id.event_location);
        this.eventDescription = (TextView) headerView.findViewById(R.id.event_description);

        this.easyDiff = headerView.findViewById(R.id.difficulty_easy);
        this.mediumDiff = headerView.findViewById(R.id.difficulty_medium);
        this.hardDiff = headerView.findViewById(R.id.difficulty_hard);

        adapter = new EventMainAdapter(this, new ArrayList<EventPost>());
        listView.setAdapter(adapter);
    }

    @Override
    public void navigateToEditEvent(Event event) {
        Intent i = new Intent(this, EditEventActivity.class);
        i.putExtra("event", event);
        startActivity(i);
    }

    @Override
    public void navigateToMain() {
        Intent i = new Intent(this, MainPageActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
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

    @Override
    public void onBackPressed() {

        Intent i = getIntent();
        int requestCode = i.getIntExtra("requestCode", 0);
        if (requestCode == EDIT_EVENT_REQUEST) {
            Intent intent = new Intent(this, MainPageActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.event_delete:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                // set dialog message
                alertDialogBuilder
                        .setMessage("Vil du slette denne aktiviteten?")
                        .setCancelable(false)
                        .setPositiveButton("Nei", new DialogInterface.OnClickListener() {//this is really negative, wanted to change sides
                            public void onClick(DialogInterface dialog, int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Ja", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {//this is really positive, wanted to change sides
                                // if this button is clicked, close
                                // current activity
                                presenter.deleteEvent();
                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
                break;
            case R.id.event_edit:
                presenter.navigateToEditEvent();
                break;
            case R.id.event_participants:
                presenter.navigateToParticipants();
                break;
            case R.id.event_attend:
                presenter.attendEvent();
                break;
            case R.id.event_unattend:
                presenter.unAttendEvent();
                break;
        }
    }
}
