package com.andreasogeirik.master_frontend.auth.register.welcome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.andreasogeirik.master_frontend.MainActivity;
import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.auth.register.interfaces.WelcomeView;
import com.andreasogeirik.master_frontend.event.EventActivity;
import com.andreasogeirik.master_frontend.util.ProgressBarManager;
import com.andreasogeirik.master_frontend.util.SessionManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WelcomeActivity extends AppCompatActivity implements WelcomeView {

    @Bind(R.id.welcome_continue)
    Button continueButton;
    @Bind(R.id.welcome_form)
    View mWelcomeFormView;
    @Bind(R.id.welcome_progress)
    View mProgressView;

    WelcomePresenterImpl presenter;
    ProgressBarManager progressBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        presenter = new WelcomePresenterImpl(this);
        this.progressBarManager = new ProgressBarManager(this, mWelcomeFormView, mProgressView);
    }

    @OnClick(R.id.welcome_continue)
    public void submit() {
        Intent intent = getIntent();
        String email = intent.getExtras().getString("email");
        String password = intent.getExtras().getString("password");
        presenter.attemptLogin(email, password);
    }


    @Override
    public void navigateToEventActivity(String cookie) {
        SessionManager.saveCookie(this, cookie);
        Intent i = new Intent(this, EventActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void loginFailed(String errorMessage) {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void showProgress() {
        this.progressBarManager.showProgress(true);
    }

    @Override
    public void hideProgress() {
        this.progressBarManager.showProgress(false);
    }
}
