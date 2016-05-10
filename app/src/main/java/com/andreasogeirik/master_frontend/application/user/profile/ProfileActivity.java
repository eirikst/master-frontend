package com.andreasogeirik.master_frontend.application.user.profile;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
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
import android.widget.TextView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.general.ToolbarPresenterImpl;
import com.andreasogeirik.master_frontend.application.general.interfaces.ToolbarPresenter;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.application.post.CommentDialog;
import com.andreasogeirik.master_frontend.application.post.LikeDialogFragment;
import com.andreasogeirik.master_frontend.application.post.PostDialog;
import com.andreasogeirik.master_frontend.application.user.edit.EditUserActivity;
import com.andreasogeirik.master_frontend.layout.adapter.PostListAdapter;
import com.andreasogeirik.master_frontend.model.Comment;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfilePresenter;
import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfileView;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class ProfileActivity extends AppCompatActivity implements ProfileView,
        AdapterView.OnItemClickListener, PostListAdapter.PostListCallback,
        CommentDialog.Listener, PostDialog.Listener {
    private String tag = getClass().getSimpleName();

    private ProfilePresenter presenter;
    private ToolbarPresenter toolbarPresenter;
    private CommentDialog commentFragment;
    private PostDialog postDialog;

    //Bind view elements
    @Bind(R.id.post_list)
    ListView listView;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.home)
    Button homeBtn;

    private TextView noPosts;
    private View headerView;
    private Button footerBtn;
    private TextView nameUserText;
    private TextView locationUser;
    private View participatingEventView;
    private TextView participatingEventTextView;
    private ImageView imageView;
    private AppCompatButton newPostBtn;
    private ImageView headerImage;
    private View nrOfFriendsView;
    private TextView nrOfFriendsTextView;
    private View editProfileView;
    private TextView editProfileTextView;
    private View yourFriendView;

    private static int EDIT_EVENT_REQUEST = 1;

    private PostListAdapter postListAdapter;

    private boolean me = false;

    ColorStateList csl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        ButterKnife.bind(this);
        headerView = getLayoutInflater().inflate(R.layout.profile_post_list_header, null);
        headerImage = (ImageView) headerView.findViewById(R.id.my_profile_image);
        this.csl = ColorStateList.valueOf(ContextCompat.getColor(this, R.color.grey_fade));


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
        if(me) {
            MenuItem item = menu.findItem(R.id.my_profile);
            item.setVisible(false);
        }
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
        initHeader(user.getId(), user.getFirstname() + " " + user.getLastname(), user.getLocation(), user.getFriends().size(), me, user.isAdmin());
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
                new ArrayList<>(user.getPosts()), this);
        listView.setAdapter(postListAdapter);
    }

    /*
    * Init post list footer
    */
    private void initFooter() {
        View footer = getLayoutInflater().inflate(R.layout.post_list_footer, null);
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
    private void initHeader(int id, String name, String location,  int nrOfFriends, boolean myProfile, boolean admin) {
        //inflate header


        listView.addHeaderView(headerView);
        nameUserText = (TextView) headerView.findViewById(R.id.name_user);
        nameUserText.setText(name);

        locationUser = (TextView) headerView.findViewById(R.id.location_user);
        locationUser.setText(location);

        imageView = (ImageView) headerView.findViewById(R.id.my_profile_image);


        participatingEventTextView = (TextView) headerView.findViewById(R.id.participating_events_text);
        participatingEventView = headerView.findViewById(R.id.participating_events_btn);
        participatingEventTextView.setPaintFlags(this.participatingEventTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        participatingEventView.setOnClickListener(new View.OnClickListener() {
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

        noPosts = (TextView)headerView.findViewById(R.id.no_posts);


        ImageView adminSymbol = (ImageView)headerView.findViewById(R.id.admin_symbol);
        if(admin) {
            adminSymbol.setVisibility(View.VISIBLE);
        }
    }

    /*
     * Init post list header. Call this only when own profile is visited, to set the right fragment
     */
    private void initMyHeader(int nrOfFriends) {
        editProfileTextView = (TextView)headerView.findViewById(R.id.edit_profile);
        editProfileTextView.setPaintFlags(this.editProfileTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        editProfileView = headerView.findViewById(R.id.edit_panel);

        editProfileView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfileActivity.this, EditUserActivity.class);
                startActivity(i);
            }
        });

        nrOfFriendsTextView = (TextView)headerView.findViewById(R.id.my_profile_friends);
        nrOfFriendsTextView.setText(nrOfFriends + " venner");
        nrOfFriendsTextView.setPaintFlags(this.nrOfFriendsTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        nrOfFriendsView = headerView.findViewById(R.id.friend_panel);
        nrOfFriendsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.friendListSelected();
            }
        });

        yourFriendView = headerView.findViewById(R.id.your_friend_panel);
        yourFriendView.setVisibility(View.GONE);

        newPostBtn = (AppCompatButton)headerView.findViewById(R.id.new_post_user);
        newPostBtn.setSupportBackgroundTintList(csl);
        newPostBtn.setVisibility(View.VISIBLE);
        newPostBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DialogFragment.show() will take care of adding the fragment
                // in a transaction.  We also want to remove any currently showing
                // dialog, so make our own transaction and take care of that here.
                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager()
                        .beginTransaction();
                Fragment prev = getSupportFragmentManager().findFragmentByTag("postDialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                postDialog = PostDialog.newInstance();
                postDialog.show(ft, "postDialog");
            }
        });


    }

    /*
    * Init post list header. Call this only when friend's profile is visited, to set the right fragment
    */
    private void initFriendHeader(int nrOfFriends) {
        editProfileView = headerView.findViewById(R.id.edit_panel);
        editProfileView.setVisibility(View.GONE);

        nrOfFriendsTextView = (TextView)headerView.findViewById(R.id.my_profile_friends);
        nrOfFriendsTextView.setText(nrOfFriends + " venner");
        nrOfFriendsTextView.setPaintFlags(this.nrOfFriendsTextView.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        nrOfFriendsView = headerView.findViewById(R.id.friend_panel);
        nrOfFriendsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.friendListSelected();
            }
        });
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Update post list
     */
    @Override
    public void addPosts(Set<Post> posts) {
        if (posts.size() < Constants.NUMBER_OF_POSTS_RETURNED) {
            footerBtn.setText("Ingen flere poster");
            footerBtn.setClickable(false);
        }
        if (!posts.isEmpty()) {
            postListAdapter.addPosts(posts);
            postListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void noPostsToShow() {
        noPosts.setVisibility(View.VISIBLE);
    }

////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Friend list
     */

    @Override
    public void setFriendCount(int count) {
        nrOfFriendsTextView.setText(count + " venner");
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Set profile image
     */
    @Override
    public void setProfileImage(String imageUri) {
        //load image
        if(imageUri != null && !imageUri.isEmpty()) {
            Picasso.with(this)
                    .load(imageUri)
                    .error(R.drawable.default_profile_full)
                    .resize(Constants.USER_IMAGE_WIDTH, Constants.USER_IMAGE_HEIGHT)
                    .centerCrop()
                    .into(headerImage);
        }
        else {
            Picasso.with(this)
                    .load(R.drawable.default_profile_full)
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
    public void showComment(Post post) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("commentDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        commentFragment = CommentDialog.newInstance(post);
        commentFragment.show(ft, "commentDialog");
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

    @Override
    public void comment(Post post, String message) {
        presenter.comment(post, message);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * General error handling
     */
    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
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
        participatingEventTextView.setText(text);
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

    @Override
    public void addComment(Post post, Comment comment) {
        postListAdapter.addComment(post, comment);
    }

     /*
      * PostDialog
      */

    @Override
    public void commentFinished() {
        commentFragment.dismiss();
    }


    @Override
    public void commentFinishedWithError() {
        commentFragment.commentButtonEnable(true);
    }

    @Override
    public void post(String msg) {
        presenter.post(msg);
    }

    /*
     * PostDialog
    */
    @Override
    public void postFinishedSuccessfully() {
        postDialog.dismiss();
    }

    @Override
    public void postFinishedWithError() {
        postDialog.postButtonEnable(true);
    }

    @Override
    public void navigateToUser(User user) {
        presenter.navigateToUser(user);
    }

    @Override
    public void navigateToLikers(Set<User> likers) {
        // DialogFragment.show() will take care of adding the fragment
        // in a transaction.  We also want to remove any currently showing
        // dialog, so make our own transaction and take care of that here.
        android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager()
                .beginTransaction();
        Fragment prev = getSupportFragmentManager().findFragmentByTag("likerDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        // Create and show the dialog.
        DialogFragment newFragment = LikeDialogFragment.newInstance(
                new HashSet<User>(likers));
        newFragment.show(ft, "likerDialog");
    }

    @Override
    public void setMe(boolean me) {
        this.me = me;
    }
}