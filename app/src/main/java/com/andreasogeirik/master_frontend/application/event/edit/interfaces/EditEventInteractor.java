package com.andreasogeirik.master_frontend.application.event.edit.interfaces;

import com.andreasogeirik.master_frontend.model.Event;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface EditEventInteractor {
    void editEvent(Event event, byte[] byteImage);
}
