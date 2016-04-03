package com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces;

import com.andreasogeirik.master_frontend.model.User;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface AttendingEventsInteractor {
    void findAttendingEvents(User user);
    void findAttendedEvents(User user, int start);
}