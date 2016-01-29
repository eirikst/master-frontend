package com.andreasogeirik.master_frontend.auth.login;

import org.json.JSONObject;

/**
 * Created by Andreas on 28.01.2016.
 */
public interface OnLoginFinishedListener {
    void onError(String errorMessage);
    void onSuccess(JSONObject object);
}
