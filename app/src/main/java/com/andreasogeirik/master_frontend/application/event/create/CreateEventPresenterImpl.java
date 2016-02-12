package com.andreasogeirik.master_frontend.application.event.create;

import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;
import com.andreasogeirik.master_frontend.listener.OnCreateEventFinishedListener;

import org.json.JSONObject;

/**
 * Created by Andreas on 10.02.2016.
 */
public class CreateEventPresenterImpl implements CreateEventPresenter, OnCreateEventFinishedListener {
    CreateEventView createEventView;

    public CreateEventPresenterImpl(CreateEventView createEventView) {
        this.createEventView = createEventView;
    }


    @Override
    public void onCreateEventSuccess(JSONObject event) {

    }

    @Override
    public void onCreateEventError(int error) {

    }
}
