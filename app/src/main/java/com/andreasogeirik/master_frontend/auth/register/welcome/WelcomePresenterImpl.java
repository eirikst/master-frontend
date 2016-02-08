package com.andreasogeirik.master_frontend.auth.register.welcome;

import com.andreasogeirik.master_frontend.auth.login.LoginTask;
import com.andreasogeirik.master_frontend.auth.login.interfaces.OnLoginFinishedListener;
import com.andreasogeirik.master_frontend.auth.register.interfaces.WelcomePresenter;
import com.andreasogeirik.master_frontend.auth.register.interfaces.WelcomeView;
import com.andreasogeirik.master_frontend.util.Constants;

/**
 * Created by Andreas on 05.02.2016.
 */
public class WelcomePresenterImpl implements WelcomePresenter, OnLoginFinishedListener {

    private WelcomeView welcomeView;

    public WelcomePresenterImpl(WelcomeView welcomeView) {
        this.welcomeView = welcomeView;
    }

    @Override
    public void attemptLogin(String email, String password) {
        welcomeView.showProgress();
        new LoginTask(email, password, Constants.BACKEND_URL, this).execute();
    }

    @Override
    public void onLoginError(String error) {
        welcomeView.loginFailed(error);
    }

    @Override
    public void onLoginSuccess(String cookie) {
        welcomeView.navigateToEventActivity(cookie);
    }
}
