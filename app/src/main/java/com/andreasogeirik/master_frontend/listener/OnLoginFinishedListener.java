package com.andreasogeirik.master_frontend.listener;
/**
 * Created by Andreas on 28.01.2016.
 */
public interface OnLoginFinishedListener {
    void onLoginError(String error);
    void onLoginSuccess(String cookie);
}
