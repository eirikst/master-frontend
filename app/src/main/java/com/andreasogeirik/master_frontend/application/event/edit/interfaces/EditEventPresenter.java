package com.andreasogeirik.master_frontend.application.event.edit.interfaces;

import android.util.Pair;

import com.andreasogeirik.master_frontend.model.Event;


import java.io.InputStream;
import java.util.Calendar;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface EditEventPresenter {
    void editEvent(String name, String location, String description);
    void editEventSuccess(Event event);
    void editEventError(int error);
    void SampleImage(InputStream inputStream);
    void setEventAttributes();
    void setDate(Boolean isStartDate);
    void setTime(Boolean isStarTime);
    void updateDateModel(Calendar eventDate, Boolean isStartDate);
    void updateTimeModel(Pair<Integer, Integer> hourMinutePair, Boolean isStartTime);
    void deleteEndTimes();
}
