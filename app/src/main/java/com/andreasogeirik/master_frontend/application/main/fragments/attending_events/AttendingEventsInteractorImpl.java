package com.andreasogeirik.master_frontend.application.main.fragments.attending_events;

import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces.AttendingEventsInteractor;
import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces.AttendingEventsPresenter;
import com.andreasogeirik.master_frontend.communication.GetAttendingEventsTask;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public class AttendingEventsInteractorImpl implements AttendingEventsInteractor,
        GetAttendingEventsTask.OnFinishedLoadingAttendingEventsListener {
    private AttendingEventsPresenter presenter;

    public AttendingEventsInteractorImpl(AttendingEventsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void findAttendingEvents() {
        new GetAttendingEventsTask(this, CurrentUser.getInstance().getUser()).execute();
    }

    @Override
    public void onSuccessAttendingEvents(JSONArray eventsJson) {
        Set<Event> events = new HashSet<>();
        try {
            for(int i = 0; i < eventsJson.length(); i++) {
                events.add(new Event(eventsJson.getJSONObject(i)));
            }
            presenter.successAttendingEvents(events);
        }
        catch (JSONException e) {
            System.out.println("JSON error: " + e);
            presenter.errorAttendingEvents(Constants.CLIENT_ERROR);
        }
    }

    @Override
    public void onFailureAttendingEvents(int code) {
        presenter.errorAttendingEvents(code);
    }
}
