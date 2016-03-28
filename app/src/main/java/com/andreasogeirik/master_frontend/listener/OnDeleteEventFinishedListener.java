package com.andreasogeirik.master_frontend.listener;

/**
 * Created by Andreas on 28.03.2016.
 */
public interface OnDeleteEventFinishedListener {
    void onDeleteEventSuccess();
    void onDeleteEventError(int error);
}
