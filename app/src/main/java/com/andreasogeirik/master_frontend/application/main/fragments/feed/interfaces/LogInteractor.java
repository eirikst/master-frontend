package com.andreasogeirik.master_frontend.application.main.fragments.feed.interfaces;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface LogInteractor {
    void findLog(int offset);
    void updateLog(int lastLogId);
}