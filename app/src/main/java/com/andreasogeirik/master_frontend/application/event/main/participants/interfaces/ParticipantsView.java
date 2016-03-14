package com.andreasogeirik.master_frontend.application.event.main.participants.interfaces;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.model.User;

import java.util.List;

/**
 * Created by Andreas on 11.03.2016.
 */
public interface ParticipantsView {
    void initGui(List<User> users);
    void setProfileImage(String imageUri, Bitmap bitmap);
}
