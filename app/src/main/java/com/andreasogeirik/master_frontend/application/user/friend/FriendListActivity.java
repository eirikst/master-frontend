package com.andreasogeirik.master_frontend.application.user.friend;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.general.ToolbarPresenterImpl;
import com.andreasogeirik.master_frontend.application.general.interfaces.ToolbarPresenter;
import com.andreasogeirik.master_frontend.application.user.friend.interfaces.FriendListPresenter;
import com.andreasogeirik.master_frontend.application.user.friend.interfaces.FriendListView;
import com.andreasogeirik.master_frontend.layout.adapter.FriendListAdapter;
import com.andreasogeirik.master_frontend.model.Friendship;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FriendListActivity extends AppCompatActivity implements FriendListView,
        AdapterView.OnItemClickListener, FriendListAdapter.Listener {
    private FriendListPresenter presenter;
    private ToolbarPresenter toolbarPresenter;
    private FriendListAdapter listAdapter;

    @Bind(R.id.friends_list)ListView listView;
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.home)Button homeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list_activity);
        ButterKnife.bind(this);

        //should get a user if not null
        if(savedInstanceState != null) {
            try {
                presenter = new FriendListPresenterImpl(this,
                        (List<Friendship>)savedInstanceState.getSerializable("friends"));
            }
            catch(ClassCastException e) {
                throw new ClassCastException(e + "/nObject in savedInstanceState bundle cannot " +
                        "be cast to User in " + this.toString());
            }
            System.out.println("Saved instance state restored");
        }
        else {
            Intent intent = getIntent();
            try {
                presenter = new FriendListPresenterImpl(this,
                        (List<Friendship>)intent.getSerializableExtra("friends"));
            }
            catch(ClassCastException e) {
                throw new ClassCastException(e + "/nObject in Intent bundle cannot " +
                        "be cast to User in " + this.toString());
            }
            System.out.println("New instance state from intent");
        }

        toolbarPresenter = new ToolbarPresenterImpl(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.saveInstanceState(outState);
    }

    @Override
    public void initGUI(List<Friendship> friendships) {
        listAdapter = new FriendListAdapter(this, friendships, this);
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener(this);
        initToolbar();
    }

    private void initToolbar() {
        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toolbarPresenter.home();
            }
        });
    }

    /*
     * Handle click on friend list
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        presenter.profileChosen(position);
    }

    @Override
    public void findImage(String imageUri) {
        presenter.findImage(imageUri);
    }

    /*
     * Sets image on adapter. Runs on ui thread...
     */
    @Override
    public void setProfileImage(String imageUri, Bitmap bitmap) {
        listAdapter.addImage(imageUri, bitmap);
    }

    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}