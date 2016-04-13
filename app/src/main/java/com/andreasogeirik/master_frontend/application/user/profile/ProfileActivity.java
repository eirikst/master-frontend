package com.andreasogeirik.master_frontend.application.user.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.general.ToolbarPresenterImpl;
import com.andreasogeirik.master_frontend.application.general.interfaces.ToolbarPresenter;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.application.user.profile.fragments.FriendProfileHeader;
import com.andreasogeirik.master_frontend.application.user.profile.fragments.MyProfileHeader;
import com.andreasogeirik.master_frontend.layout.adapter.PostListAdapter;
import com.andreasogeirik.master_frontend.layout.transformation.CircleTransform;
import com.andreasogeirik.master_frontend.listener.MyProfileHeaderListener;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfilePresenter;
import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfileView;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class ProfileActivity extends AppCompatActivity implements ProfileView,
        AdapterView.OnItemClickListener, MyProfileHeaderListener, PostListAdapter.PostListCallback {
    private String tag = getClass().getSimpleName();

    private ProfilePresenter presenter;
    private ToolbarPresenter toolbarPresenter;

    //Bind view elements
    @Bind(R.id.post_list)
    ListView listView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.home)
    Button homeBtn;

    private View headerView;
    private Button footerBtn;
    private TextView nameUserText;
    private Button eventButton;
    private ImageView imageView;

    private static int EDIT_EVENT_REQUEST = 1;

    //fragments
    private MyProfileHeader myProfileHeaderFragment;
    private FriendProfileHeader friendProfileHeaderFragment;

    private PostListAdapter postListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        ButterKnife.bind(this);


            Intent intent = getIntent();
            try {
                presenter = new ProfilePresenterImpl(this, intent.getIntExtra("user", -1));
            } catch (ClassCastException e) {
                throw new ClassCastException(e + "/nObject in Intent bundle cannot " +
                        "be cast to User in " + this.toString());
            }
            Log.i(tag, "New instance state from intent");

        toolbarPresenter = new ToolbarPresenterImpl(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        MenuItem item = menu.findItem(R.id.my_profile);
        item.setVisible(false);
        return true;
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
    public void initUser(User user, boolean me) {
        initListView(user);
        initHeader(user.getFirstname() + " " + user.getLastname(), user.getFriends().size(), me);
    }

    @Override
    public void initView() {
        setupToolbar();
        initFooter();
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
        getSupportActionBar().setDisplayShowTitleEnabled(false);

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
                new ArrayList<>(user.getPosts()), user, this);
        listView.setAdapter(postListAdapter);
    }

    /*
    * Init post list footer
    */
    private void initFooter() {
        View footer = getLayoutInflater().inflate(R.layout.profile_post_list_footer, null);
        listView.addFooterView(footer);

        //footer click listener
        footerBtn = (Button) findViewById(R.id.footer_btn);
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
        headerView = getLayoutInflater().inflate(R.layout.profile_post_list_header, null);
        listView.addHeaderView(headerView);
        nameUserText = (TextView) headerView.findViewById(R.id.name_user);
        nameUserText.setText(name);
        imageView = (ImageView) headerView.findViewById(R.id.my_profile_image);


        eventButton = (Button) headerView.findViewById(R.id.participating_events_btn);

        eventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.accessEvents();
            }
        });

        //if current user, set MyProfileHeader fragment
        if (myProfile) {
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
        RelativeLayout fragmentContainer = (RelativeLayout) findViewById(
                R.id.left_header_fragment_container);
        myProfileHeaderFragment = MyProfileHeader.newInstance(nrOfFriends);
        getSupportFragmentManager().beginTransaction().add(fragmentContainer.getId(),
                myProfileHeaderFragment, "").commit();
    }

    /*
    * Init post list header. Call this only when friend's profile is visited, to set the right fragment
    */
    private void initFriendHeader(int nrOfFriends) {
        RelativeLayout fragmentContainer = (RelativeLayout) findViewById(
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
        for(Post post: posts) {
            System.out.println(post);
        }

        if (posts.size() < Constants.NUMBER_OF_POSTS_RETURNED) {
            footerBtn.setText("Ingen flere poster");
            footerBtn.setClickable(false);
        }
        if (!posts.isEmpty()) {
            postListAdapter.addPosts(posts);
            postListAdapter.notifyDataSetChanged();
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Friend list
     */

    @Override
    public void setFriendCount(int count) {
        if (myProfileHeaderFragment != null) {
            myProfileHeaderFragment.updateFriendCount(count);
        } else if (friendProfileHeaderFragment != null) {
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
    public void setProfileImage(String imageUri) {
        ImageView headerImage = (ImageView)findViewById(R.id.my_profile_image);

        //load image
        if(imageUri != null && !imageUri.isEmpty()) {
            Picasso.with(this)
                    .load(imageUri)
                    .error(R.drawable.default_profile)
                    .resize(Constants.USER_IMAGE_WIDTH, Constants.USER_IMAGE_HEIGHT)
                    .centerCrop()
                    .into(headerImage);
        }
        else {
            Picasso.with(this)
                    .load(R.drawable.default_profile)
                    .resize(Constants.USER_IMAGE_WIDTH, Constants.USER_IMAGE_HEIGHT)
                    .centerCrop()
                    .into(headerImage);
        }
    }

    /*
     * Post list handling
     */

    @Override
    public void likeComment(int commentId) {
        presenter.likeComment(commentId);
    }

    @Override
    public void likePost(int postId) {
        presenter.likePost(postId);
    }

    @Override
    public void unlikeComment(int commentId) {
        presenter.unlikeComment(commentId);
    }

    @Override
    public void unlikePost(int postId) {
        presenter.unlikePost(postId);
    }

    @Override
    public void updatePostLike(int id, boolean like) {
        postListAdapter.updatePost(id, like);
    }

    @Override
    public void updateCommentLike(int id, boolean like) {
        postListAdapter.updateComment(id, like);
    }

    @Override
    public void displayLoadPostsButton(boolean display) {
        if(display) {
            footerBtn.setVisibility(View.VISIBLE);
        }
        else {
            footerBtn.setVisibility(View.GONE);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!com.andreasogeirik.master_frontend.layout.Toolbar.onOptionsItemSelected(item, this)) {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void setEventButtonText(String text) {
        eventButton.setText(text);
    }

    @Override
    public void displayError(String errorMessage) {
        Toast.makeText(ProfileActivity.this, errorMessage, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {

        Intent i = getIntent();
        int requestCode = i.getIntExtra("requestCode", 0);
        if (requestCode == EDIT_EVENT_REQUEST) {
            Intent intent = new Intent(this, MainPageActivity.class);
            startActivity(intent);
            finish();
        } else {
            super.onBackPressed();
        }
    }
}