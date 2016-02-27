package com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface AttendingEventsInteractor {
    void findAttendingEvents();
    void findAttendedEvents(int start);
}