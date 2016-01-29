package com.andreasogeirik.master_frontend.event;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.auth.login.LoginActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class EventActivity extends AppCompatActivity {

    @Bind(R.id.username)
    TextView username;

    @Bind(R.id.password)
    TextView password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        ButterKnife.bind(this);

        Intent mainActivityIntent = getIntent();

        String username = mainActivityIntent.getExtras().getString("username");
        String password = mainActivityIntent.getExtras().getString("password");
        this.username.append(username);
        this.password.append(password);
    }
}
