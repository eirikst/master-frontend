package com.andreasogeirik.master_frontend.application.auth.welcome.interfaces;

/**
 * Created by Andreas on 05.02.2016.
 */
public interface WelcomeView {
    void navigateToEventActivity();
    void loginFailed(String errorMessage);
    void showProgress();
    void hideProgress();
}
