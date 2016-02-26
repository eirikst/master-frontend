package com.andreasogeirik.master_frontend.application.main.fragments.my_events.interfaces;


import android.graphics.Bitmap;
import android.os.Bundle;

import com.andreasogeirik.master_frontend.model.Event;

import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public interface MyEventView {

    void saveInstanceState(Bundle instanceState);
    void displayMessage(String message);
    void setMyEvents(Set<Event> events);
    void setEventImage(String imageUri, Bitmap bitmap);
}