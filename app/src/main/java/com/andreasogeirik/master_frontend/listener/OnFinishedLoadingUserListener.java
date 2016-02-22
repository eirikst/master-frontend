package com.andreasogeirik.master_frontend.listener;

import org.json.JSONObject;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface OnFinishedLoadingUserListener {
    void onLoadingUserSuccess(JSONObject user);
    void onLoadingUserFailure(int code);
}
