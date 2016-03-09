package com.andreasogeirik.master_frontend.listener;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;

/**
 * Created by Andreas on 20.02.2016.
 */
public interface OnSampleImageFinishedListener {
    void onSampleSuccess(Bitmap bitmap, byte[] byteImage);
    void onSampleError(ImageStatusCode statusCode);
}
