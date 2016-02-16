package com.andreasogeirik.master_frontend.application.user.profile_not_friend;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.andreasogeirik.master_frontend.R;
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

    }
}
