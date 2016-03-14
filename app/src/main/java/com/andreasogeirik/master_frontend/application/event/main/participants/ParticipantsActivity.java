package com.andreasogeirik.master_frontend.application.event.main.participants;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.EventPresenterImpl;
import com.andreasogeirik.master_frontend.application.event.main.participants.interfaces.ParticipantsPresenter;
import com.andreasogeirik.master_frontend.application.event.main.participants.interfaces.ParticipantsView;
import com.andreasogeirik.master_frontend.layout.adapter.ParticipantsListAdapter;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.User;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ParticipantsActivity extends AppCompatActivity implements ParticipantsView, AdapterView.OnItemClickListener, ParticipantsListAdapter.Listener {

    private ParticipantsListAdapter listAdapter;

    @Bind(R.id.event_participants)ListView listView;

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
        this.presenter.initGui();
    }

    @Override
    public void initGui(List<User> participants) {
        listAdapter = new ParticipantsListAdapter(this, participants, this);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void setProfileImage(String imageUri, Bitmap bitmap) {
        listAdapter.addImage(imageUri, bitmap);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void findImage(String imageUri) {
        this.presenter.findImage(imageUri);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.profileChosen(position);
    }
}
