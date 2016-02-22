package com.andreasogeirik.master_frontend.listener;

/**
 * Created by eirikstadheim on 17/02/16.
 */
public interface OnAcceptRequestListener {
    void onAcceptRequestSuccess(int friendshipId);
    void onAcceptRequestFailure(int code);
}
