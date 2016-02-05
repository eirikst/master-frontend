package com.andreasogeirik.master_frontend.auth.register.interfaces;

/**
 * Created by Andreas on 05.02.2016.
 */
public interface OnRegisterFinishedListener {
    void onRegisterError(String error);
    void onRegisterSuccess(String email, String password);
}
