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
            return new CreateEventValidationContainer(CreateEventStatusCodes.NAME_ERROR, "Sett et navn");
        } else if (TextUtils.isEmpty(location)) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.LOCATION_ERROR, "Velg et sted");
        } else if (TextUtils.isEmpty(description)) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.DESCRIPTION_ERROR, "Skriv en kort beskrivelse");
        } else if (startDate == null) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.START_DATE_ERROR, "Velg en dato");
        } else if (startTimePair == null) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.START_TIME_ERROR, "Velg et tidspunkt");
        } else if (convertToDate(startDate, startTimePair).before(new Date())) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.START_DATE_ERROR, "Velg et tidspunkt etter nåværende tid");
        }

        // Optional inputs
        if (endDate != null) {
            if (endTimePair == null) {
                return new CreateEventValidationContainer(CreateEventStatusCodes.END_TIME_ERROR, "Sett et slutttidspunkt");
            } else if (convertToDate(endDate, endTimePair).before(new Date())) {
                return new CreateEventValidationContainer(CreateEventStatusCodes.END_DATE_ERROR, "Velg et slutttidspunkt etter nåværende tid");
            } else if (convertToDate(endDate, endTimePair).before(convertToDate(startDate, startTimePair))) {
                return new CreateEventValidationContainer(CreateEventStatusCodes.END_DATE_ERROR, "Aktiviteten kan ikke slutte før den begynner! Velg et nytt slutttidspunkt");
            }
        } else if (endTimePair != null && endDate == null) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.END_DATE_ERROR, "Veld en sluttdato");
        }

        return new CreateEventValidationContainer(CreateEventStatusCodes.OK);

    }

}
