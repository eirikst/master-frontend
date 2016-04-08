package com.andreasogeirik.master_frontend.application.user.edit.fragments.interfaces;

/**
 * Created by Andreas on 08.04.2016.
 */
public interface EditPasswordPresenter {
    void updatePassword(String currentPassword, String newPassword, String rePassword);
    void updateSuccess();
    void updateError(int error);
}
