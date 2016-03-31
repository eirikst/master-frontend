package com.andreasogeirik.master_frontend.listener;

import org.json.JSONObject;

/**
 * Created by Andreas on 23.03.2016.
 */
public interface OnEditEventFinishedListener {
    void onEditEventSuccess(JSONObject event);
    void onEditEventError(int error);
}
