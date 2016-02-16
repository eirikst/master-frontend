package com.andreasogeirik.master_frontend.application.user.profile_not_friend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.SessionManager;

public class ProfileOthersActivity extends AppCompatActivity {
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_others_activity);

        SessionManager.getInstance().initialize(this);

        Intent intent = getIntent();
        user = (User)intent.getSerializableExtra("user");

        TextView tView = (TextView)findViewById(R.id.are_we_friends_bro);
        if(CurrentUser.getInstance().getUser().iHaveRequested(user)) {
            tView.setText("Du har sendt en venneforespørsel");
        }

        else if(CurrentUser.getInstance().getUser().iWasRequested(user)) {
            tView.setText(user.getFirstname() + " ønsker å bli venn med deg. Godta?");

            tView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //accept request
                }
            });
        }
        else {
            tView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //request friend
                }
            });
        }
    }
}
