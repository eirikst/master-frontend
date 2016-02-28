package com.andreasogeirik.master_frontend.application.event.main;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.layout.adapter.EventMainAdapter;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.EventPost;

import java.util.ArrayList;

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
    private TextView eventName;

    private EventPresenter presenter;
    private ProgressBarManager progressBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_activity);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        this.presenter = new EventPresenterImpl(this);
        this.progressBarManager = new ProgressBarManager(this, listView, progressView);

        headerView = getLayoutInflater().inflate(R.layout.event_list_header, null);
        listView.addHeaderView(headerView);


        this.eventImageContainer = headerView.findViewById(R.id.event_image_container);
        this.imageView = (ImageView)headerView.findViewById(R.id.event_image);
        this.eventName = (TextView)headerView.findViewById(R.id.event_name);
        presenter.getEvent(1);
        adapter = new EventMainAdapter(this, new ArrayList<EventPost>());
        listView.setAdapter(adapter);
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
//        this.locationView.setText(event.getLocation());
//        this.description.setText(event.getDescription());
        if (!event.getImageURI().isEmpty()){
            presenter.findImage(event.getImageURI());
        }
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
        this.imageView.setImageBitmap(image);
        this.eventImageContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void imageLoadError() {

    }
}
