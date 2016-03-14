package com.andreasogeirik.master_frontend.application.main.interfaces;

import android.os.Bundle;

import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface EventPresenter {
    void saveInstanceState(Bundle instanceState);

    void findFriendships();
    void successFriendshipsLoad(Set<Friendship> friendships, Set<Friendship> requests);
    void errorFriendshipsLoad(int code);

    void findUser();
    void findUserSuccess(User user);
    void findUserFailure(int code);

    void accessNotificationCenter();
}
