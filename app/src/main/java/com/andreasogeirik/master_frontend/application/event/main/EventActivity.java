package com.andreasogeirik.master_frontend.application.event.main;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.layout.adapter.EventMainAdapter;
import com.andreasogeirik.master_frontend.layout.adapter.PostListAdapter;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.EventPost;
import com.andreasogeirik.master_frontend.model.UserPost;

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

//    @Bind(R.id.event_location)
//    TextView locationView;
//    @Bind(R.id.event_description)
//    TextView description;


    @Bind(R.id.toolbar)
    Toolbar toolbar;

    private View headerView;

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
        imageView = (ImageView)headerView.findViewById(R.id.event_image);
        presenter.getEvent(12);
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
//        this.nameView.setText(event.getName());
//        this.locationView.setText(event.getLocation());
//        this.description.setText(event.getDescription());
        presenter.findImage(event.getImageURI());
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
    }

    @Override
    public void imageLoadError() {

    }
}
