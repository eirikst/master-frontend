package com.andreasogeirik.master_frontend.application.event.main;

import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventInteractor;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.communication.GetEventTask;
import com.andreasogeirik.master_frontend.listener.OnEventLoadedListener;
import com.andreasogeirik.master_frontend.model.Event;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Andreas on 10.02.2016.
 */
public class EventInteractorImpl implements EventInteractor, OnEventLoadedListener {

    private EventPresenter presenter;
    private Event event;

    public EventInteractorImpl(EventPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void getEvent(int eventId) {
        new GetEventTask(eventId, this).execute();
    }

    @Override
    public void onSuccess(JSONObject jsonEvent) {
        try {
            Event event = new Event(jsonEvent);
            presenter.setEventView(event);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(int error) {
        presenter.displayError(error);
    }
}
