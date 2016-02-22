package com.andreasogeirik.master_frontend.listener;

import org.json.JSONObject;

/**
 * Created by eirikstadheim on 17/02/16.
 */
public interface OnFriendRequestedListener {
    void onFriendRequestSuccess(JSONObject friendship);
    void onFriendRequestFailure(int code);
}
