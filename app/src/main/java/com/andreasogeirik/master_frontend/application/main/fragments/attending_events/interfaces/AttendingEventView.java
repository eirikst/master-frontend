package com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces;


import android.graphics.Bitmap;
import android.os.Bundle;

import com.andreasogeirik.master_frontend.model.Event;

import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public interface AttendingEventView {

    void saveInstanceState(Bundle instanceState);
    void displayMessage(String message);
    void setAttendingEvents(Set<Event> events);
    void setAttendingImage(String imageUri, Bitmap bitmap);
    void setNoMoreEventsToLoad();
}