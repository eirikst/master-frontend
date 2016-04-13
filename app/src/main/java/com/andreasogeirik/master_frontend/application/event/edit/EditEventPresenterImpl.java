package com.andreasogeirik.master_frontend.application.event.edit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Pair;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.edit.interfaces.EditEventInteractor;
import com.andreasogeirik.master_frontend.application.event.edit.interfaces.EditEventPresenter;
import com.andreasogeirik.master_frontend.application.event.edit.interfaces.EditEventView;
import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.DateUtility;
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
public class EditEventPresenterImpl extends GeneralPresenter implements EditEventPresenter {
    EditEventView editEventView;
    private EditEventInteractor interactor;
    private Event event;

    private Calendar startDate;
    private Calendar endDate;

    private Pair<Integer, Integer> startTimePair;
    private Pair<Integer, Integer> endTimePair;

    public EditEventPresenterImpl(EditEventView editEventView, Event event) {
        super((Activity) editEventView, GeneralPresenter.NO_CHECK);
        this.editEventView = editEventView;
        this.interactor = new EditEventInteractorImpl(this);
        this.event = event;
        this.startDate = event.getStartDate();
        this.startTimePair = new Pair<>(startDate.get(Calendar.HOUR_OF_DAY), startDate.get(Calendar.MINUTE));
        if (event.getEndDate() != null) {
            this.endDate = event.getEndDate();
            this.endTimePair = new Pair<>(endDate.get(Calendar.HOUR_OF_DAY), endDate.get(Calendar.MINUTE));
        }
    }

    @Override
    public void editEventSuccess(Event event) {
        this.editEventView.navigateToEventView(event);
    }

    @Override
    public void editEventError(int error) {
        this.editEventView.hideProgress();
        switch (error) {
            case Constants.RESOURCE_ACCESS_ERROR:
                this.editEventView.displayError(getActivity().getResources().getString(R.string.resource_access_error));
                break;
            case Constants.UNAUTHORIZED:
                checkAuth();
                break;
            // Dette skal ikke skje..
            case Constants.CLIENT_ERROR:
                this.editEventView.displayError(getActivity().getResources().getString(R.string.some_error));
                break;
            case Constants.SOME_ERROR:
                this.editEventView.displayError(getActivity().getResources().getString(R.string.some_error));
                break;
        }
    }

    @Override
    public void SampleImage(InputStream inputStream) {
        this.interactor.sampleImage(inputStream);
    }

    @Override
    public void setTime(Boolean isStarTime) {
        Bundle bundle = new Bundle();
        if (isStarTime) {
            bundle.putString("time", "start");
            bundle.putInt("hour", this.startTimePair.first);
            bundle.putInt("minute", this.startTimePair.second);
        } else {
            bundle.putString("time", "end");
            if (this.endTimePair != null) {
                bundle.putInt("hour", this.endTimePair.first);
                bundle.putInt("minute", this.endTimePair.second);
            }
        }
        this.editEventView.onTimeSet(bundle);
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
        this.editEventView.onDateSet(bundle);
    }

    @Override
    public void setEventAttributes() {
        this.editEventView.setEventAttributes(event.getName(), event.getLocation(),
                event.getDescription(), DateUtility.format(event.getStartDate().getTime()),
                DateUtility.formatTime(event.getStartDate().getTime()), event.getDifficulty());

        if (this.event.getImageUri() != null && !this.event.getImageUri().isEmpty()) {
            editEventView.setImage(this.event.getImageUri());
        }

        if (event.getEndDate() != null) {
            this.editEventView.setEndDate(DateUtility.format(event.getEndDate().getTime()), DateUtility.formatTime(event.getEndDate().getTime()));
        }
    }

