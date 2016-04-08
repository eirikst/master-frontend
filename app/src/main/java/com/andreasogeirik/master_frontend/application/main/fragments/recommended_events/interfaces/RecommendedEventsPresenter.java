package com.andreasogeirik.master_frontend.application.main.fragments.recommended_events.interfaces;

import com.andreasogeirik.master_frontend.model.Event;

import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public interface RecommendedEventsPresenter {
    void findRecommendedEvents();
    void successRecommendedEvents(Set<Event> events);
    void errorRecommendedEvents(int code);
}
