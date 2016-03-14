package com.andreasogeirik.master_frontend.listener;

import org.json.JSONObject;

/**
 * Created by Andreas on 10.03.2016.
 */
public interface OnAttendEventFinishedListener {
    void onAttendSuccess(JSONObject jsonEvent);
    void onAttendError(int error);
}
