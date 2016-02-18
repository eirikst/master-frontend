package com.andreasogeirik.master_frontend.listener;

import org.json.JSONArray;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface OnFinishedLoadingFriendshipsListener {
    void onSuccessFriendshipsLoad(JSONArray friends);
    void onFailedFriendshipsLoad(int code);
}
