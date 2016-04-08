package com.andreasogeirik.master_frontend.application.user.profile_others.interfaces;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by eirikstadheim on 17/02/16.
 */
public interface ProfileOthersView {
    void setupGUI(User user, int requested);

    void setProfileImage(String imageUri);

    void setRequestFriendButton();
    void setHaveBeenRequestedButtons(String firstname);
    void setIHaveRequestedView();

    void displayMessage(String message);
}
