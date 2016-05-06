package com.andreasogeirik.master_frontend.application.event.create.interfaces;

import com.andreasogeirik.master_frontend.model.Event;

import java.io.InputStream;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface CreateEventInteractor {
    void create(Event event, int activityTypeId);
    void sampleImage(InputStream inputStream);
}