    @Override
    public void editEvent(String name, String location, String description, int difficulty) {

        CreateEventValidationContainer createEventValidationContainer = InputValidation.validateEvent(name, location, description, startDate, endDate, startTimePair, endTimePair);
        if (createEventValidationContainer.getStatusCode() != CreateEventStatusCodes.OK) {
            String error = createEventValidationContainer.getError();
            switch (createEventValidationContainer.getStatusCode()) {
                case NAME_ERROR:
                    this.editEventView.setNameError(error);
                    break;
                case LOCATION_ERROR:
                    this.editEventView.setLocationError(error);
                    break;
                case DESCRIPTION_ERROR:
                    this.editEventView.setDescriptionError(error);
                    break;
                case START_DATE_ERROR:
                    this.editEventView.setStartDateError(error);
                    break;
                case START_TIME_ERROR:
                    this.editEventView.setStartDateError(error);
                    break;
                case END_DATE_ERROR:
                    this.editEventView.setEndDateError(error);
                    break;
                case END_TIME_ERROR:
                    this.editEventView.setEndDateError(error);
                    break;
            }
        } else {
            // Set start time in milliseconds
            Calendar startDateCal = new GregorianCalendar();
            startDateCal.setTimeInMillis(dateToLong(startDate, startTimePair.first, startTimePair.second));
            this.event.setName(name);
            this.event.setLocation(location);
            this.event.setDescription(description);
            this.event.setStartDate(startDateCal);
            this.event.setDifficulty(difficulty + 1);

            // Check if end date and time is chosen
            if (endDate != null && endTimePair != null) {
                Calendar endDateCal = new GregorianCalendar();
                endDateCal.setTimeInMillis(dateToLong(endDate, endTimePair.first, endTimePair.second));
                this.event.setEndDate(endDateCal);
            }
            this.editEventView.showProgress();
            interactor.editEvent(event);
        }
    }

    @Override
    public void sampleImageSuccess(Bitmap image) {
        this.editEventView.updateImage(image);
    }

    @Override
    public void sampleImageError(ImageStatusCode statusCode) {
        switch (statusCode) {
            case NOT_AN_IMAGE:
                editEventView.imageError("Den valgte filen var ikke et bilde");
                break;
            case FILE_NOT_FOUND:
                editEventView.imageError("Fant ikke den valgte filen");
                break;
        }
    }

    @Override
    public void uploadImageError(int error) {
        this.editEventView.hideProgress();
        switch (error) {
            case Constants.RESOURCE_ACCESS_ERROR:
                this.editEventView.displayError(getActivity().getResources().getString(R.string.resource_access_error));
                break;
            case Constants.UNAUTHORIZED:
                checkAuth();
                break;
            // Dette skal ikke skje..
            case Constants.CLIENT_ERROR:
                this.editEventView.displayError(getActivity().getResources().getString(R.string.some_error));
                break;
            case Constants.SOME_ERROR:
                this.editEventView.displayError(getActivity().getResources().getString(R.string.some_error));
                break;
        }
    }

    @Override
    public void updateDateModel(Calendar eventDate, Boolean isStartDate) {
        int day = eventDate.get(Calendar.DAY_OF_MONTH);
        int month = eventDate.get(Calendar.MONTH) + 1;
        int year = eventDate.get(Calendar.YEAR);

        if (isStartDate) {
            this.startDate = eventDate;
            this.editEventView.updateStartDateView(day, month, year);
        } else {
            this.endDate = eventDate;
            this.editEventView.updateEndDateView(day, month, year);
        }
    }

    @Override
    public void updateTimeModel(Pair<Integer, Integer> hourMinutePair, Boolean isStartTime) {
        int hour = hourMinutePair.first;
        int minute = hourMinutePair.second;
        if (isStartTime) {
            this.startTimePair = hourMinutePair;
            this.editEventView.updateStartTimeView(hour, minute);
        } else {
            this.endTimePair = hourMinutePair;
            this.editEventView.updateEndTimeView(hour, minute);
        }
    }

    @Override
    public void deleteEndTimes() {
        this.endDate = null;
        this.endTimePair = null;
        this.event.setEndDate(null);
    }

    private long dateToLong(Calendar eventDate, int hour, int minute) {
        Calendar calendar = new GregorianCalendar(eventDate.get(Calendar.YEAR), eventDate.get(Calendar.MONTH), eventDate.get(Calendar.DAY_OF_MONTH), hour, minute);
        return calendar.getTimeInMillis();
    }
}
