package com.andreasogeirik.master_frontend.application.event.create;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventInteractor;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;
import com.andreasogeirik.master_frontend.listener.OnEncodeImageFinishedListener;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.InputValidation;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Andreas on 10.02.2016.
 */
public class CreateEventPresenterImpl implements CreateEventPresenter, OnEncodeImageFinishedListener {
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

        if (error == Constants.CLIENT_ERROR){
            createEventView.createEventFailed("En uventet feil oppstod. Prøv igjen.");
        }
        else if(error == Constants.RESOURCE_ACCESS_ERROR) {
            createEventView.createEventFailed("Fant ikke ressurs. Prøv igjen.");
        }
    }

    @Override
    public void encodeImage(InputStream inputStream) {
        createEventView.showProgress();
        new EncodeImageTask(this, inputStream).execute();
    }

    @Override
    public void create(Event event, String encodedImage) {
        InputValidation.validateEvent(event, encodedImage, this.createEventView, this.interactor);
    }

    @Override
    public void onSuccess(Bitmap bitmap, String encodedImage) {
        createEventView.hideProgress();
        createEventView.setImage(bitmap, encodedImage);
    }

    @Override
    public void onError(ImageStatusCode statusCode) {
        createEventView.hideProgress();
        if (statusCode == ImageStatusCode.FILE_NOT_FOUND){
            createEventView.setImageError("Finner ikke filen");
        }
        else if (statusCode == ImageStatusCode.NOT_AN_IMAGE){
            createEventView.setImageError("Den valgte bildefilen støttes ikke");
        }
    }
}
