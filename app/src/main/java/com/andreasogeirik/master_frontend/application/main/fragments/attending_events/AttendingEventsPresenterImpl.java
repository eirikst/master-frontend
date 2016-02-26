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
import com.andreasogeirik.master_frontend.util.ImageInteractor;

import java.util.Set;

/**
 * Created by eirikstadheim on 26/02/16.
 */
public class AttendingEventsPresenterImpl extends GeneralPresenter implements AttendingEventsPresenter,
        ImageInteractor.OnImageFoundListener {
    private AttendingEventView view;
    private AttendingEventsInteractor eventInteractor;

    //model
    private Set<Event> attendingEvents;

    public AttendingEventsPresenterImpl(AttendingEventView view) {
        super(((Fragment)view).getActivity());
        this.view = view;
        eventInteractor = new AttendingEventsInteractorImpl(this);
        //TODO:Sjekke user cookie etc.

        eventInteractor.findAttendingEvents();
    }

    @Override
    public void saveInstanceState(Bundle instanceState) {
        //todo:implement
    }

    @Override
    public void findAttendingEvents() {
        eventInteractor.findAttendingEvents();
    }

    @Override
    public void successAttendingEvents(Set<Event> events) {
        view.setAttendingEvents(events);
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