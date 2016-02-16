package com.andreasogeirik.master_frontend.application.user.my_profile.interfaces;

import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface MyProfileInteractor {
    void findPosts(User user, int start);
    void findFriends(int userId);
}
