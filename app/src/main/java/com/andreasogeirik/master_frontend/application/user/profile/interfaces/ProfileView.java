package com.andreasogeirik.master_frontend.application.user.profile.interfaces;

import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public interface ProfileView {
    void initUser(User user, boolean me);
    void initView();

    void addPosts(Set<Post> posts);
    void setFriendCount(int count);
    void setProfileImage(String imageUri);

    void displayMessage(String message);
    void setEventButtonText(String text);
    void displayError(String errorMessage);
}