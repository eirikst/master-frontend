package com.andreasogeirik.master_frontend.application.event.main.participants;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.EventPresenterImpl;
import com.andreasogeirik.master_frontend.application.event.main.participants.interfaces.ParticipantsPresenter;
import com.andreasogeirik.master_frontend.application.event.main.participants.interfaces.ParticipantsView;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.layout.adapter.ParticipantsListAdapter;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.User;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ParticipantsActivity extends AppCompatActivity implements ParticipantsView, AdapterView.OnItemClickListener {

    private ParticipantsListAdapter listAdapter;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.home)
    Button homeBtn;
    @Bind(R.id.event_participants)
    ListView listView;

    private ParticipantsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.participants_activity);
        ButterKnife.bind(this);
        try {
            this.presenter = new ParticipantsPresenterImpl(this, (HashSet<User>) getIntent().getSerializableExtra("users"));
        } catch (ClassCastException e) {
            throw new ClassCastException(e + "/nObject in Intent bundle cannot " +
                    "be cast to User in " + this.toString());
        }
        setupToolbar();
        this.presenter.initGui();
    }

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
    public void initGui(List<User> participants) {
        listAdapter = new ParticipantsListAdapter(this, participants);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.profileChosen(position);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!com.andreasogeirik.master_frontend.layout.Toolbar.onOptionsItemSelected(item, this)) {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
