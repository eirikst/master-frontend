package com.andreasogeirik.master_frontend.application.user.my_profile.interfaces;

import com.andreasogeirik.master_frontend.model.User;

import java.io.File;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface ProfileInteractor {
    void findPosts(User user, int start);
    void findFriends(int userId);
    void findImage(String imageUri, File storagePath);
}
