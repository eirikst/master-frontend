package com.andreasogeirik.master_frontend.application.event.main;

import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventInteractor;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.communication.AttendEventTask;
import com.andreasogeirik.master_frontend.communication.DeleteEventTask;
import com.andreasogeirik.master_frontend.listener.OnAttendEventFinishedListener;
import com.andreasogeirik.master_frontend.listener.OnDeleteEventFinishedListener;
import com.andreasogeirik.master_frontend.model.Event;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by Andreas on 10.02.2016.
 */
public class EventInteractorImpl implements EventInteractor, OnAttendEventFinishedListener, OnDeleteEventFinishedListener {

    private EventPresenter presenter;

    public EventInteractorImpl(EventPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void attendEvent(int eventId) {
        new AttendEventTask(eventId, this, true).execute();
    }

    @Override
    public void unAttendEvent(int eventId) {
        new AttendEventTask(eventId, this, false).execute();
    }

    @Override
    public void deleteEvent(int eventId) {
        new DeleteEventTask(eventId, this).execute();
    }

    @Override
    public void onAttendSuccess(JSONObject jsonEvent) {
        try {
            Event event = new Event(jsonEvent);
            presenter.attendSuccess(event);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttendError(int error) {
        presenter.attendError(error);
    }

    @Override
    public void onDeleteEventSuccess() {
        this.presenter.deleteSuccess();
    }

    @Override
    public void onDeleteEventError(int error) {
        this.presenter.deleteError(error);
    }
}
