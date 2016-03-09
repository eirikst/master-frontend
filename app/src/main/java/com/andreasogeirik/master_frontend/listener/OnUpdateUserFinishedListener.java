package com.andreasogeirik.master_frontend.listener;

import org.json.JSONObject;

/**
 * Created by Andreas on 09.03.2016.
 */
public interface OnUpdateUserFinishedListener {
    void onUpdateSuccess(JSONObject user);
    void onUpdateError(int error);
}
