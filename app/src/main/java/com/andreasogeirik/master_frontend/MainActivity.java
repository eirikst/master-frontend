package com.andreasogeirik.master_frontend;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.andreasogeirik.master_frontend.auth.login.LoginActivity;
import com.andreasogeirik.master_frontend.auth.register.RegisterActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @Bind(R.id.login) Button login;
    @Bind(R.id.register) Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
