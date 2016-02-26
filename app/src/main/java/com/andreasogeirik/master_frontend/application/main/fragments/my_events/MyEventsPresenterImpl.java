package com.andreasogeirik.master_frontend.application.main.fragments.my_events;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;

import com.andreasogeirik.master_frontend.application.general.interactors.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.main.fragments.my_events.interfaces.MyEventView;
import com.andreasogeirik.master_frontend.application.main.fragments.my_events.interfaces.MyEventsInteractor;
import com.andreasogeirik.master_frontend.application.main.fragments.my_events.interfaces.MyEventsPresenter;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.ImageInteractor;

import java.util.Set;

/**
 * Created by eirikstadheim on 26/02/16.
 */
public class MyEventsPresenterImpl extends GeneralPresenter implements MyEventsPresenter,
        ImageInteractor.OnImageFoundListener {
    private MyEventView view;
    private MyEventsInteractor eventInteractor;

    //model
    private Set<Event> myEvents;

    public MyEventsPresenterImpl(MyEventView view) {
        super(((Fragment)view).getActivity());
        this.view = view;
        eventInteractor = new MyEventsInteractorImpl(this);
        //TODO:Sjekke user cookie etc.

        eventInteractor.findMyEvents();
    }

    @Override
    public void saveInstanceState(Bundle instanceState) {
        //todo:implement
    }

    @Override
    public void findMyEvents() {
        eventInteractor.findMyEvents();
    }

    @Override
    public void successMyEvents(Set<Event> events) {
        view.setMyEvents(events);
    }

    @Override
    public void errorMyEvents(int code) {
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