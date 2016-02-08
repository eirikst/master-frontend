package com.andreasogeirik.master_frontend.auth.register.interfaces;

/**
 * Created by Andreas on 05.02.2016.
 */
public interface WelcomeView {
    void navigateToEventActivity(String cookie);
    void loginFailed(String errorMessage);
    void showProgress();
    void hideProgress();
}
