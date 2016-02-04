package com.andreasogeirik.master_frontend.auth.login.interfaces;

/**
 * Created by Andreas on 26.01.2016.
 */
public interface ILoginView {
    void navigateToEventActivity(String cookie);
    void loginFailed(String errorMessage);
}
