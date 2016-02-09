package com.andreasogeirik.master_frontend.application.user;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;


import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.layout.adapter.PostListAdapter;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.application.user.interfaces.MyProfilePresenter;
import com.andreasogeirik.master_frontend.application.user.interfaces.MyProfileView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyProfileActivity extends AppCompatActivity implements MyProfileView, AdapterView.OnItemClickListener {
    private ListView listView;

    private MyProfilePresenter presenter;

    private List<Post> posts = new ArrayList<Post>();
    private PostListAdapter postListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MyProfilePresenterImpl(this);

        setContentView(R.layout.my_profile_activity);

        //find and set header view on listview
        View header = getLayoutInflater().inflate(R.layout.my_profile_post_list_header, null);

        ListView listView = (ListView)findViewById(R.id.post_list);
        listView.addHeaderView(header);
        listView.setOnItemClickListener(this);

        //set adapter on listview
        postListAdapter = new PostListAdapter(MyProfileActivity.this, posts);
        listView.setAdapter(postListAdapter);

        //init post list
        presenter.findPosts(0);
    }

    @Override
    public void addPosts(List<Post> posts) {
        this.posts.addAll(posts);

        postListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
}