package com.andreasogeirik.master_frontend.application.event.create.interfaces;

import android.graphics.Bitmap;
import android.util.Pair;

import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;

import java.io.InputStream;
import java.util.Calendar;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface CreateEventPresenter {
    void create(String name, String location, String description, int difficulty);
    void createEventSuccess(Event event);
    void createEventError(int error);
    void sampleImage(InputStream inputStream);
    void deleteEndTimes();
    void setDate(Boolean isStartDate);
    void setTime(Boolean isStarTime);
    void updateDateModel(Calendar eventDate, Boolean isStartDate);
    void updateTimeModel(Pair<Integer, Integer> hourMinutePair, Boolean isStartTime);
    void sampleImageSuccess(Bitmap image);
    void sampleImageError(ImageStatusCode statusCode);
    void uploadImageError(int error);
}

