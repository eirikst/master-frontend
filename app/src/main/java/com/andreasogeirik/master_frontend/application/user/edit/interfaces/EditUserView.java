package com.andreasogeirik.master_frontend.application.user.edit.interfaces;

import android.graphics.Bitmap;

/**
 * Created by Andreas on 07.04.2016.
 */
public interface EditUserView {
    void showPasswordCenter();
    void updateImage(Bitmap image);
    void setUserAttributes(String firstname, String lastname, String location, String imageUri);
    void naviagteToProfileView(int userId);
    void displayUpdateError(String message);
    void firstnameError(String message);
    void lastnameError(String message);
    void locationError(String message);
    void imageError(String message);
    void showProgress();
    void hideProgress();
}
