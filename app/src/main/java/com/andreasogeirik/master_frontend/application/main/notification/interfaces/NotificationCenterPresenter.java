package com.andreasogeirik.master_frontend.application.main.notification.interfaces;

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
}
