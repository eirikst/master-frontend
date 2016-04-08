package com.andreasogeirik.master_frontend.application.user.edit.interfaces;

/**
 * Created by Andreas on 07.04.2016.
 */
public interface EditUserView {
    void showPasswordCenter();
    void setUserAttributes(String firstname, String lastname, String location);
    void naviagetToProfileView();
    void displayUpdateError(String message);
    void firstnameError(String message);
    void lastnameError(String message);
    void locationError(String message);
}
