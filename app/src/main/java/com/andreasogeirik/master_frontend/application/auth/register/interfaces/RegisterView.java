package com.andreasogeirik.master_frontend.application.auth.register.interfaces;

/**
 * Created by Andreas on 29.01.2016.
 */
public interface RegisterView {
    void navigateToWelcomeActivity();
    void registrationFailed(String error);
    void showProgress();
    void hideProgress();
    void setEmailError(String error);
    void setPasswordError(String error);
    void setFirstnameError(String error);
    void setLastnameError(String error);
    void setLocationError(String error);
}
