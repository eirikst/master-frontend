package com.andreasogeirik.master_frontend.application.event.main;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.layout.adapter.EventMainAdapter;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.EventPost;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.DateUtility;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import java.util.ArrayList;
import java.util.List;

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
    private Button unAttentButton;

    private TextView eventName;
    private TextView startTime;
    private TextView endTime;
    private TextView eventLocation;
    private TextView eventDescription;


    private EventPresenter presenter;
    private ProgressBarManager progressBarManager;
    private Event event;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        ButterKnife.bind(this);
        this.progressBarManager = new ProgressBarManager(this, listView, progressView);
        setupToolbar();


        try {
            this.presenter = new EventPresenterImpl(this);
        } catch (ClassCastException e) {
            throw new ClassCastException(e + "/nObject in Intent bundle cannot " +
                    "be cast to User in " + this.toString());
        }

        this.event = (Event) getIntent().getSerializableExtra("event");

        initGui();
        setEventView(event);
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
    public void setEventView(Event event) {
        this.eventName.setText(event.getName());

        this.startTime.setText("Tidspunkt (start): " + DateUtility.formatFull(event.getStartDate().getTime()));
        if (event.getEndDate() != null) {
            this.endTime.setText("Tidspunkt (slutt): " + DateUtility.formatFull(event.getEndDate().getTime()));
            this.endTime.setVisibility(View.VISIBLE);
        }
        this.eventLocation.setText("Sted: " + event.getLocation());
        this.eventDescription.setText("Detaljer: " + event.getDescription());
        if (!event.getImageURI().isEmpty()) {
            presenter.findImage(event.getImageURI());
        }
    }

    @Override
    public void setEventError(String errorMessage) {

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
    public void imageLoadError() {

    }

    @Override
    public void initGui() {
        headerView = getLayoutInflater().inflate(R.layout.event_list_header, null);
        listView.addHeaderView(headerView);

        User currentUser = CurrentUser.getInstance().getUser();
        boolean userInEvent = false;

        for (User user : event.getUsers()) {
            if (currentUser.getId() == user.getId()) {
                this.unAttentButton = (Button) headerView.findViewById(R.id.event_unattend);
                this.unAttentButton.setVisibility(View.VISIBLE);
                userInEvent = true;
                break;
            }
        }
        if (!userInEvent) {
            this.attendButton = (Button) headerView.findViewById(R.id.event_attend);
            this.attendButton.setVisibility(View.VISIBLE);
        }
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
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!com.andreasogeirik.master_frontend.layout.Toolbar.onOptionsItemSelected(item, this)) {
            return super.onOptionsItemSelected(item);
        }
        return true;

    }
}
