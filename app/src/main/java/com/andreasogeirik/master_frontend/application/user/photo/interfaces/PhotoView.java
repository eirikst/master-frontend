package com.andreasogeirik.master_frontend.application.user.photo.interfaces;

import android.graphics.Bitmap;

/**
 * Created by Andreas on 09.03.2016.
 */
public interface PhotoView {
    void navigateToMainView();
    void imageError(String error);
    void setImage(Bitmap bitmap);
    void sendMessage(String message);
    void updateError(String error);
}
