package com.andreasogeirik.master_frontend.application.user.profile_not_friend;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.EventActivity;
import com.andreasogeirik.master_frontend.application.user.my_profile.MyProfileActivity;
import com.andreasogeirik.master_frontend.application.user.profile_not_friend.interfaces.ProfileOthersPresenter;
import com.andreasogeirik.master_frontend.application.user.profile_not_friend.interfaces.ProfileOthersView;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Iterator;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ProfileOthersActivity extends AppCompatActivity implements ProfileOthersView {
    private ProfileOthersPresenter presenter;

    //Bind view elements
    @Bind(R.id.toolbar)Toolbar toolbar;
    @Bind(R.id.home)Button homeBtn;
    @Bind(R.id.others_profile_image)ImageView profileImage;
    @Bind(R.id.are_we_friends_bro)TextView tView;
    @Bind(R.id.are_we_friends_bro2)TextView tView2;
    @Bind(R.id.name_user)TextView nameUserText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_others_activity);
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
            System.out.println("Saved instance state restored");
        }
        else {
            Intent intent = getIntent();
            try {
                presenter = new ProfileOthersPresenterImpl(this,
                        (User)intent.getSerializableExtra("user"));
            }
            catch(ClassCastException e) {
                throw new ClassCastException(e + "/nObject in Intent bundle cannot " +
                        "be cast to User in " + this.toString());
            }
            System.out.println("New instance state from intent");
        }
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
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileOthersActivity.this, EventActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Init buttons methods
     */
    @Override
    public void setRequestFriendButton() {
        tView.setText("Send en venne- forespørsel");
        tView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestFriendship();
            }
        });

        tView2.setText("");
        tView2.setClickable(false);
    }

    @Override
    public void setHaveBeenRequestedButtons(String firstname) {
        tView.setText("Godta venneforespørsel fra " + firstname);
        tView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.acceptRequest();//accept
            }
        });

        tView2.setText("Avslå venneforespørsel fra " + firstname);
        tView2.setClickable(true);
        tView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.rejectRequest();//reject
            }
        });
    }

    @Override
    public void setIHaveRequestedView() {
        tView.setText("Trekk tilbake venneforespørsel");
        tView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.rejectRequest();
            }
        });
        tView2.setText("");
        tView2.setClickable(false);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////


    @Override
    public void setProfileImage(Bitmap bitmap) {
        //set profile image
        profileImage.setImageBitmap(bitmap);
    }

    /*
    * General error handling
    */
    @Override
    public void displayMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
