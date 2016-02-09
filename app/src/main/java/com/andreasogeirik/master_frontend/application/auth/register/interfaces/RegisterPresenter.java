package com.andreasogeirik.master_frontend.application.auth.register.interfaces;

import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by Andreas on 05.02.2016.
 */
public interface RegisterPresenter {
    void registerUser(User user, String password);
    void registerSuccess();
    void registerError(String msg);
    }
