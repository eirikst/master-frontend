package com.andreasogeirik.master_frontend.application.main.fragments.feed.interfaces;


import com.andreasogeirik.master_frontend.model.LogElement;

import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public interface LogView {
    void displayMessage(String message);
    void setLog(Set<LogElement> log);
    void noMoreLog();
    void updateListView();
}