package com.andreasogeirik.master_frontend.application.main.fragments.recommended_events;

import com.andreasogeirik.master_frontend.application.main.fragments.recommended_events.interfaces.RecommendedEventsInteractor;
import com.andreasogeirik.master_frontend.application.main.fragments.recommended_events.interfaces.RecommendedEventsPresenter;
import com.andreasogeirik.master_frontend.communication.GetRecommendedEventsTask;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public class RecommendedEventsInteractorImpl implements RecommendedEventsInteractor,
        GetRecommendedEventsTask.OnFinishedLoadingRecommendedEventsListener {
    private RecommendedEventsPresenter presenter;

    public RecommendedEventsInteractorImpl(RecommendedEventsPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void findRecommendedEvents(int offset) {
        new GetRecommendedEventsTask(this, offset).execute();
    }

    @Override
    public void onSuccessRecommendedEvents(JSONArray eventsJson) {
        Set<Event> events = new HashSet<>();
        try {
            for(int i = 0; i < eventsJson.length(); i++) {
                events.add(new Event(eventsJson.getJSONObject(i)));
            }
            presenter.successRecommendedEvents(events);
        }
        catch (JSONException e) {
            System.out.println("JSON error: " + e);
            presenter.errorRecommendedEvents(Constants.CLIENT_ERROR);
        }
    }

    @Override
    public void onFailureRecommendedEvents(int code) {
        presenter.errorRecommendedEvents(code);
    }
}
