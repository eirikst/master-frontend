package com.andreasogeirik.master_frontend.listener;

/**
 * Created by Andreas on 08.04.2016.
 */
public interface OnUpdatePasswordFinishedListener {
    void onUpdateSuccess();
    void onUpdateError(int error);
}
