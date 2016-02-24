package com.andreasogeirik.master_frontend.application.user.profile_not_friend.interfaces;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by eirikstadheim on 17/02/16.
 */
public interface ProfileOthersView {
    void setupGUI(User user, int requested);

    void setProfileImage(Bitmap bitmap);

    void setRequestFriendButton();
    void setHaveBeenRequestedButtons(String firstname);
    void setIHaveRequestedView();

    void displayMessage(String message);
}
