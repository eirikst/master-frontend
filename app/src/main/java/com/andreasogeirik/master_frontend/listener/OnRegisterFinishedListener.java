package com.andreasogeirik.master_frontend.listener;

import org.json.JSONObject;

/**
 * Created by Andreas on 05.02.2016.
 */
public interface OnRegisterFinishedListener {
    void onRegisterError(String error);
    void onRegisterSuccess(JSONObject user);
}
