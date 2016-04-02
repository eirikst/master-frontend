package com.andreasogeirik.master_frontend.application.main.fragments.recommended_events.interfaces;


import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public interface RecommendedEventsView {

    void displayMessage(String message);
    void setRecommendedEvents(Set<Event> events);
    void setEventImage(String imageUri, Bitmap bitmap);
    void updateListView();
    void setNoMoreEventsToLoad();
    void setUser(User user);
}