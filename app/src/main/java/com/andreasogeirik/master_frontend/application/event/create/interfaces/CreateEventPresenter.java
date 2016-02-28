package com.andreasogeirik.master_frontend.application.event.create.interfaces;

import android.util.Pair;

import com.andreasogeirik.master_frontend.model.Event;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.Calendar;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface CreateEventPresenter {
    void create(String name, String location, String description, Calendar startDate, Calendar endDate, Pair<Integer, Integer> startTimePair, Pair<Integer, Integer> endTimePair);
    void createEventSuccess(JSONObject event);
    void createEventError(int error);
    void encodeImage(InputStream inputStream);
}
