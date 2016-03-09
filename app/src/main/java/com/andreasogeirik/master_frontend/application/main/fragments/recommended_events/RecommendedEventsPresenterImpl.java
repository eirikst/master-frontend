package com.andreasogeirik.master_frontend.application.main.fragments.recommended_events;

import android.graphics.Bitmap;
import android.os.Environment;
import android.support.v4.app.Fragment;

import com.andreasogeirik.master_frontend.application.general.interactors.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.main.fragments.recommended_events.interfaces.RecommendedEventsInteractor;
import com.andreasogeirik.master_frontend.application.main.fragments.recommended_events.interfaces.RecommendedEventsPresenter;
import com.andreasogeirik.master_frontend.application.main.fragments.recommended_events.interfaces.RecommendedEventsView;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.ImageInteractor;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 26/02/16.
 */
public class RecommendedEventsPresenterImpl extends GeneralPresenter implements RecommendedEventsPresenter,
        ImageInteractor.OnImageFoundListener {
    private RecommendedEventsView view;
    private RecommendedEventsInteractor eventInteractor;

    //model
    private Set<Event> events = new HashSet<>();

    public RecommendedEventsPresenterImpl(RecommendedEventsView view) {
        super(((Fragment)view).getActivity());
        this.view = view;
        eventInteractor = new RecommendedEventsInteractorImpl(this);
        //TODO:Sjekke user cookie etc.

        eventInteractor.findRecommendedEvents(0);
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