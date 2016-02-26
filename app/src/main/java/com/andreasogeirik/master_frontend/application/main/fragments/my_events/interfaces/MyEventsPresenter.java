package com.andreasogeirik.master_frontend.application.main.fragments.my_events.interfaces;

import android.os.Bundle;

import com.andreasogeirik.master_frontend.model.Event;

import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface MyEventsPresenter {
    void saveInstanceState(Bundle instanceState);

    void findMyEvents();
    void successMyEvents(Set<Event> events);
    void errorMyEvents(int code);

    void findImage(String imageUri);
}
