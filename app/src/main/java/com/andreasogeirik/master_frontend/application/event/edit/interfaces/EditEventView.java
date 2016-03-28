package com.andreasogeirik.master_frontend.application.event.edit.interfaces;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.andreasogeirik.master_frontend.model.Event;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface EditEventView {
    void navigateToEventView(Event event);
    void editEventFailed(String errorMessage);
    void showProgress();
    void hideProgress();
    void setNameError(String error);
    void setLocationError(String error);
    void setDescriptionError(String error);
    void setStartDateError(String error);
    void setEndDateError(String error);
    void setImageError(String error);
    void setImage(Bitmap bitmap);
    void setEventAttributes(String name, String location, String description, String startDate, String startTime);
    void setEndDate(String endDate, String endTime);
    void onDateSet(Bundle bundle);
    void onTimeSet(Bundle bundle);
    void updateStartDateView(int day, int month, int year);
    void updateEndDateView(int day, int month, int year);
    void updateStartTimeView(int hour, int minute);
    void updateEndTimeView(int hour, int minute);
}
