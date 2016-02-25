package com.andreasogeirik.master_frontend.application.event.main;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.model.Event;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity implements EventView {

    // Containers
    @Bind(R.id.event_progress)
    View progressView;
    @Bind(R.id.event_view)
    View eventView;


    @Bind(R.id.event_name)
    TextView nameView;
    @Bind(R.id.event_location)
    TextView locationView;
    @Bind(R.id.event_description)
    TextView description;


    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private EventPresenter presenter;
    private ProgressBarManager progressBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        this.presenter = new EventPresenterImpl(this);
        this.progressBarManager = new ProgressBarManager(this, eventView, progressView);

        presenter.getEvent(1);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void setEventView(Event event) {
        this.nameView.setText(event.getName());
        this.locationView.setText(event.getLocation());
        this.description.setText(event.getDescription());
    }

    @Override
    public void setEventError(String errorMessage) {

    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setImage(Bitmap image) {

    }

    @Override
    public void imageLoadError() {

    }
}
