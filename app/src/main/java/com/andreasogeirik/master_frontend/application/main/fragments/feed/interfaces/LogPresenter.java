package com.andreasogeirik.master_frontend.application.main.fragments.feed.interfaces;

import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.LogElement;

import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface LogPresenter {
    void findLog();
    void elementChosen(LogElement element);
    void activityVisible(boolean visible);
}
