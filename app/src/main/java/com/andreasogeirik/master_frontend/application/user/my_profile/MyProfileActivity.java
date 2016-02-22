package com.andreasogeirik.master_frontend.application.user.my_profile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.EventActivity;
import com.andreasogeirik.master_frontend.application.user.friend.FriendListActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.layout.adapter.PostListAdapter;
import com.andreasogeirik.master_frontend.listener.MyProfileHeaderListener;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfilePresenter;
import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfileView;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class MyProfileActivity extends AppCompatActivity implements MyProfileView,
        AdapterView.OnItemClickListener, MyProfileHeaderListener {
    private MyProfilePresenter presenter;

    //Bind view elements
    @Bind(R.id.post_list) ListView listView;
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.home)Button homeBtn;

    private Button footerBtn;
    private TextView nameUserText;

    private List<Post> posts = new ArrayList<Post>();
    private Set<Friendship> friends = new HashSet<>();
    private User user;

    //fragments
    private MyProfileHeader myProfileHeaderFragment;
    private FriendProfileHeader friendProfileHeaderFragment;

    private PostListAdapter postListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_activity);
        ButterKnife.bind(this);

        UserPreferencesManager.getInstance().initialize(this);
        presenter = new MyProfilePresenterImpl(this);

        if(savedInstanceState != null) {
            System.out.println("Saved instance state restored");
            user = (User)savedInstanceState.getSerializable("user");
        }
        else {
            System.out.println("New instance state from intent");
            Intent intent = getIntent();
            user = (User)intent.getSerializableExtra("user");

            //init post and friend list after all view is set
            presenter.findPosts(user, 0);
            presenter.findFriends(user.getId());
        }


        //init views
        setupToolbar();
        initListView();
        initFooter();
        //if current user, set MyProfileHeader fragment
        if(user.equals(CurrentUser.getInstance().getUser())) {
            initMyHeader();
        }
        //else set FriendProfileHeader fragment
        else {
            initFriendHeader();
        }

        //find profile image
        findProfileImage(user.getImageUri());
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // Save UI state changes to the savedInstanceState.
        // This bundle will be passed to onCreate if the process is
        // killed and restarted.
        savedInstanceState.putSerializable("user", user);
        // etc.
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO: Click on posts
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * View inits coming up
     */

    /*
     * Toolbar setup
     */
    private void setupToolbar() {
        setSupportActionBar(toolbar);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyProfileActivity.this, EventActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    /*
     * Init post list
     */
    private void initListView() {
        //listView = (ListView)findViewById(R.id.post_list);
        listView.setOnItemClickListener(this);

        //set adapter on listview
        postListAdapter = new PostListAdapter(MyProfileActivity.this, posts, user);
        listView.setAdapter(postListAdapter);
    }

    /*
     * Init post list footer
     */
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
     * Init post list header. Call this only when own profile is visited, to set the right fragment
     */
    private void initMyHeader() {
        View header = getLayoutInflater().inflate(R.layout.my_profile_post_list_header, null);
        listView.addHeaderView(header);

        nameUserText = (TextView)findViewById(R.id.name_user);

        RelativeLayout fragmentContainer = (RelativeLayout)findViewById(
                R.id.left_header_fragment_container);

        myProfileHeaderFragment = MyProfileHeader.newInstance(friends.size());
        getSupportFragmentManager().beginTransaction().add(fragmentContainer.getId(),
                myProfileHeaderFragment, "").commit();

        nameUserText.setText(user.getFirstname() + " " + user.getLastname());

        //find profile image
        findProfileImage(user.getImageUri());
    }

    /*
    * Init post list header. Call this only when friend's profile is visited, to set the right fragment
    */
    private void initFriendHeader() {
        View header = getLayoutInflater().inflate(R.layout.my_profile_post_list_header, null);
        listView.addHeaderView(header);

        nameUserText = (TextView)findViewById(R.id.name_user);

        RelativeLayout fragmentContainer = (RelativeLayout)findViewById(
                R.id.left_header_fragment_container);

        friendProfileHeaderFragment = FriendProfileHeader.newInstance(friends.size());
        getSupportFragmentManager().beginTransaction().add(fragmentContainer.getId(),
                friendProfileHeaderFragment, "").commit();

        nameUserText.setText(user.getFirstname() + " " + user.getLastname());
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Post list
     */

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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Friend list
     */

    @Override
    public void addFriends(Set<Friendship> friends) {
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
    public void onFriendListSelected() {
        Intent intent = new Intent(MyProfileActivity.this, FriendListActivity.class);
        intent.putExtra("friends", (HashSet)friends);
        startActivity(intent);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Image handling
     */

    @Override
    public void findProfileImage(String imageUri) {
        if(imageUri == null ||imageUri.isEmpty()) {
            return;
        }
        presenter.findImage(imageUri, getExternalFilesDir(Environment.DIRECTORY_PICTURES));
    }

    @Override
    public void setProfileImage(Bitmap bitmap) {
        //update adapter to display profile image on posts
        postListAdapter.setProfileImage(bitmap);
        postListAdapter.notifyDataSetChanged();

        //set header image
        ImageView headerImage = (ImageView)findViewById(R.id.my_profile_image);
        headerImage.setImageBitmap(bitmap);
    }

    @Override
    public void findProfileImageFailure() {
        //TODO:Set standard image
        System.out.println("Profile image not found for user " + user);
    }
}