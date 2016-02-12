package com.andreasogeirik.master_frontend.application.event.create;

import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventInteractor;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.communication.CreateEventTask;
import com.andreasogeirik.master_frontend.listener.OnCreateEventFinishedListener;
import com.andreasogeirik.master_frontend.model.Event;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.Bind;

/**
 * Created by Andreas on 10.02.2016.
 */
public class CreateEventInteractorImpl implements CreateEventInteractor, OnCreateEventFinishedListener {

    private CreateEventPresenter presenter;

    public CreateEventInteractorImpl(CreateEventPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void createEvent(Event event) {
        JSONObject jsonEvent = new JSONObject();
        try {
            jsonEvent.put("email", event.getName());
            jsonEvent.put("password", event.getLocation());
            jsonEvent.put("firstname", event.getDescription());
            jsonEvent.put("lastname", event.getTimeCreated());
            jsonEvent.put("location", event.getTimeStart());
            jsonEvent.put("location", event.getTimeEnd());
            jsonEvent.put("location", event.getImageURI());
            jsonEvent.put("location", event.getAdmin());
            jsonEvent.put("location", event.getUsers());
            jsonEvent.put("location", event.getPosts());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        new CreateEventTask(jsonEvent, this).execute();
    }

    @Override
    public void onCreateEventSuccess(JSONObject event) {

    }

    @Override
    public void onCreateEventError(int error) {

    }
}
