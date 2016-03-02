package com.andreasogeirik.master_frontend.listener;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.application.event.create.ImageStatusCode;

/**
 * Created by Andreas on 20.02.2016.
 */
public interface OnSampleImageFinishedListener {
    void onSuccess(Bitmap bitmap, byte[] byteImage);
    void onError(ImageStatusCode statusCode);
}
