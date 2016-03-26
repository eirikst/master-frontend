package com.andreasogeirik.master_frontend.application.auth.register.interfaces;

/**
 * Created by Andreas on 05.02.2016.
 */
public interface RegisterPresenter {
    void registerUser(String email, String password, String rePassword, String firstname, String lastname, String location);
    void registerAndLoginSuccess();
    void registerOrLoginError(int error);
    }
