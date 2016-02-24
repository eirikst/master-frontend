package com.andreasogeirik.master_frontend.application.user.my_profile.interfaces;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;

import java.util.List;
import java.util.Set;

/**
 * Created by eirikstadheim on 05/02/16.
 */
public interface ProfileView {
    void initView(User user, boolean me);

    void addPosts(Set<Post> posts);
    void setFriendCount(int count);
    void setProfileImage(Bitmap bitmap);

    void displayMessage(String message);
}