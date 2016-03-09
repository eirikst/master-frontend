package com.andreasogeirik.master_frontend.application.auth.login.interfaces;

import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by eirikstadheim on 09/02/16.
 */
public interface LoginInteractor {
    void attemptLogin(User user);
}
