package com.andreasogeirik.master_frontend.application.event.edit;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.util.Pair;
import android.view.View;

import com.andreasogeirik.master_frontend.application.event.create.fragments.DatePickerFragment;
import com.andreasogeirik.master_frontend.application.event.edit.interfaces.EditEventInteractor;
import com.andreasogeirik.master_frontend.application.event.edit.interfaces.EditEventPresenter;
import com.andreasogeirik.master_frontend.application.event.edit.interfaces.EditEventView;
import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.listener.OnSampleImageFinishedListener;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.DateUtility;
import com.andreasogeirik.master_frontend.util.ImageInteractor;
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
public class EditEventPresenterImpl extends GeneralPresenter implements EditEventPresenter, OnSampleImageFinishedListener, ImageInteractor.OnImageFoundListener {
    EditEventView editEventView;
    private EditEventInteractor interactor;
    private byte[] byteImage;
    private Event event;

    private Calendar startDate;
    private Calendar endDate;

    private Pair<Integer, Integer> startTimePair;
    private Pair<Integer, Integer> endTimePair;

    public EditEventPresenterImpl(EditEventView editEventView, Event event) {
        super((Activity) editEventView);
        this.editEventView = editEventView;
        this.interactor = new EditEventInteractorImpl(this);
        this.event = event;
        this.startDate = event.getStartDate();
        this.startTimePair = new Pair<>(startDate.get(Calendar.HOUR), startDate.get(Calendar.MINUTE));
        if (event.getEndDate() != null) {
            this.endDate = event.getEndDate();
            this.endTimePair = new Pair<>(endDate.get(Calendar.HOUR), endDate.get(Calendar.MINUTE));
        }
    }

    @Override
    public void editEventSuccess(JSONObject event) {
        this.editEventView.navigateToEventView();
    }

    @Override
    public void editEventError(int error) {
        this.editEventView.hideProgress();

        if (error == Constants.CLIENT_ERROR) {
            this.editEventView.editEventFailed("En uventet feil oppstod. Prøv igjen.");
        } else if (error == Constants.RESOURCE_ACCESS_ERROR) {
            this.editEventView.editEventFailed("Fant ikke ressurs. Prøv igjen.");
        }
    }

    @Override
    public void SampleImage(InputStream inputStream) {
        this.editEventView.showProgress();
        new SampleImageTask(this, inputStream, false).execute();
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
        this.editEventView.setEventAttributes(event.getName(), event.getLocation(), event.getDescription(), DateUtility.format(event.getStartDate().getTime()), DateUtility.formatTime(event.getStartDate().getTime()));

        if (!this.event.getImageURI().isEmpty()) {
            ImageInteractor.getInstance().findImage(event.getImageURI(), getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), this);
        }

        if (event.getEndDate() != null) {
            this.editEventView.setEndDate(DateUtility.format(event.getEndDate().getTime()), DateUtility.formatTime(event.getEndDate().getTime()));
        }
    }

    @Override
    public void editEvent(String name, String location, String description) {

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
            Event event = new Event(name, location, description, startDateCal);
            // Check if end date and time is chosen
            if (endDate != null && endTimePair != null) {
                Calendar endDateCal = new GregorianCalendar();
                endDateCal.setTimeInMillis(dateToLong(endDate, endTimePair.first, endTimePair.second));
                event.setEndDate(endDateCal);
            }
            this.editEventView.showProgress();
            interactor.editEvent(event, byteImage);
        }
    }

    @Override
    public void onSampleSuccess(Bitmap bitmap, byte[] byteImage) {
        this.editEventView.hideProgress();
        this.byteImage = byteImage;
        this.editEventView.setImage(bitmap);
    }

    @Override
    public void onSampleError(ImageStatusCode statusCode) {
        this.editEventView.hideProgress();
        if (statusCode == ImageStatusCode.FILE_NOT_FOUND) {
            this.editEventView.setImageError("Finner ikke filen");
        } else if (statusCode == ImageStatusCode.NOT_AN_IMAGE) {
            this.editEventView.setImageError("Den valgte bildefilen støttes ikke");
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
    }

    private long dateToLong(Calendar eventDate, int hour, int minute) {
        Calendar calendar = new GregorianCalendar(eventDate.get(Calendar.YEAR), eventDate.get(Calendar.MONTH), eventDate.get(Calendar.DAY_OF_MONTH), hour, minute);
        return calendar.getTimeInMillis();
    }

    @Override
    public void foundImage(String imageUri, Bitmap bitmap) {
        this.editEventView.setImage(bitmap);
    }

    @Override
    public void onProgressChange(int percent) {

    }

    @Override
    public void imageNotFound(String imageUri) {

    }
}
