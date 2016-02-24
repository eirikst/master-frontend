package com.andreasogeirik.master_frontend.application.event.create;

import android.app.Activity;
import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventInteractor;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;
import com.andreasogeirik.master_frontend.application.general.interactors.GeneralPresenter;
import com.andreasogeirik.master_frontend.listener.OnEncodeImageFinishedListener;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.CreateEventStatusCodes;
import com.andreasogeirik.master_frontend.util.CreateEventValidationContainer;
import com.andreasogeirik.master_frontend.util.InputValidation;

import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Andreas on 10.02.2016.
 */
public class CreateEventPresenterImpl extends GeneralPresenter implements CreateEventPresenter, OnEncodeImageFinishedListener {
    CreateEventView createEventView;
    private CreateEventInteractor interactor;


    public CreateEventPresenterImpl(CreateEventView createEventView) {
        super((Activity) createEventView);
        this.createEventView = createEventView;
        this.interactor = new CreateEventInteractorImpl(this);

        //check that current user singleton is set, if not redirection
        userAvailable();
    }


    @Override
    public void createEventSuccess(JSONObject event) {
        createEventView.navigateToEventView();
    }

    @Override
    public void createEventError(int error) {
        createEventView.hideProgress();

        if (error == Constants.CLIENT_ERROR) {
            createEventView.createEventFailed("En uventet feil oppstod. Prøv igjen.");
        } else if (error == Constants.RESOURCE_ACCESS_ERROR) {
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
        CreateEventValidationContainer createEventValidationContainer = InputValidation.validateEvent(event);
        if (createEventValidationContainer.getStatusCode() != CreateEventStatusCodes.OK) {
            String error = createEventValidationContainer.getError();
            switch (createEventValidationContainer.getStatusCode()) {
                case NAME_ERROR:
                    createEventView.setNameError(error);
                    break;
                case LOCATION_ERROR:
                    createEventView.setLocationError(error);
                    break;
                case DESCRIPTION_ERROR:
                    createEventView.setDescriptionError(error);
                    break;
                case START_DATE_ERROR:
                    createEventView.setStartDateError(error);
                    break;
                case START_TIME_ERROR:
                    createEventView.setStartTimeError(error);
                    break;
                case END_DATE_ERROR:
                    createEventView.setEndDateError(error);
                    break;
                case END_TIME_ERROR:
                    createEventView.setEndDateError(error);
                    break;
            }
        } else {
            createEventView.showProgress();
            interactor.create(event, encodedImage);
        }
    }

    @Override
    public void onSuccess(Bitmap bitmap, String encodedImage) {
        createEventView.hideProgress();
        createEventView.setImage(bitmap, encodedImage);
    }

    @Override
    public void onError(ImageStatusCode statusCode) {
        createEventView.hideProgress();
        if (statusCode == ImageStatusCode.FILE_NOT_FOUND) {
            createEventView.setImageError("Finner ikke filen");
        } else if (statusCode == ImageStatusCode.NOT_AN_IMAGE) {
            createEventView.setImageError("Den valgte bildefilen støttes ikke");
        }
    }
}
