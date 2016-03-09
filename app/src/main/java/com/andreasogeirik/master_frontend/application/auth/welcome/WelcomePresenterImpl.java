package com.andreasogeirik.master_frontend.application.auth.welcome;

import com.andreasogeirik.master_frontend.application.auth.welcome.interfaces.WelcomeInteractor;
import com.andreasogeirik.master_frontend.application.auth.welcome.interfaces.WelcomePresenter;
import com.andreasogeirik.master_frontend.application.auth.welcome.interfaces.WelcomeView;
import com.andreasogeirik.master_frontend.listener.OnLoginFinishedListener;
import com.andreasogeirik.master_frontend.model.User;

import org.json.JSONObject;

/**
 * Created by Andreas on 05.02.2016.
 */
public class WelcomePresenterImpl implements WelcomePresenter, OnLoginFinishedListener {

    private WelcomeView welcomeView;
    private WelcomeInteractor interactor;

    public WelcomePresenterImpl(WelcomeView welcomeView) {
        this.welcomeView = welcomeView;

        this.interactor = new WelcomeInteractorImpl(this);
    }

    @Override
    public void attemptLogin(User user, String password) {
        welcomeView.showProgress();
        interactor.attemptLogin(user, password);
    }

    @Override
    public void loginError(int error) {
        welcomeView.loginFailed();
    }

    @Override
    public void loginSuccess() {
        welcomeView.navigateToEventView();
    }

    @Override
    public void onLoginError(int error) {

    }

    @Override
    public void onLoginSuccess(JSONObject user, String sessionId) {

    }
}
