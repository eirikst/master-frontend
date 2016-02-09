package com.andreasogeirik.master_frontend.listener;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface OnFinishedLoadingPostsListener {
    void onSuccessPostsLoad(JSONArray posts);
    void onFailedPostsLoad(int code);
}
