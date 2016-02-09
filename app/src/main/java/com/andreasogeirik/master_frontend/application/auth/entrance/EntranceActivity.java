package com.andreasogeirik.master_frontend.application.auth.entrance;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.auth.login.LoginActivity;
import com.andreasogeirik.master_frontend.application.auth.register.RegisterActivity;
import com.andreasogeirik.master_frontend.util.SessionManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EntranceActivity extends Activity {

    @Bind(R.id.login) Button login;
    @Bind(R.id.register) Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entrance_activity);
        ButterKnife.bind(this);
        SessionManager.getInstance().initialize(this);
    }

    @OnClick(R.id.login)
    public void login(){
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.register)
    public void register(){
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}
