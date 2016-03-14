package com.andreasogeirik.master_frontend.application.main.notification.interfaces;

/**
 * Created by eirikstadheim on 10/03/16.
 */
public interface NotificationCenterInteractor {
    void acceptFriendship(int friendshipId);
    void rejectFriendship(int friendshipId);
}
