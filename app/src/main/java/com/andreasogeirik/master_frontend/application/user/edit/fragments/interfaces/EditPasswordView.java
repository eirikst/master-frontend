package com.andreasogeirik.master_frontend.application.user.edit.fragments.interfaces;

/**
 * Created by Andreas on 08.04.2016.
 */
public interface EditPasswordView {
    void navigateBack();
    void currentPasswordError(String errorMessage);
    void newPasswordError(String errorMessage);
    void rePasswordError(String errorMessage);
    void displayErrors(String errorMessage);
}
