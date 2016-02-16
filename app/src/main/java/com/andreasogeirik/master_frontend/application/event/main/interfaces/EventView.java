package com.andreasogeirik.master_frontend.application.event.main.interfaces;

import com.andreasogeirik.master_frontend.model.FriendRequest;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public interface EventView {
    void addFriends(Set<User> friends);
    void addRequests(Set<FriendRequest> requests);
}