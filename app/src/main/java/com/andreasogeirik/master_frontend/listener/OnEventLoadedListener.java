package com.andreasogeirik.master_frontend.listener;

import org.json.JSONObject;

/**
 * Created by Andreas on 24.02.2016.
 */
public interface OnEventLoadedListener {
    void onSuccess(JSONObject jsonEvent);
    void onError(int error);
}
