package com.andreasogeirik.master_frontend.application.user.profile.interfaces;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.model.UserPost;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public interface ProfileView {
    void initUser(User user, boolean me);
    void initView();

    void addPosts(Set<UserPost> posts);
    void setFriendCount(int count);
    void setProfileImage(String imageUri);

    void displayMessage(String message);
    void setEventButtonText(String text);
    void updateUserError(String errorMessage);
    void refreshView();
}