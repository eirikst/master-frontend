package com.andreasogeirik.master_frontend.application.friend;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.layout.adapter.FriendListAdapter;
import com.andreasogeirik.master_frontend.model.User;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class FriendListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener
    {
    private ListView listView;
    private FriendListAdapter listAdapter;

    private List<User> friends = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.friend_list_activity);

        Intent intent = getIntent();
        friends.addAll((HashSet<User>)intent.getSerializableExtra("friends"));

        initListView();
    }

    private void initListView() {
        listView = (ListView)findViewById(R.id.friends_list);
        listView.setOnItemClickListener(this);

        //set adapter on listview
        listAdapter = new FriendListAdapter(FriendListActivity.this, friends);
        listView.setAdapter(listAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
