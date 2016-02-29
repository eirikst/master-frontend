package com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces;

import android.os.Bundle;

import com.andreasogeirik.master_frontend.model.Event;

import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface AttendingEventsPresenter {
    void findAttendingEvents();
    void successAttendingEvents(Set<Event> events);
    void errorAttendingEvents(int code);

    void findAttendedEvents();
    void successAttendedEvents(Set<Event> events);
    void errorAttendedEvents(int code);

    void findImage(String imageUri);
}
