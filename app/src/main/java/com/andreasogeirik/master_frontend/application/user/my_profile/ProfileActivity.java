package com.andreasogeirik.master_frontend.application.user.my_profile;

import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.EventActivity;
import com.andreasogeirik.master_frontend.application.general.interactors.ToolbarPresenterImpl;
import com.andreasogeirik.master_frontend.application.general.interactors.interfaces.ToolbarPresenter;
import com.andreasogeirik.master_frontend.layout.adapter.PostListAdapter;
import com.andreasogeirik.master_frontend.listener.MyProfileHeaderListener;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.ProfilePresenter;
import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.ProfileView;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import java.util.ArrayList;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class ProfileActivity extends AppCompatActivity implements ProfileView,
        AdapterView.OnItemClickListener, MyProfileHeaderListener {
    private ProfilePresenter presenter;
    private ToolbarPresenter toolbarPresenter;

    //Bind view elements
    @Bind(R.id.post_list) ListView listView;
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.home)Button homeBtn;

    private View headerView;
    private Button footerBtn;
    private TextView nameUserText;

    //fragments
    private MyProfileHeader myProfileHeaderFragment;
    private FriendProfileHeader friendProfileHeaderFragment;

    private PostListAdapter postListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profile_activity);
        ButterKnife.bind(this);


        //should get a user if not null
        if(savedInstanceState != null) {
            try {
                presenter = new ProfilePresenterImpl(this,
                        (User)savedInstanceState.getSerializable("user"));
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
                presenter = new ProfilePresenterImpl(this,
                        (User)intent.getSerializableExtra("user"));
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
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        presenter.saveInstanceState(savedInstanceState);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        //TODO: Click on posts
    }

    @Override
    public void initView(User user, boolean me) {
        setupToolbar();
        initListView(user);
        initFooter();
        initHeader(user.getFirstname() + " " + user.getLastname(), user.getFriends().size(), me);
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
                toolbarPresenter.home();
            }
        });
    }

    /*
    * Init post list
    */
    private void initListView(User user) {
        listView.setOnItemClickListener(this);

        //set adapter on listview
        postListAdapter = new PostListAdapter(ProfileActivity.this,
                new ArrayList<>(user.getPosts()));
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
                presenter.findPosts();
            }
        });
    }

    /*
     * Init post list header
     */
    private void initHeader(String name, int nrOfFriends, boolean myProfile) {
        //inflate header
        headerView = getLayoutInflater().inflate(R.layout.my_profile_post_list_header, null);
        listView.addHeaderView(headerView);
        nameUserText = (TextView)headerView.findViewById(R.id.name_user);
        nameUserText.setText(name);

        //if current user, set MyProfileHeader fragment
        if(myProfile) {
            initMyHeader(nrOfFriends);
        }
        //else set FriendProfileHeader fragment
        else {
            initFriendHeader(nrOfFriends);
        }
    }

    /*
     * Init post list header. Call this only when own profile is visited, to set the right fragment
     */
    private void initMyHeader(int nrOfFriends) {
        RelativeLayout fragmentContainer = (RelativeLayout)findViewById(
                R.id.left_header_fragment_container);
        myProfileHeaderFragment = MyProfileHeader.newInstance(nrOfFriends);
        getSupportFragmentManager().beginTransaction().add(fragmentContainer.getId(),
                myProfileHeaderFragment, "").commit();
    }

    /*
    * Init post list header. Call this only when friend's profile is visited, to set the right fragment
    */
    private void initFriendHeader(int nrOfFriends) {
        RelativeLayout fragmentContainer = (RelativeLayout)findViewById(
                R.id.left_header_fragment_container);
        friendProfileHeaderFragment = FriendProfileHeader.newInstance(nrOfFriends);
        getSupportFragmentManager().beginTransaction().add(fragmentContainer.getId(),
                friendProfileHeaderFragment, "").commit();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Update post list
     */
    @Override
    public void addPosts(Set<Post> posts) {
        if(posts.size() < Constants.NUMBER_OF_POSTS_RETURNED) {
            footerBtn.setText("Ingen flere poster");
            footerBtn.setClickable(false);
        }
        if(!posts.isEmpty()) {
            postListAdapter.addAll(posts);
            postListAdapter.notifyDataSetChanged();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Friend list
     */

    @Override
    public void setFriendCount(int count) {
        if(myProfileHeaderFragment != null) {
            myProfileHeaderFragment.updateFriendCount(count);
        }
        else if(friendProfileHeaderFragment != null) {
            friendProfileHeaderFragment.updateFriendCount(count);
        }
    }

    @Override
    public void onFriendListSelected() {
        presenter.friendListSelected();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Set profile image
     */
    @Override
    public void setProfileImage(Bitmap bitmap) {
        //update adapter to display profile image on posts
        postListAdapter.setProfileImage(bitmap);
        postListAdapter.notifyDataSetChanged();

        //set header image
        if(bitmap != null) {
            ImageView headerImage = (ImageView)findViewById(R.id.my_profile_image);
            headerImage.setImageBitmap(bitmap);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * General error handling
     */
    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}