package com.andreasogeirik.master_frontend.auth.register;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.auth.register.interfaces.WelcomeView;
import com.andreasogeirik.master_frontend.event.EventActivity;
import com.andreasogeirik.master_frontend.util.SessionManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity implements WelcomeView {

    @Bind(R.id.welcome_continue)
    Button continueButton;

    WelcomePresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        presenter = new WelcomePresenterImpl(this);
    }

    @OnClick(R.id.welcome_continue)
    public void submit(){
        Intent intent = getIntent();
        String email = intent.getExtras().getString("email");
        String password = intent.getExtras().getString("password");
        presenter.attemptLogin(email, password);
    }


    @Override
    public void navigateToEventActivity(String cookie) {

//        showProgress(false);
        // No cookie in header
        if (cookie == null) {
            return;
        }

        SessionManager.saveCookie(this, cookie);
        Intent intent = new Intent(WelcomeActivity.this, EventActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFailed(String errorMessage) {

    }
}
