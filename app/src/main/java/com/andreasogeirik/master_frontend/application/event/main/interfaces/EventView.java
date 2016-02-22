package com.andreasogeirik.master_frontend.application.event.main.interfaces;

import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public interface EventView {
    void addFriendships(Set<Friendship> friendships, Set<Friendship> requests);
    void findUserSuccess(User user);
    void findUserFailure(int code);
}