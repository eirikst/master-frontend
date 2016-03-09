package com.andreasogeirik.master_frontend.application.auth.register.interfaces;

import android.net.Uri;

import java.io.InputStream;

/**
 * Created by Andreas on 05.02.2016.
 */
public interface RegisterPresenter {
    void registerUser(String email, String password, String rePassword, String firstname, String lastname, String location, byte[] byteImage);
    void sampleImage(InputStream inputStream);
    void registerSuccess();
    void registerError(int error);
    }
