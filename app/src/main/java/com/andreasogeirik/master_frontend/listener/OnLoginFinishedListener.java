package com.andreasogeirik.master_frontend.listener;

import org.json.JSONObject;

/**
 * Created by Andreas on 28.01.2016.
 */
public interface OnLoginFinishedListener {
    void onLoginError(int error);
    void onLoginSuccess(JSONObject user, String sessionId);
}
