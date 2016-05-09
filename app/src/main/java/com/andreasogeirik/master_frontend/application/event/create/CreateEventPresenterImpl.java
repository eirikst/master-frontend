package com.andreasogeirik.master_frontend.application.event.create;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Pair;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventInteractor;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;
import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;
import com.andreasogeirik.master_frontend.util.validation.event.CreateEventStatusCodes;
import com.andreasogeirik.master_frontend.util.validation.event.CreateEventValidationContainer;
import com.andreasogeirik.master_frontend.util.validation.event.InputValidation;

import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Andreas on 10.02.2016.
 */
public class CreateEventPresenterImpl extends GeneralPresenter implements CreateEventPresenter{
    CreateEventView createEventView;
    private CreateEventInteractor interactor;

    private Calendar startDate;
    private Calendar endDate;

    private Pair<Integer, Integer> startTimePair;
    private Pair<Integer, Integer> endTimePair;

    private int activityTypeId = 0;

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
        switch (error) {
            case Constants.RESOURCE_ACCESS_ERROR:
                this.createEventView.displayError(getActivity().getResources().getString(R.string.resource_access_error));
                break;
            case Constants.UNAUTHORIZED:
                checkAuth();
                break;
            // Dette skal ikke skje..
            case Constants.CLIENT_ERROR:
                this.createEventView.displayError(getActivity().getResources().getString(R.string.some_error));
                break;
            case Constants.SOME_ERROR:
                this.createEventView.displayError(getActivity().getResources().getString(R.string.some_error));
                break;
        }
    }

    @Override
    public void sampleImage(InputStream inputStream) {
        this.interactor.sampleImage(inputStream);
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
            if (this.startDate != null && date == null){
                bundle.putInt("day", this.startDate.get(Calendar.DAY_OF_MONTH));
                bundle.putInt("month", this.startDate.get(Calendar.MONTH));
                bundle.putInt("year", this.startDate.get(Calendar.YEAR));
            }
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
    public void updateActivityTypeModel(int checkId) {
        this.activityTypeId = checkId;
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
    public void sampleImageSuccess(Bitmap image) {
        this.createEventView.updateImage(image);
    }

    @Override
    public void sampleImageError(ImageStatusCode statusCode) {
        switch (statusCode) {
            case NOT_AN_IMAGE:
                createEventView.imageError("Den valgte filen var ikke et bilde");
                break;
            case FILE_NOT_FOUND:
                createEventView.imageError("Fant ikke den valgte filen");
                break;
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
            interactor.create(event, activityTypeId);
        }
    }

    @Override
    public void uploadImageError(int error) {
        this.createEventView.hideProgress();
        switch (error) {
            case Constants.RESOURCE_ACCESS_ERROR:
                this.createEventView.displayError(getActivity().getResources().getString(R.string.resource_access_error));
                break;
            case Constants.UNAUTHORIZED:
                checkAuth();
                break;
            // Dette skal ikke skje..
            case Constants.CLIENT_ERROR:
                this.createEventView.displayError(getActivity().getResources().getString(R.string.some_error));
                break;
            case Constants.SOME_ERROR:
                this.createEventView.displayError(getActivity().getResources().getString(R.string.some_error));
                break;
        }
    }

    public int getActivityTypeId() {
        return this.activityTypeId;
    }

    private long dateToLong(Calendar eventDate, int hour, int minute) {
        Calendar calendar = new GregorianCalendar(eventDate.get(Calendar.YEAR), eventDate.get(Calendar.MONTH), eventDate.get(Calendar.DAY_OF_MONTH), hour, minute);
        return calendar.getTimeInMillis();
    }
}
