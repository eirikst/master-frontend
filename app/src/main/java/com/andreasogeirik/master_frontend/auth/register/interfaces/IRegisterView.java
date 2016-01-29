package com.andreasogeirik.master_frontend.auth.register.interfaces;

import org.json.JSONObject;

/**
 * Created by Andreas on 29.01.2016.
 */
public interface IRegisterView {
    void navigateToEventActivity(JSONObject object);
    void registrationFailed(String errorMessage);
}
