package com.andreasogeirik.master_frontend.application.user.friend.interfaces;

import android.graphics.Bitmap;

/**
 * Created by eirikstadheim on 20/02/16.
 */
public interface FriendListView {
    void setProfileImage(String imageUri, Bitmap bitmap);
    void findProfileImageFailure(String imageUri);
}//TODO: finne flere bilder da dette er en liste bror
