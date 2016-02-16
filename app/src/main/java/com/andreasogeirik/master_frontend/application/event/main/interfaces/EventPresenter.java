package com.andreasogeirik.master_frontend.application.event.main.interfaces;

import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface EventPresenter {
    void findFriends(int userId);
    void successFriendsLoad(Set<User> friends);
    void errorFriendsLoad(int code);
}
