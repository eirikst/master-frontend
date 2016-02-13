package com.andreasogeirik.master_frontend.application.event.create.interfaces;

import com.andreasogeirik.master_frontend.model.Event;

import org.json.JSONObject;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface CreateEventPresenter {
    void create(Event event);
    void createEventSuccess(JSONObject event);
    void createEventError(int error);
}
