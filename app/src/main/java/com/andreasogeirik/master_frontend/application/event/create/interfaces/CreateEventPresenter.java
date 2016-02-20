package com.andreasogeirik.master_frontend.application.event.create.interfaces;

import com.andreasogeirik.master_frontend.model.Event;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface CreateEventPresenter {
    void create(Event event, String encodedImage);
    void createEventSuccess(JSONObject event);
    void createEventError(int error);
    void encodeImage(InputStream inputStream);
}
