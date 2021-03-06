package com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces;


import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public interface AttendingEventView {

    void displayMessage(String message);
    void setAttendingEvents(Set<Event> events);
    void setNoMoreEventsToLoad();
    void updateListView();
    void setUser(User user);
}