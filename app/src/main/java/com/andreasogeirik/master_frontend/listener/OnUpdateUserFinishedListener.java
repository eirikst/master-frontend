package com.andreasogeirik.master_frontend.listener;

import org.json.JSONObject;

/**
 * Created by Andreas on 09.03.2016.
 */
public interface OnUpdateUserFinishedListener {
    void onUpdateUserSuccess(JSONObject user);
    void onUserUpdateError(int error);
}
