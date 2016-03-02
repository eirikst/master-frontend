package com.andreasogeirik.master_frontend.application.auth.register.interfaces;

import com.andreasogeirik.master_frontend.model.User;

import java.io.InputStream;

/**
 * Created by Andreas on 05.02.2016.
 */
public interface RegisterPresenter {
    void registerUser(User user, String password);
    void sampleImage(InputStream inputStream);
    void registerSuccess();
    void registerError(int error);
    }
