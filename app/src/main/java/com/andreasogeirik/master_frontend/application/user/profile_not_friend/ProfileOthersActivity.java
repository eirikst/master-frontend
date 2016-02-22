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
import com.andreasogeirik.master_frontend.util.UserPreferencesManager;

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

    private User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_others_activity);
        ButterKnife.bind(this);

        UserPreferencesManager.getInstance().initialize(this);
        presenter = new ProfileOthersPresenterImpl(this);

        if(savedInstanceState != null) {
            System.out.println("Saved instance state restored");
            user = (User)savedInstanceState.getSerializable("user");
        }
        else {
            System.out.println("New instance state from intent");
            Intent intent = getIntent();
            user = (User)intent.getSerializableExtra("user");
        }

        //find profile image
        findProfileImage(user.getImageUri());

        setupToolbar();
        //set name of user as header
        nameUserText.setText(user.getFirstname() + " " + user.getLastname());

        if(CurrentUser.getInstance().getUser().iHaveRequested(user)) {
            setIHaveRequestedButtons();
        }
        else if(CurrentUser.getInstance().getUser().iWasRequested(user)) {
            setHaveBeenRequestedButtons();
        }
        else {
            setRequestFriendButton();
        }
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
    private void setRequestFriendButton() {
        tView.setText("Send en venne- forespørsel");
        tView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestFriendship();
            }
        });

        tView2.setText("");
        tView2.setClickable(false);
    }

    private void setHaveBeenRequestedButtons() {
        tView.setText("Godta venneforespørsel fra " + user.getFirstname());
        tView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRequest();//accept
            }
        });

        tView2.setText("Avslå venneforespørsel fra " + user.getFirstname());
        tView2.setClickable(true);
        tView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectRequest();//reject
            }
        });
    }

    private void setIHaveRequestedButtons() {
        tView.setText("Trekk tilbake venneforespørsel");
        tView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectRequest();
            }
        });
        tView2.setText("");
        tView2.setClickable(false);

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Request friendship methods
     */
    @Override
    public void requestFriendship() {
        presenter.requestFriendship(user);
    }

    @Override
    public void friendRequestSuccess(Friendship friendship) {
        setIHaveRequestedButtons();
        Toast.makeText(this, "Venneforespørsel sendt", Toast.LENGTH_LONG).show();
        CurrentUser.getInstance().getUser().addRequest(friendship);
    }

    @Override
    public void friendRequestFailure(int code) {
        Toast.makeText(this, "Vennligst prøv igjen senere :)", Toast.LENGTH_LONG).show();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Accept friendship methods
     */
    @Override
    public void acceptRequest() {
        Set<Friendship> requests = CurrentUser.getInstance().getUser().getRequests();
        Iterator<Friendship> it = requests.iterator();
        while(it.hasNext()) {
            Friendship f = it.next();
            if(f.getFriend().equals(user)) {
                presenter.acceptRequest(f.getId());
                return;
            }
        }
        Toast.makeText(this, "Vennligst prøv igjen senere :)", Toast.LENGTH_LONG).show();
    }

    @Override
    public void acceptRequestSuccess(int friendshipId) {
        Toast.makeText(this, "Venneforespørsel akseptert", Toast.LENGTH_LONG).show();
        CurrentUser.getInstance().getUser().goFromRequestToFriend(friendshipId);
        Friendship friendship = CurrentUser.getInstance().getUser().findFriend(friendshipId);

        System.out.println("VENNEN MIN ER " + friendship);
        Intent intent = new Intent(this, MyProfileActivity.class);
        intent.putExtra("user", friendship.getFriend());
        startActivity(intent);
        finish();
    }

    @Override
    public void acceptRequestFailure(int code) {
        Toast.makeText(this, "Vennligst prøv igjen senere :)", Toast.LENGTH_LONG).show();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Reject friendship methods
     */
    @Override
    public void rejectRequest() {
        Set<Friendship> requests = CurrentUser.getInstance().getUser().getRequests();
        Iterator<Friendship> it = requests.iterator();
        while(it.hasNext()) {
            Friendship f = it.next();
            if(f.getFriend().equals(user)) {
                presenter.rejectRequest(f.getId());
                return;
            }
        }
        Toast.makeText(this, "Vennligst prøv igjen senere :)", Toast.LENGTH_LONG).show();
    }

    @Override
    public void rejectRequestSuccess(int friendshipId) {
        Toast.makeText(this, "Venneskap fjernet", Toast.LENGTH_LONG).show();
        setRequestFriendButton();
        CurrentUser.getInstance().getUser().removeFriendship(friendshipId);
    }

    @Override
    public void rejectRequestFailure(int code) {
        Toast.makeText(this, "Vennligst prøv igjen senere :)", Toast.LENGTH_LONG).show();
    }

////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Image handling
     */

    @Override
    public void findProfileImage(String imageUri) {
        if(imageUri == null ||imageUri.isEmpty()) {
            profileImage.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                    R.drawable.default_profile));
            return;
        }
        presenter.findImage(imageUri, getExternalFilesDir(Environment.DIRECTORY_PICTURES));
    }

    @Override
    public void setProfileImage(Bitmap bitmap) {
        //set profile image
        profileImage.setImageBitmap(bitmap);
    }

    @Override
    public void findProfileImageFailure() {
        //TODO:Set standard image
        System.out.println("Profile image not found for user " + user);
    }
}
