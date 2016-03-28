package com.andreasogeirik.master_frontend.application.event.main.interfaces;


/**
 * Created by Andreas on 10.02.2016.
 */
public interface EventInteractor {
    void attendEvent(int eventId);
    void unAttendEvent(int eventId);
    void deleteEvent(int eventId);
}
