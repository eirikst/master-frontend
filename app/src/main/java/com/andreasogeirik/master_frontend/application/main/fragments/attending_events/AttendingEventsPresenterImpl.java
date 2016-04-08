package com.andreasogeirik.master_frontend.application.main.fragments.attending_events;

import android.support.v4.app.Fragment;

import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces.AttendingEventView;
import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces.AttendingEventsInteractor;
import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces.AttendingEventsPresenter;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 26/02/16.
 */
public class AttendingEventsPresenterImpl extends GeneralPresenter implements AttendingEventsPresenter {
    private AttendingEventView view;
    private AttendingEventsInteractor eventInteractor;
    private User user;

    //model
    private HashSet<Event> attendingEvents = new HashSet<>();
    private HashSet<Event> attendedEvents = new HashSet<>();

    public AttendingEventsPresenterImpl(AttendingEventView view, User user) {
        super(((Fragment)view).getActivity(), NO_CHECK);
        this.view = view;


        eventInteractor = new AttendingEventsInteractorImpl(this);
        //TODO:Sjekke user cookie etc.

        if(user == null) {
            this.user = CurrentUser.getInstance().getUser();
        }
        else {
            this.user = user;
        }


        eventInteractor.findAttendingEvents(this.user);
        eventInteractor.findAttendedEvents(this.user, 0);//0 for init

        //this is the current user because it is used in the list adapter to check which of your
        // friends are participating etc.(and not necessarily the user that participates in the events)
        view.setUser(CurrentUser.getInstance().getUser());
    }

    @Override
    public void findAttendingEvents() {
        eventInteractor.findAttendingEvents(user);
    }

    @Override
    public void successAttendingEvents(Set<Event> events) {
        attendingEvents.addAll(events);//add to model

        //set view
        Set<Event> eventsJoined = new HashSet<>();
        eventsJoined.addAll(attendedEvents);
        eventsJoined.addAll(attendingEvents);

        view.setAttendingEvents(eventsJoined);
    }

    @Override
    public void errorAttendedEvents(int code) {
        view.displayMessage("Feil ved lasting av aktiviteter");
    }

    @Override
    public void findAttendedEvents() {
        eventInteractor.findAttendedEvents(user, attendedEvents.size());
    }

    @Override
    public void successAttendedEvents(Set<Event> events) {
        if(!events.isEmpty()) {
            attendedEvents.addAll(events);//add to model

            //set view, join together tables
            Set<Event> eventsJoined = new HashSet<>();
            eventsJoined.addAll(attendedEvents);
            eventsJoined.addAll(attendingEvents);

            view.setAttendingEvents(eventsJoined);
        }
        if(events.size() < Constants.NUMBER_OF_EVENTS_RETURNED) {
            view.setNoMoreEventsToLoad();
        }
    }

    @Override
    public void errorAttendingEvents(int code) {
        view.displayMessage("Feil ved lasting av aktiviteter");
    }
}