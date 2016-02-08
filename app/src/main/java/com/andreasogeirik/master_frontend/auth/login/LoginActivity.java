package com.andreasogeirik.master_frontend.auth.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.auth.login.interfaces.LoginView;
import com.andreasogeirik.master_frontend.event.EventActivity;
import com.andreasogeirik.master_frontend.util.ProgressBarManager;
import com.andreasogeirik.master_frontend.util.SessionManager;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @Bind(R.id.email)
    EditText mEmailView;
    @Bind(R.id.login_form)
    View mLoginFormView;
    @Bind(R.id.login_progress)
    View mProgressView;
    @Bind(R.id.password)
    EditText mPasswordView;
    @Bind(R.id.error)
    TextView mErrorMessage;
    @Bind(R.id.sign_in_button)
    Button mSign_in_button;

    LoginPresenterImpl presenter;
    ProgressBarManager progressBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        this.presenter = new LoginPresenterImpl(this);
        this.progressBarManager = new ProgressBarManager(this, mLoginFormView, mProgressView);
    }

    @OnClick(R.id.sign_in_button)
    public void onClick() {
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();
        presenter.attemptLogin(email, password);
    }

    @Override
    public void navigateToEventActivity(String cookie) {
        SessionManager.saveCookie(this, cookie);
        Intent intent = new Intent(LoginActivity.this, EventActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public void loginFailed(String errorMessage) {
        mErrorMessage.setText(errorMessage);
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void showProgress() {
        this.progressBarManager.showProgress(true);
    }

    @Override
    public void hideProgress() {
        this.progressBarManager.showProgress(false);
    }

    @Override
    public void setEmailError(String error) {
        mEmailView.setError(error);
        View focusView = mEmailView;
        focusView.requestFocus();
    }

    @Override
    public void setPasswordError(String error) {
        mPasswordView.setError(error);
        View focusView = mPasswordView;
        focusView.requestFocus();
    }
}

