package com.andreasogeirik.master_frontend.application.user;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.friend.FriendListActivity;
import com.andreasogeirik.master_frontend.layout.adapter.PostListAdapter;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.application.user.interfaces.MyProfilePresenter;
import com.andreasogeirik.master_frontend.application.user.interfaces.MyProfileView;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MyProfileActivity extends AppCompatActivity implements MyProfileView,
        AdapterView.OnItemClickListener {
    private ListView listView;
    private Button footerBtn;
    private TextView friendText;
    private TextView editProfileText;

    private MyProfilePresenter presenter;

    private List<Post> posts = new ArrayList<Post>();
    private Set<User> friends = new HashSet<>();

    private PostListAdapter postListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = new MyProfilePresenterImpl(this);

        setContentView(R.layout.my_profile_activity);

        initListView();

        //init post list
        presenter.findPosts(0);
        presenter.findFriends();
    }

    @Override
    public void addPosts(List<Post> posts) {
        if(posts.size() < Constants.NUMBER_OF_POSTS_RETURNED) {
            footerBtn.setText("Ingen flere poster");
            footerBtn.setClickable(false);
        }
        if(!posts.isEmpty()){
            this.posts.addAll(posts);
            postListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void addFriends(Set<User> friends) {
        this.friends.addAll(friends);
        friendText.setText(friends.size() + " friends");
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private void initListView() {
        listView = (ListView)findViewById(R.id.post_list);
        listView.setOnItemClickListener(this);

        //set adapter on listview
        postListAdapter = new PostListAdapter(MyProfileActivity.this, posts);
        listView.setAdapter(postListAdapter);

        initHeader();
        initFooter();
    }

    private void initHeader() {
        View header = getLayoutInflater().inflate(R.layout.my_profile_post_list_header, null);
        listView.addHeaderView(header);

        friendText = (TextView)findViewById(R.id.my_profile_friends);
        editProfileText = (TextView)findViewById(R.id.my_profile_edit_profile);

        friendText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, FriendListActivity.class);

                intent.putExtra("friends", (HashSet)friends);//this yo bro
                startActivity(intent);
            }
        });

        editProfileText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start edit profile activity
            }
        });
    }

    private void initFooter() {
        View footer = getLayoutInflater().inflate(R.layout.my_profile_post_list_footer, null);
        listView.addFooterView(footer);

        //footer click listener
        footerBtn = (Button)findViewById(R.id.footer_btn);
        footerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.findPosts(posts.size());
            }
        });
    }
}