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
    public void create(Event event) {
        JSONObject jsonEvent = new JSONObject();
        try {
            jsonEvent.put("name", event.getName());
            jsonEvent.put("location", event.getLocation());
            jsonEvent.put("description", event.getDescription());
            jsonEvent.put("timeStart", event.getTimeStart().getTime());
            jsonEvent.put("timeEnd", event.getTimeEnd().getTime());
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        new CreateEventTask(jsonEvent, this).execute();
    }

    @Override
    public void onCreateEventSuccess(JSONObject event) {
        presenter.createEventSuccess(event);
    }

    @Override
    public void onCreateEventError(int error) {
        presenter.createEventError(error);
    }
}
