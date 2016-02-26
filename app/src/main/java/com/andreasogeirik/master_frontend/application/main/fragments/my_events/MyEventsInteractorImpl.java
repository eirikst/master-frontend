package com.andreasogeirik.master_frontend.application.main.fragments.my_events;

import com.andreasogeirik.master_frontend.application.main.fragments.my_events.interfaces.MyEventsInteractor;
import com.andreasogeirik.master_frontend.application.main.fragments.my_events.interfaces.MyEventsPresenter;
import com.andreasogeirik.master_frontend.communication.GetMyEventsTask;
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
public class MyEventsInteractorImpl implements MyEventsInteractor,
        GetMyEventsTask.OnFinishedLoadingMyEventsListener {
    private MyEventsPresenter presenter;

    public MyEventsInteractorImpl(MyEventsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void findMyEvents() {
        new GetMyEventsTask(this, CurrentUser.getInstance().getUser()).execute();
    }

    @Override
    public void onSuccessMyEvents(JSONArray eventsJson) {
        Set<Event> events = new HashSet<>();
        try {
            for(int i = 0; i < eventsJson.length(); i++) {
                events.add(new Event(eventsJson.getJSONObject(i)));
            }
            presenter.successMyEvents(events);
        }
        catch (JSONException e) {
            System.out.println("JSON error: " + e);
            presenter.errorMyEvents(Constants.CLIENT_ERROR);
        }
    }

    @Override
    public void onFailureMyEvents(int code) {
        presenter.errorMyEvents(code);
    }
}
