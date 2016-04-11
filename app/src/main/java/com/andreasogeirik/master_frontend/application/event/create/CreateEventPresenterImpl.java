package com.andreasogeirik.master_frontend.application.event.create;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Pair;

import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventInteractor;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;
import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.listener.OnSampleImageFinishedListener;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;
import com.andreasogeirik.master_frontend.util.image.SampleImageTask;
import com.andreasogeirik.master_frontend.util.validation.event.CreateEventStatusCodes;
import com.andreasogeirik.master_frontend.util.validation.event.CreateEventValidationContainer;
import com.andreasogeirik.master_frontend.util.validation.event.InputValidation;

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

    private Calendar startDate;
    private Calendar endDate;

    private Pair<Integer, Integer> startTimePair;
    private Pair<Integer, Integer> endTimePair;

    public CreateEventPresenterImpl(CreateEventView createEventView) {
        super((Activity) createEventView, GeneralPresenter.CHECK_USER_AVAILABLE);
        this.createEventView = createEventView;
        this.interactor = new CreateEventInteractorImpl(this);
    }


    @Override
    public void createEventSuccess(Event event) {
        createEventView.navigateToEventView(event);
    }

    @Override
    public void createEventError(int error) {
        createEventView.hideProgress();

        if (error == Constants.CLIENT_ERROR) {
            createEventView.displayError("En uventet feil oppstod. Prøv igjen.");
        } else if (error == Constants.RESOURCE_ACCESS_ERROR) {
            createEventView.displayError("Fant ikke ressurs. Prøv igjen.");
        }
        else if(error == Constants.UNAUTHORIZED) {
            checkAuth();
        }
    }

    @Override
    public void sampleImage(InputStream inputStream) {
        createEventView.showProgress();
        new SampleImageTask(this, inputStream, false).execute();
    }

    @Override
    public void deleteEndTimes() {
        this.endDate = null;
        this.endTimePair = null;
    }

    @Override
    public void setDate(Boolean isStartDate) {
        Bundle bundle = new Bundle();
        Calendar date;
        if (isStartDate) {
            date = this.startDate;
            bundle.putString("date", "start");
        } else {
            date = this.endDate;
            bundle.putString("date", "end");
        }
        if (date != null) {
            bundle.putInt("day", date.get(Calendar.DAY_OF_MONTH));
            bundle.putInt("month", date.get(Calendar.MONTH));
            bundle.putInt("year", date.get(Calendar.YEAR));
        }
        this.createEventView.onDateSet(bundle);
    }

    @Override
    public void setTime(Boolean isStarTime) {
        Bundle bundle = new Bundle();
        if (isStarTime) {
            bundle.putString("time", "start");
            if (this.startTimePair != null) {
                bundle.putInt("hour", this.startTimePair.first);
                bundle.putInt("minute", this.startTimePair.second);
            }
        } else {
            bundle.putString("time", "end");
            if (this.endTimePair != null) {
                bundle.putInt("hour", this.endTimePair.first);
                bundle.putInt("minute", this.endTimePair.second);
            }
        }
        this.createEventView.onTimeSet(bundle);
    }

    @Override
    public void updateDateModel(Calendar eventDate, Boolean isStartDate) {
        int day = eventDate.get(Calendar.DAY_OF_MONTH);
        int month = eventDate.get(Calendar.MONTH) + 1;
        int year = eventDate.get(Calendar.YEAR);

        if (isStartDate) {
            this.startDate = eventDate;
            this.createEventView.updateStartDateView(day, month, year);
        } else {
            this.endDate = eventDate;
            this.createEventView.updateEndDateView(day, month, year);
        }
    }

    @Override
    public void updateTimeModel(Pair<Integer, Integer> hourMinutePair, Boolean isStartTime) {
        int hour = hourMinutePair.first;
        int minute = hourMinutePair.second;
        if (isStartTime) {
            this.startTimePair = hourMinutePair;
            this.createEventView.updateStartTimeView(hour, minute);
        } else {
            this.endTimePair = hourMinutePair;
            this.createEventView.updateEndTimeView(hour, minute);
        }
    }

    @Override
    public void create(String name, String location, String description, int difficulty) {

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
            Event event = new Event(name, location, description, startDateCal, difficulty + 1);
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
            createEventView.displayError("Finner ikke bildefilen");
        } else if (statusCode == ImageStatusCode.NOT_AN_IMAGE) {
            createEventView.displayError("Den valgte bildefilen støttes ikke");
        }
    }

    private long dateToLong(Calendar eventDate, int hour, int minute) {
        Calendar calendar = new GregorianCalendar(eventDate.get(Calendar.YEAR), eventDate.get(Calendar.MONTH), eventDate.get(Calendar.DAY_OF_MONTH), hour, minute);
        return calendar.getTimeInMillis();
    }
}
