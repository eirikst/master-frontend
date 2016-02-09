package com.andreasogeirik.master_frontend.application.auth.login.interfaces;

/**
 * Created by Andreas on 26.01.2016.
 */
public interface LoginView {
    void navigateToEventView();
    void loginFailed(String errorMessage);
    void showProgress();
    void hideProgress();
    void setEmailError(String error);
    void setPasswordError(String error);
}
