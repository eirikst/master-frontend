package com.andreasogeirik.master_frontend.application.user.profile_not_friend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
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

public class ProfileOthersActivity extends AppCompatActivity implements ProfileOthersView {
    ProfileOthersPresenter presenter;
    private User user;
    private TextView tView;
    private TextView tView2;

    private Toolbar toolbar;
    private Button homeBtn;
    private TextView toolbarText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_others_activity);

        tView = (TextView)findViewById(R.id.are_we_friends_bro);
        tView2 = (TextView)findViewById(R.id.are_we_friends_bro2);

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        homeBtn = (Button)findViewById(R.id.home);
        toolbarText = (TextView)findViewById(R.id.toolbar_text);


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

        setupToolbar(user.getFirstname() + " " + user.getLastname());

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

    private void setupToolbar(String text) {
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
}
