package com.andreasogeirik.master_frontend.application.user.profile_not_friend.interfaces;

import com.andreasogeirik.master_frontend.model.User;

import java.io.File;

/**
 * Created by eirikstadheim on 17/02/16.
 */
public interface ProfileOthersInteractor {
    void requestFriendship(User user);
    void acceptFriendship(int friendshipId);
    void rejectFriendship(int friendshipId);

    void findImage(String imageUri, File storagePath);
}
