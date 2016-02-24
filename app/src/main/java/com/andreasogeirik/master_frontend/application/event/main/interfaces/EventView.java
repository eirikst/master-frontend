package com.andreasogeirik.master_frontend.application.event.main.interfaces;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.model.Event;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface EventView {
    void setEventView(Event event);
    void setEventError(String errorMessage);
    void showProgress();
    void hideProgress();
    void setImage(Bitmap image);
    void imageLoadError();
}
