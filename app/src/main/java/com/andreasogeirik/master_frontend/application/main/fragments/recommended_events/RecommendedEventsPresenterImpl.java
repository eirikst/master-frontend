package com.andreasogeirik.master_frontend.application.main.fragments.recommended_events;

import android.support.v4.app.Fragment;

import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.main.fragments.recommended_events.interfaces.RecommendedEventsInteractor;
import com.andreasogeirik.master_frontend.application.main.fragments.recommended_events.interfaces.RecommendedEventsPresenter;
import com.andreasogeirik.master_frontend.application.main.fragments.recommended_events.interfaces.RecommendedEventsView;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 26/02/16.
 */
public class RecommendedEventsPresenterImpl extends GeneralPresenter implements RecommendedEventsPresenter {
    private RecommendedEventsView view;
    private RecommendedEventsInteractor eventInteractor;

    //model
    private Set<Event> events = new HashSet<>();

    public RecommendedEventsPresenterImpl(RecommendedEventsView view) {
        super(((Fragment)view).getActivity(), NO_CHECK);
        this.view = view;
        eventInteractor = new RecommendedEventsInteractorImpl(this);
        //TODO:Sjekke user cookie etc.

        eventInteractor.findRecommendedEvents(0);

        view.setUser(CurrentUser.getInstance().getUser());
    }

    @Override
    public void findRecommendedEvents() {
        eventInteractor.findRecommendedEvents(events.size());
    }

    @Override
    public void successRecommendedEvents(Set<Event> events) {
        if(!events.isEmpty()) {
            this.events.addAll(events);//add to model
            view.setRecommendedEvents(this.events);
        }
        if(events.size() < Constants.NUMBER_OF_EVENTS_RETURNED) {
            view.setNoMoreEventsToLoad();
        }
    }

    @Override
    public void errorRecommendedEvents(int code) {
        view.displayMessage("Feil ved lasting av aktiviteter");
    }
}