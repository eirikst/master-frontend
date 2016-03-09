package com.andreasogeirik.master_frontend.application.auth.register.interfaces;

import java.io.InputStream;

/**
 * Created by Andreas on 05.02.2016.
 */
public interface RegisterPresenter {
    void registerUser(String email, String password, String rePassword, String firstname, String lastname, String location);
    void sampleImage(InputStream inputStream);
    void registerSuccess();
    void registerError(int error);
    }
