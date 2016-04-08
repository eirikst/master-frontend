package com.andreasogeirik.master_frontend.application.main.notification.interfaces;

import android.graphics.Bitmap;

import java.util.Set;

/**
 * Created by eirikstadheim on 10/03/16.
 */
public interface NotificationCenterView {
    void setNotifications(Set<Object> notifications);
    void displayMessage(String message);
}
