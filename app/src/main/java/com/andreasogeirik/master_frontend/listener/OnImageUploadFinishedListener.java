package com.andreasogeirik.master_frontend.listener;

/**
 * Created by Andreas on 18.02.2016.
 */
public interface OnImageUploadFinishedListener {
    void onImageUploadSuccess(String imageUrl);
    void onImageUploadError(int error);

}
