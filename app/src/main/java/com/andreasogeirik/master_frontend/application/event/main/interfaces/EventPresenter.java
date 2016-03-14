package com.andreasogeirik.master_frontend.application.event.main.interfaces;

import com.andreasogeirik.master_frontend.model.Event;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface EventPresenter {
    void attendEvent();
    void unAttendEvent();
    void attendSuccess(Event event);
    void attendError(int error);
    void findImage(String imageUrl);
    void initGui();
    void updateView();
    void navigateToParticipants();
}
