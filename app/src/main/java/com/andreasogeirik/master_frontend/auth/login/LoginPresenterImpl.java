package com.andreasogeirik.master_frontend.auth.login;

import com.andreasogeirik.master_frontend.auth.login.interfaces.LoginPresenter;
import com.andreasogeirik.master_frontend.auth.login.interfaces.LoginView;
import com.andreasogeirik.master_frontend.auth.login.interfaces.OnLoginFinishedListener;
import com.andreasogeirik.master_frontend.util.Constants;

/**
 * Created by Andreas on 26.01.2016.
 */
public class LoginPresenterImpl implements LoginPresenter, OnLoginFinishedListener {

    private LoginView view;

    public LoginPresenterImpl(LoginView loginView){
        this.view = loginView;
    }

    @Override
    public void attemptLogin(String email, String password) {
        new LoginTask(email, password, Constants.BACKEND_URL, this).execute();
    }

    @Override
    public void onLoginError(String errorMessage) {
        this.view.loginFailed(errorMessage);
    }

    @Override
    public void onLoginSuccess(String cookie) {
        this.view.navigateToEventActivity(cookie);
    }
}
