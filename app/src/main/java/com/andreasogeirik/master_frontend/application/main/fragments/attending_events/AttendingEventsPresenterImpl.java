package com.andreasogeirik.master_frontend.application.main.fragments.attending_events;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;

import com.andreasogeirik.master_frontend.application.general.interactors.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces.AttendingEventView;
import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces.AttendingEventsInteractor;
import com.andreasogeirik.master_frontend.application.main.fragments.attending_events.interfaces.AttendingEventsPresenter;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.ImageInteractor;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 26/02/16.
 */
public class AttendingEventsPresenterImpl extends GeneralPresenter implements AttendingEventsPresenter,
        ImageInteractor.OnImageFoundListener {
    private AttendingEventView view;
    private AttendingEventsInteractor eventInteractor;

    //model
    private HashSet<Event> attendingEvents = new HashSet<>();
    private HashSet<Event> attendedEvents = new HashSet<>();

    public AttendingEventsPresenterImpl(AttendingEventView view) {
        super(((Fragment)view).getActivity());
        this.view = view;
        eventInteractor = new AttendingEventsInteractorImpl(this);
        //TODO:Sjekke user cookie etc.

        eventInteractor.findAttendingEvents();
        eventInteractor.findAttendedEvents(0);//0 for init
    }

    @Override
    public void findAttendingEvents() {
        eventInteractor.findAttendingEvents();
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
        eventInteractor.findAttendedEvents(attendedEvents.size());
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

    @Override
    public void findImage(String imageUri) {
        ImageInteractor.getInstance().findImage(imageUri, getActivity().getExternalFilesDir
                (Environment.DIRECTORY_PICTURES), this);
    }

    @Override
    public void foundImage(String imageUri, Bitmap bitmap) {
        view.setAttendingImage(imageUri, bitmap);
    }

    @Override
    public void onProgressChange(int percent) {
        // not implemented, not necessary
    }

    @Override
    public void imageNotFound(String imageUri) {
        // do nothing, default image is set
    }
}