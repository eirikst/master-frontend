package com.andreasogeirik.master_frontend.application.user.profile.interfaces;

import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface ProfileInteractor {
    void findUser(int userId);

    void findPosts(User user, int start);
    void findFriends(int userId);

    void findAttendingEvents(User user);

    void post(int userId, String message);
}
