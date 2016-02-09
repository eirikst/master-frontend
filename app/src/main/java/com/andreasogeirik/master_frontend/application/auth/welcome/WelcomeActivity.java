package com.andreasogeirik.master_frontend.application.auth.welcome;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.andreasogeirik.master_frontend.application.auth.entrance.EntranceActivity;
import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.auth.welcome.interfaces.WelcomePresenter;
import com.andreasogeirik.master_frontend.application.auth.welcome.interfaces.WelcomeView;
import com.andreasogeirik.master_frontend.application.event.EventActivity;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;

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

    WelcomePresenter presenter;
    ProgressBarManager progressBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome_activity);
        ButterKnife.bind(this);
        presenter = new WelcomePresenterImpl(this);
        this.progressBarManager = new ProgressBarManager(this, mWelcomeFormView, mProgressView);
    }

    @OnClick(R.id.welcome_continue)
    public void submit() {
        presenter.attemptLogin(CurrentUser.getInstance().getUser());
    }


    @Override
    public void navigateToEventView() {
        Intent i = new Intent(this, EventActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void loginFailed() {
        Intent i = new Intent(this, EntranceActivity.class);
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
