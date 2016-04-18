package com.andreasogeirik.master_frontend.application.main.notification.interfaces;

import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by eirikstadheim on 10/03/16.
 */
public interface NotificationCenterPresenter {
    void acceptFriendship(int friendshipId);
    void successAcceptFriendship(int friendshipId);
    void failureAcceptFriendship(int code);
    void rejectFriendship(int friendshipId);
    void successRejectFriendship(int friendshipId);
    void failureRejectFriendship(int code);
    void navigateToUser(User user);
    void checkFriendships();
}
