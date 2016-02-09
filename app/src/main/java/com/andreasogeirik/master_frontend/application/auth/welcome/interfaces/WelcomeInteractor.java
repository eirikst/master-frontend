package com.andreasogeirik.master_frontend.application.auth.welcome.interfaces;

import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by Andreas on 05.02.2016.
 */
public interface WelcomeInteractor {
    void attemptLogin(User user);
}
