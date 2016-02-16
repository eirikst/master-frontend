package com.andreasogeirik.master_frontend.application.user.my_profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.friend.FriendListActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.layout.adapter.PostListAdapter;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfilePresenter;
import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfileView;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.SessionManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class MyProfileActivity extends AppCompatActivity implements MyProfileView,
        AdapterView.OnItemClickListener, MyProfileHeader.MyProfileHeaderListener, FriendProfileHeader.FriendProfileHeaderListener {
    private MyProfilePresenter presenter;

    private ListView listView;
    private Button footerBtn;

    private PostListAdapter postListAdapter;

    private List<Post> posts = new ArrayList<Post>();
    private Set<User> friends = new HashSet<>();
    private User user;


    private MyProfileHeader myProfileHeaderFragment;
    private FriendProfileHeader friendProfileHeaderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_activity);

        SessionManager.getInstance().initialize(this);

        presenter = new MyProfilePresenterImpl(this);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("user");

        initListView();

        //////////////////////////////////////////////////

        //if current user, set MyProfileHeader fragment
        if(user.equals(CurrentUser.getInstance().getUser())) {
            initMyHeader();
        }
        //else set FriendProfileHeader fragment
        else {
            initFriendHeader();
        }

        //init post and friend list after all view is set
        presenter.findPosts(user, 0);
        presenter.findFriends(user.getId());
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
        user.setFriends(friends);
        this.friends.addAll(friends);
        if(myProfileHeaderFragment != null) {
            myProfileHeaderFragment.updateFriendCount(this.friends.size());
        }
        else if(friendProfileHeaderFragment != null) {
            friendProfileHeaderFragment.updateFriendCount(this.friends.size());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    private void initListView() {
        listView = (ListView)findViewById(R.id.post_list);
        listView.setOnItemClickListener(this);

        //set adapter on listview
        postListAdapter = new PostListAdapter(MyProfileActivity.this, posts, user);
        listView.setAdapter(postListAdapter);

        initFooter();
    }

    private void initFooter() {
        View footer = getLayoutInflater().inflate(R.layout.my_profile_post_list_footer, null);
        listView.addFooterView(footer);

        //footer click listener
        footerBtn = (Button)findViewById(R.id.footer_btn);
        footerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.findPosts(user, posts.size());
            }
        });
    }

    /*
     * Call this only when own profile is visited, to set the right fragment
     */
    private void initMyHeader() {
        View header = getLayoutInflater().inflate(R.layout.my_profile_post_list_header, null);
        listView.addHeaderView(header);

        RelativeLayout fragmentContainer = (RelativeLayout)findViewById(
                R.id.left_header_fragment_container);

        myProfileHeaderFragment = MyProfileHeader.newInstance(friends.size());
        getSupportFragmentManager().beginTransaction().add(fragmentContainer.getId(), myProfileHeaderFragment, "").commit();
    }

    private void initFriendHeader() {
        View header = getLayoutInflater().inflate(R.layout.my_profile_post_list_header, null);
        listView.addHeaderView(header);

        RelativeLayout fragmentContainer = (RelativeLayout)findViewById(
                R.id.left_header_fragment_container);

        friendProfileHeaderFragment = FriendProfileHeader.newInstance(friends.size());
        getSupportFragmentManager().beginTransaction().add(fragmentContainer.getId(), friendProfileHeaderFragment, "").commit();
    }


    @Override
    public void onFriendListSelected1() {
        Intent intent = new Intent(MyProfileActivity.this, FriendListActivity.class);
        intent.putExtra("friends", (HashSet)friends);
        startActivity(intent);
    }

    @Override
    public void onFriendListSelected2() {
        Intent intent = new Intent(MyProfileActivity.this, FriendListActivity.class);
        intent.putExtra("friends", (HashSet)friends);
        startActivity(intent);
    }
}