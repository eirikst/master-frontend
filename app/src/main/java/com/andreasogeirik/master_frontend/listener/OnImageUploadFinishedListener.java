package com.andreasogeirik.master_frontend.listener;

import org.json.JSONObject;

/**
 * Created by Andreas on 18.02.2016.
 */
public interface OnImageUploadFinishedListener {
    void onImageUploadSuccess(JSONObject jsonImage);
    void onImageUploadError(int error);

}
