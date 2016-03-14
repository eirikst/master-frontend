package com.andreasogeirik.master_frontend.application.main.fragments.my_events;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.Fragment;

import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.main.fragments.my_events.interfaces.MyEventView;
import com.andreasogeirik.master_frontend.application.main.fragments.my_events.interfaces.MyEventsInteractor;
import com.andreasogeirik.master_frontend.application.main.fragments.my_events.interfaces.MyEventsPresenter;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.ImageInteractor;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 26/02/16.
 */
public class MyEventsPresenterImpl extends GeneralPresenter implements MyEventsPresenter,
        ImageInteractor.OnImageFoundListener {
    private MyEventView view;
    private MyEventsInteractor eventInteractor;

    //model
    private Set<Event> myEvents = new HashSet<>();
    private Set<Event> myPastEvents = new HashSet<>();

    public MyEventsPresenterImpl(MyEventView view) {
        super(((Fragment)view).getActivity());
        this.view = view;
        eventInteractor = new MyEventsInteractorImpl(this);
        //TODO:Sjekke user cookie etc.

        eventInteractor.findMyEvents();
        eventInteractor.findMyPastEvents(0);
    }

    @Override
    public void findMyEvents() {
        eventInteractor.findMyEvents();
    }

    @Override
    public void successMyEvents(Set<Event> events) {
        myEvents.addAll(events);

        Set<Event> joined = new HashSet<>();
        joined.addAll(myEvents);
        joined.addAll(myPastEvents);

        view.setMyEvents(joined);
    }

    @Override
    public void errorMyEvents(int code) {
        view.displayMessage("Feil ved lasting av aktiviteter");
    }


    @Override
    public void findMyPastEvents() {
        eventInteractor.findMyPastEvents(myPastEvents.size());
    }

    @Override
    public void successMyPastEvents(Set<Event> events) {
        if(!events.isEmpty()) {
            myPastEvents.addAll(events);

            Set<Event> joined = new HashSet<>();
            joined.addAll(myEvents);
            joined.addAll(myPastEvents);

            view.setMyEvents(joined);
        }
        if(events.size() < Constants.NUMBER_OF_EVENTS_RETURNED) {
            view.setNoMoreEventsToLoad();
        }
    }

    @Override
    public void errorMyPastEvents(int code) {
        view.displayMessage("Feil ved lasting av aktiviteter");
    }

    @Override
    public void findImage(String imageUri) {
        ImageInteractor.getInstance().findImage(imageUri, getActivity().getExternalFilesDir
                (Environment.DIRECTORY_PICTURES), this);
    }

    @Override
    public void foundImage(String imageUri, Bitmap bitmap) {
        view.setEventImage(imageUri, bitmap);
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