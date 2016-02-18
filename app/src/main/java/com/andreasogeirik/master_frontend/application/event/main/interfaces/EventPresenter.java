package com.andreasogeirik.master_frontend.application.event.main.interfaces;

import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface EventPresenter {
    void findFriendships();
    void successFriendshipsLoad(Set<Friendship> friendships, Set<Friendship> requests);
    void errorFriendshipsLoad(int code);

    void findUser();
    void findUserSuccess(User user);
    void findUserFailure(int code);
}
