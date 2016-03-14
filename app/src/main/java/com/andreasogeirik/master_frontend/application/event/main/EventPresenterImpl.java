package com.andreasogeirik.master_frontend.application.event.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;

import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventInteractor;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.ImageInteractor;

import org.apache.commons.io.FilenameUtils;


import static com.andreasogeirik.master_frontend.util.Constants.CLIENT_ERROR;
import static com.andreasogeirik.master_frontend.util.Constants.RESOURCE_ACCESS_ERROR;


/**
 * Created by Andreas on 10.02.2016.
 */
public class EventPresenterImpl extends GeneralPresenter implements EventPresenter, ImageInteractor.OnImageFoundListener {
    private EventView eventView;
    private EventInteractor interactor;
    private Event event;


    public EventPresenterImpl(EventView eventView) {
        super((Activity) eventView);
        this.eventView = eventView;
        this.interactor = new EventInteractorImpl(this);
        //check that current user singleton is set, if not redirection
//        userAvailable();
    }

    @Override
    public void getEvent(int eventId) {
//        eventView.showProgress();
        interactor.getEvent(eventId);
    }


    @Override
    public void setEventView(Event event) {
        eventView.setEventView(event);
    }

    @Override
    public void displayError(int error) {

        switch (error) {
            case CLIENT_ERROR:
            eventView.setEventError("Fant ikke hendelse med gitt ID");
                break;
            case RESOURCE_ACCESS_ERROR:
                eventView.setEventError("Fant ikke ressurs. Prøv igjen");
                break;
        }
    }

    @Override
    public void findImage(String imageUrl) {
//        eventView.showProgress();
        String fileName = FilenameUtils.getName(imageUrl);
        ImageInteractor.getInstance().findImage(imageUrl, getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), this);
    }

    @Override
    public void foundImage(String imageUri, Bitmap bitmap) {
//        eventView.hideProgress();
        eventView.setImage(bitmap);
    }

    @Override
    public void onProgressChange(int percent) {

    }

    @Override
    public void imageNotFound(String imageUri) {
        eventView.hideProgress();
    }


}
