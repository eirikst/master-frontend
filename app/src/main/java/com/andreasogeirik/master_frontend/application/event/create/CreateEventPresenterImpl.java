package com.andreasogeirik.master_frontend.application.event.create;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Pair;

import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventInteractor;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;
import com.andreasogeirik.master_frontend.application.general.interactors.GeneralPresenter;
import com.andreasogeirik.master_frontend.listener.OnSampleImageFinishedListener;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;
import com.andreasogeirik.master_frontend.util.image.SampleImageTask;
import com.andreasogeirik.master_frontend.util.validation.CreateEventStatusCodes;
import com.andreasogeirik.master_frontend.util.validation.CreateEventValidationContainer;
import com.andreasogeirik.master_frontend.util.validation.InputValidation;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Andreas on 10.02.2016.
 */
public class CreateEventPresenterImpl extends GeneralPresenter implements CreateEventPresenter, OnSampleImageFinishedListener {
    CreateEventView createEventView;
    private CreateEventInteractor interactor;
    private byte[] byteImage;


    public CreateEventPresenterImpl(CreateEventView createEventView) {
        super((Activity) createEventView);
        this.createEventView = createEventView;
        this.interactor = new CreateEventInteractorImpl(this);

        //check that current user singleton is set, if not redirection
//        userAvailable();
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
    public void SampleImage(InputStream inputStream) {
        createEventView.showProgress();
        new SampleImageTask(this, inputStream, false).execute();
    }

    @Override
    public void create(String name, String location, String description, Calendar startDate, Calendar endDate, Pair<Integer, Integer> startTimePair, Pair<Integer, Integer> endTimePair) {

        CreateEventValidationContainer createEventValidationContainer = InputValidation.validateEvent(name, location, description, startDate, endDate, startTimePair, endTimePair);
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
                    createEventView.setStartDateError(error);
                    break;
                case END_DATE_ERROR:
                    createEventView.setEndDateError(error);
                    break;
                case END_TIME_ERROR:
                    createEventView.setEndDateError(error);
                    break;
            }
        } else {
            // Set start time in milliseconds
            Calendar startDateCal = new GregorianCalendar();
            startDateCal.setTimeInMillis(dateToLong(startDate, startTimePair.first, startTimePair.second));
            Event event = new Event(name, location, description, startDateCal);
            // Check if end date and time is chosen
            if (endDate != null && endTimePair != null) {
                Calendar endDateCal = new GregorianCalendar();
                endDateCal.setTimeInMillis(dateToLong(endDate, endTimePair.first, endTimePair.second));
                event.setEndDate(endDateCal);
            }
            createEventView.showProgress();
            interactor.create(event, byteImage);
        }
    }

    @Override
    public void onSampleSuccess(Bitmap bitmap, byte[] byteImage) {
        createEventView.hideProgress();
        this.byteImage = byteImage;
        createEventView.setImage(bitmap);
    }

    @Override
    public void onSampleError(ImageStatusCode statusCode) {
        createEventView.hideProgress();
        if (statusCode == ImageStatusCode.FILE_NOT_FOUND) {
            createEventView.setImageError("Finner ikke filen");
        } else if (statusCode == ImageStatusCode.NOT_AN_IMAGE) {
            createEventView.setImageError("Den valgte bildefilen støttes ikke");
        }
    }

    private long dateToLong(Calendar eventDate, int hour, int minute) {
        Calendar calendar = new GregorianCalendar(eventDate.get(Calendar.YEAR), eventDate.get(Calendar.MONTH), eventDate.get(Calendar.DAY_OF_MONTH), hour, minute);
        return calendar.getTimeInMillis();
    }
}
