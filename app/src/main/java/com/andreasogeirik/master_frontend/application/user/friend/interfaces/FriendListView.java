package com.andreasogeirik.master_frontend.application.user.friend.interfaces;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.model.Friendship;

import java.util.List;

/**
 * Created by eirikstadheim on 20/02/16.
 */
public interface FriendListView {
    void initGUI(List<Friendship> friendships);
    void displayMessage(String message);
}
