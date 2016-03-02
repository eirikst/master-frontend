package com.andreasogeirik.master_frontend.application.auth.register.interfaces;

import android.graphics.Bitmap;

/**
 * Created by Andreas on 29.01.2016.
 */
public interface RegisterView {
    void navigateToWelcomeView();
    void registrationFailed(String error);
    void showProgress();
    void hideProgress();
    void setImage(Bitmap image);
    void setEmailError(String error);
    void setPasswordError(String error);
    void setFirstnameError(String error);
    void setLastnameError(String error);
    void setLocationError(String error);
}
