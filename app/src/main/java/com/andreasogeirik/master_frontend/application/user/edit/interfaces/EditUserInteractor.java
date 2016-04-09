package com.andreasogeirik.master_frontend.application.user.edit.interfaces;

import com.andreasogeirik.master_frontend.model.User;

import java.io.InputStream;

/**
 * Created by Andreas on 07.04.2016.
 */
public interface EditUserInteractor {
    void updateUser(User user);
    void sampleImage(InputStream inputStream);
}
