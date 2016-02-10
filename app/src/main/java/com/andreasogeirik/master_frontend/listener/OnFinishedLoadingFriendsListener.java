package com.andreasogeirik.master_frontend.listener;

import org.json.JSONArray;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface OnFinishedLoadingFriendsListener {
    void onSuccessFriendsLoad(JSONArray friends);
    void onFailedFriendsLoad(int code);
}
