package com.andreasogeirik.master_frontend.application.event.create;

import android.text.TextUtils;

import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventInteractor;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;
import com.andreasogeirik.master_frontend.listener.OnCreateEventFinishedListener;
import com.andreasogeirik.master_frontend.model.Event;

import org.json.JSONObject;

/**
 * Created by Andreas on 10.02.2016.
 */
public class CreateEventPresenterImpl implements CreateEventPresenter {
    CreateEventView createEventView;
    private CreateEventInteractor interactor;


    public CreateEventPresenterImpl(CreateEventView createEventView) {
        this.createEventView = createEventView;
        this.interactor = new CreateEventInteractorImpl(this);
    }


    @Override
    public void createEventSuccess(JSONObject event) {
        createEventView.navigateToEventView();
    }

    @Override
    public void createEventError(int error) {
        createEventView.hideProgress();
        createEventView.createEventFailed("");
    }

    @Override
    public void create(Event event) {
        if (TextUtils.isEmpty(event.getName())){
            createEventView.setNameError("Sett et navn");
        }
        else if (TextUtils.isEmpty(event.getLocation())){
            createEventView.setLocationError("Velg et sted");
        }
        else if (TextUtils.isEmpty(event.getDescription())){
            createEventView.setLocationError("Skriv en kort beskrivelse");
        }
//        else if (event.getTimeStart()){
//            createEventView.setLocationError("Velg et starttidspunkt");
//        }
        else{
            createEventView.showProgress();
            interactor.create(event);
        }
    }
}
