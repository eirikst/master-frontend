package com.andreasogeirik.master_frontend.util.validation.event;

import android.text.TextUtils;
import android.util.Pair;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Andreas on 22.02.2016.
 */
public class InputValidation {

    private static Date convertToDate(Calendar eventDate, Pair<Integer, Integer> timePair) {
        return new GregorianCalendar(eventDate.get(Calendar.YEAR), eventDate.get(Calendar.MONTH), eventDate.get(Calendar.DAY_OF_MONTH), timePair.first, timePair.second).getTime();
    }

    public static CreateEventValidationContainer validateEvent(String name, String location, String description, Calendar startDate, Calendar endDate, Pair<Integer, Integer> startTimePair, Pair<Integer, Integer> endTimePair) {

        // Mandatory inputs

        if (TextUtils.isEmpty(name)) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.NAME_ERROR, "Enter a name");
        } else if (TextUtils.isEmpty(location)) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.LOCATION_ERROR, "Enter a location");
        } else if (TextUtils.isEmpty(description)) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.DESCRIPTION_ERROR, "Enter a short description");
        } else if (startDate == null) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.START_DATE_ERROR, "Select a date");
        } else if (startTimePair == null) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.START_TIME_ERROR, "Select a time");
        } else if (convertToDate(startDate, startTimePair).before(new Date())) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.START_DATE_ERROR, "Select a start time after current time");
        }

        // Optional inputs
        if (endDate != null) {
            if (endTimePair == null) {
                return new CreateEventValidationContainer(CreateEventStatusCodes.END_TIME_ERROR, "Select an end time");
            } else if (convertToDate(endDate, endTimePair).before(new Date())) {
                return new CreateEventValidationContainer(CreateEventStatusCodes.END_DATE_ERROR, "Select an end time after current time");
            } else if (convertToDate(endDate, endTimePair).before(convertToDate(startDate, startTimePair))) {
                return new CreateEventValidationContainer(CreateEventStatusCodes.END_DATE_ERROR,
                        "The activity cannot start before it begins! Edit one of the time fields");
            }
        } else if (endTimePair != null && endDate == null) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.END_DATE_ERROR,
                    "Select an end date");
        }

        return new CreateEventValidationContainer(CreateEventStatusCodes.OK);

    }

}
