package com.andreasogeirik.master_frontend.application.user.edit.interfaces;

/**
 * Created by Andreas on 07.04.2016.
 */
public interface EditUserPresenter {
    void setUserAttributes();
    void updateUser(String firstname, String lastname, String location);
    void updateSuccess();
    void updateError(int error);
}
