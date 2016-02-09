package com.andreasogeirik.master_frontend.application.auth.welcome;

import com.andreasogeirik.master_frontend.application.auth.welcome.interfaces.WelcomeInteractor;
import com.andreasogeirik.master_frontend.application.auth.welcome.interfaces.WelcomePresenter;
import com.andreasogeirik.master_frontend.application.auth.welcome.interfaces.WelcomeView;
import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by Andreas on 05.02.2016.
 */
public class WelcomePresenterImpl implements WelcomePresenter {

    private WelcomeView welcomeView;
    private WelcomeInteractor interactor;

    public WelcomePresenterImpl(WelcomeView welcomeView) {
        this.welcomeView = welcomeView;

        this.interactor = new WelcomeInteractorImpl(this);
    }

    @Override
    public void attemptLogin(User user) {
        welcomeView.showProgress();

        interactor.attemptLogin(user);
    }

    @Override
    public void loginError(String error) {
        welcomeView.loginFailed(error);
    }

    @Override
    public void loginSuccess() {
        welcomeView.navigateToEventActivity();
    }
}
