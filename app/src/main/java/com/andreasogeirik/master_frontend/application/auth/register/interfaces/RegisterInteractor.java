package com.andreasogeirik.master_frontend.application.auth.register.interfaces;

import com.andreasogeirik.master_frontend.model.User;

import org.json.JSONObject;

/**
 * Created by Andreas on 05.02.2016.
 */
public interface RegisterInteractor {
    void registerUser(User user, byte[] byteImage);
}
