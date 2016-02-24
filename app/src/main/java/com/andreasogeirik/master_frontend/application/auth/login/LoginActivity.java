package com.andreasogeirik.master_frontend.application.auth.login;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.auth.login.interfaces.LoginPresenter;
import com.andreasogeirik.master_frontend.application.auth.login.interfaces.LoginView;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.layout.ProgressBarManager;
import com.andreasogeirik.master_frontend.model.User;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @Bind(R.id.email)
    EditText emailView;
    @Bind(R.id.login_form)
    View loginFormView;
    @Bind(R.id.login_progress)
    View progressView;
    @Bind(R.id.password)
    EditText passwordView;
    @Bind(R.id.login_error)
    TextView errorMessage;
    @Bind(R.id.sign_in_button)
    Button signInButton;

    private LoginPresenter presenter;
    private ProgressBarManager progressBarManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
        this.presenter = new LoginPresenterImpl(this);
        this.progressBarManager = new ProgressBarManager(this, loginFormView, progressView);
    }

    @OnClick(R.id.sign_in_button)
    public void onClick() {
        User user = new User();
        user.setEmail(emailView.getText().toString());
        user.setPassword(passwordView.getText().toString());
        presenter.attemptLogin(user);
    }

    @Override
    public void navigateToEventView() {
        Intent intent = new Intent(this, MainPageActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void loginFailed(String errorMessage) {
        this.errorMessage.setText(errorMessage);
        this.errorMessage.setVisibility(View.VISIBLE);
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
        emailView.setError(error);
        View focusView = emailView;
        focusView.requestFocus();
    }

    @Override
    public void setPasswordError(String error) {
        passwordView.setError(error);
        View focusView = passwordView;
        focusView.requestFocus();
    }
}

