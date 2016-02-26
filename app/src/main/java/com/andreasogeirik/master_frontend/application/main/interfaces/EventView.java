package com.andreasogeirik.master_frontend.application.main.interfaces;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;

import java.util.List;
import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public interface EventView {
    void initGUI();
    void navigateToLogin();
    void displayMessage(String message);
    void setAttendingEvents(Set<Event> events);
    void setAttendingImage(String imageUri, Bitmap bitmap);
}