package com.andreasogeirik.master_frontend.application.auth.welcome.interfaces;

import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by Andreas on 05.02.2016.
 */
public interface WelcomePresenter {
    void attemptLogin(User user);
    void loginSuccess();
    void loginError(String msg);
}
