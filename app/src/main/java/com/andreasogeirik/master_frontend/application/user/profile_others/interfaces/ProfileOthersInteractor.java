package com.andreasogeirik.master_frontend.application.user.profile_others.interfaces;

import com.andreasogeirik.master_frontend.model.User;

import java.io.File;

/**
 * Created by eirikstadheim on 17/02/16.
 */
public interface ProfileOthersInteractor {
    void requestFriendship(User user);
    void acceptFriendship(int friendshipId);
    void rejectFriendship(int friendshipId);
    void findUser(int userId);
}
