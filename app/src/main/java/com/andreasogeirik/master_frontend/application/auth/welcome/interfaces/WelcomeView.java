package com.andreasogeirik.master_frontend.application.auth.welcome.interfaces;

/**
 * Created by Andreas on 05.02.2016.
 */
public interface WelcomeView {
    void navigateToEventView();
    void loginFailed();
    void showProgress();
    void hideProgress();
}
