package com.andreasogeirik.master_frontend.application.auth.login.interfaces;

import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by Andreas on 26.01.2016.
 */
public interface LoginPresenter {
    void attemptLogin(User user);
    void loginSuccess();
    void loginError(int error);
}
