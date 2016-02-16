package com.andreasogeirik.master_frontend.application.event.create;

import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventInteractor;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.communication.CreateEventTask;
import com.andreasogeirik.master_frontend.listener.OnCreateEventFinishedListener;
import com.andreasogeirik.master_frontend.model.Event;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
        new CreateEventTask(eventToJson(event), this).execute();
    }

    @Override
    public void onCreateEventSuccess(JSONObject event) {
        presenter.createEventSuccess(event);
    }

    @Override
    public void onCreateEventError(int error) {
        presenter.createEventError(error);
    }

    private JSONObject eventToJson(Event event) {

        long startTime = dateToLong(event.getEventDate(), event.getTimeStart().first, event.getTimeStart().second);
        JSONObject jsonEvent = new JSONObject();

        try {
            jsonEvent.put("name", event.getName());
            jsonEvent.put("location", event.getLocation());
            jsonEvent.put("description", event.getDescription());
            jsonEvent.put("timeStart", startTime);
            if (event.getTimeEnd() != null) {
                long endTime = dateToLong(event.getEventDate(), event.getTimeEnd().first, event.getTimeEnd().second);
                jsonEvent.put("timeEnd", endTime);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonEvent;
    }

    private long dateToLong(Calendar eventDate, int hour, int minute) {
        Calendar calendar = new GregorianCalendar(eventDate.get(Calendar.YEAR), eventDate.get(Calendar.MONTH), eventDate.get(Calendar.DAY_OF_MONTH), hour, minute);
        return calendar.getTimeInMillis();
    }
}
