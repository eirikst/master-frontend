package com.andreasogeirik.master_frontend.listener;

/**
 * Created by eirikstadheim on 17/02/16.
 */
public interface OnUnfriendedListener {
    void onRejectFriendSuccess(int friendshipId);
    void onRejectFriendFailure(int code);
}
