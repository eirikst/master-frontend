package com.andreasogeirik.master_frontend.application.event.main.interfaces;

import com.andreasogeirik.master_frontend.model.Event;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface EventPresenter {
    void getEvent(int eventId);
    void setEventView(Event event);
    void displayError(int error);
}
