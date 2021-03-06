package com.andreasogeirik.master_frontend.application.user.profile_others;

import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.general.ToolbarPresenterImpl;
import com.andreasogeirik.master_frontend.application.general.interfaces.ToolbarPresenter;
import com.andreasogeirik.master_frontend.application.user.profile_others.interfaces.ProfileOthersPresenter;
import com.andreasogeirik.master_frontend.application.user.profile_others.interfaces.ProfileOthersView;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.squareup.picasso.Picasso;

import java.io.Serializable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileOthersActivity extends AppCompatActivity implements ProfileOthersView {
    private ProfileOthersPresenter presenter;
    private ToolbarPresenter toolbarPresenter;

    //Bind view elements
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.home)Button homeBtn;
    @Bind(R.id.others_profile_image)ImageView profileImage;
    @Bind(R.id.are_we_friends_panel)RelativeLayout areWeFriendsPanel;
    @Bind(R.id.are_we_friends_panel2)RelativeLayout areWeFriendsPanel2;
    @Bind(R.id.are_we_friends_panel3)RelativeLayout areWeFriendsPanel3;
    @Bind(R.id.are_we_friends_panel4)RelativeLayout areWeFriendsPanel4;
    @Bind(R.id.are_we_friends_bro)TextView areWeFriendsText;
    @Bind(R.id.are_we_friends_bro2)TextView areWeFriendsText2;
    @Bind(R.id.are_we_friends_bro3)TextView areWeFriendsText3;
    @Bind(R.id.are_we_friends_bro4)TextView areWeFriendsText4;

    @Bind(R.id.name_user)TextView nameUserText;
    @Bind(R.id.location_user)TextView locationText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.others_profile_activity);
        ButterKnife.bind(this);

        if(savedInstanceState != null) {
            try {
                presenter = new ProfileOthersPresenterImpl(this,
                        (User)savedInstanceState.getSerializable("user"));
            }
            catch(ClassCastException e) {
                throw new ClassCastException(e + "/nObject in savedInstanceState bundle cannot " +
                        "be cast to User in " + this.toString());
            }
        }
        else {
            Intent intent = getIntent();
            try {
                Serializable ser = intent.getSerializableExtra("user");
                if(ser != null) {
                    User user = (User)ser;
                    presenter = new ProfileOthersPresenterImpl(this, user);
                }
                else {
                    presenter = new ProfileOthersPresenterImpl(this, intent.getIntExtra("userId", -1));
                }
            }
            catch(ClassCastException e) {
                throw new ClassCastException(e + "/nObject in Intent bundle cannot " +
                        "be cast to User in " + this.toString());
            }
        }

        toolbarPresenter = new ToolbarPresenterImpl(this);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.saveInstanceState(outState);
    }

    @Override
    public void setupGUI(User user, int requested) {
        setupToolbar();

        if(requested == Friendship.I_REQUESTED) {
            setIHaveRequestedView();
        }
        else if(requested == Friendship.FRIEND_REQUESTED) {
            setHaveBeenRequestedButtons(user.getFirstname());
        }
        else {
            setRequestFriendButton();
        }

        //set name of user as header
        nameUserText.setText(user.getFirstname() + " " + user.getLastname());
        locationText.setText(user.getLocation());

        if(user.isAdmin()) {
            ImageView adminSymbol = (ImageView)findViewById(R.id.admin_symbol);
            adminSymbol.setVisibility(View.VISIBLE);
        }

        areWeFriendsPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestFriendship();
            }
        });

        areWeFriendsPanel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.rejectRequest();
            }
        });

        areWeFriendsPanel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.acceptRequest();//accept
            }
        });

        areWeFriendsPanel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.rejectRequest();//reject
            }
        });

        areWeFriendsText.setPaintFlags(this.areWeFriendsText.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        areWeFriendsText2.setPaintFlags(this.areWeFriendsText2.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        areWeFriendsText3.setPaintFlags(this.areWeFriendsText3.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        areWeFriendsText4.setPaintFlags(this.areWeFriendsText4.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
    }

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

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Init buttons methods
     */
    @Override
    public void setRequestFriendButton() {
        areWeFriendsPanel.setVisibility(View.VISIBLE);
        areWeFriendsPanel2.setVisibility(View.GONE);
        areWeFriendsPanel3.setVisibility(View.GONE);
        areWeFriendsPanel4.setVisibility(View.GONE);
    }

    @Override
    public void setHaveBeenRequestedButtons(String firstname) {
        areWeFriendsPanel.setVisibility(View.GONE);
        areWeFriendsPanel2.setVisibility(View.GONE);
        areWeFriendsPanel3.setVisibility(View.VISIBLE);
        areWeFriendsPanel4.setVisibility(View.VISIBLE);
    }

    @Override
    public void setIHaveRequestedView() {
        areWeFriendsPanel.setVisibility(View.GONE);
        areWeFriendsPanel2.setVisibility(View.VISIBLE);
        areWeFriendsPanel3.setVisibility(View.GONE);
        areWeFriendsPanel4.setVisibility(View.GONE);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void setProfileImage(String imageUri) {
        //load image
        if(imageUri != null && !imageUri.isEmpty()) {
            Picasso.with(this)
                    .load(imageUri)
                    .error(R.drawable.default_profile_full)
                    .resize(Constants.USER_IMAGE_WIDTH, Constants.USER_IMAGE_HEIGHT)
                    .centerCrop()
                    .into(profileImage);
        }
        else {
            Picasso.with(this)
                    .load(R.drawable.default_profile_full)
                    .resize(Constants.USER_IMAGE_WIDTH, Constants.USER_IMAGE_HEIGHT)
                    .centerCrop()
                    .into(profileImage);
        }
    }

    /*
    * General error handling
    */
    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!com.andreasogeirik.master_frontend.layout.Toolbar.onOptionsItemSelected(item, this)) {
            return super.onOptionsItemSelected(item);
        }
        return true;
    }
}
