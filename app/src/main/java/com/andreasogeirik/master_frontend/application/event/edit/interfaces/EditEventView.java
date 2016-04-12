package com.andreasogeirik.master_frontend.application.event.edit.interfaces;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface EditEventView {
    void navigateToEventView(Event event);
    void displayError(String errorMessage);
    void showProgress();
    void hideProgress();
    void setNameError(String error);
    void setLocationError(String error);
    void setDescriptionError(String error);
    void setStartDateError(String error);
    void setEndDateError(String error);
    void imageError(String error);
    void setImage(String imageUri);
    void setEventAttributes(String name, String location, String description, String startDate, String startTime, int difficulty);
    void setEndDate(String endDate, String endTime);
    void onDateSet(Bundle bundle);
    void onTimeSet(Bundle bundle);
    void updateStartDateView(int day, int month, int year);
    void updateEndDateView(int day, int month, int year);
    void updateStartTimeView(int hour, int minute);
    void updateEndTimeView(int hour, int minute);
    void updateImage(Bitmap image);

}
