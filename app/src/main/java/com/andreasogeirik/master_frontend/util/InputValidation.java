package com.andreasogeirik.master_frontend.util;

import android.text.TextUtils;
import android.util.Pair;

import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventInteractor;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;
import com.andreasogeirik.master_frontend.model.Event;

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

    public static CreateEventValidationContainer validateEvent(Event event) {

        // Mandatory inputs

        if (TextUtils.isEmpty(event.getName())) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.NAME_ERROR, "Sett et navn");
        } else if (TextUtils.isEmpty(event.getLocation())) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.LOCATION_ERROR, "Velg et sted");
        } else if (TextUtils.isEmpty(event.getDescription())) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.DESCRIPTION_ERROR, "Skriv en kort beskrivelse");
        } else if (event.getStartDate() == null) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.START_DATE_ERROR, "Velg en dato");
        } else if (event.getStartTime() == null) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.START_TIME_ERROR, "Velg et starttidspunkt");
        } else if (convertToDate(event.getStartDate(), event.getStartTime()).before(new Date())) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.START_DATE_ERROR, "Velg et starttidspunkt etter nåværende tidspunkt");
        }

        // Optional inputs
        if (event.getEndDate() != null) {
            if (event.getEndTime() == null) {
                return new CreateEventValidationContainer(CreateEventStatusCodes.END_TIME_ERROR, "Sett et slutttidspunkt");
            } else if (convertToDate(event.getEndDate(), event.getEndTime()).before(new Date())) {
                return new CreateEventValidationContainer(CreateEventStatusCodes.END_DATE_ERROR, "Velg et slutttidspunkt etter nåværende tidspunkt");
            } else if (convertToDate(event.getEndDate(), event.getEndTime()).before(convertToDate(event.getStartDate(), event.getStartTime()))) {
                return new CreateEventValidationContainer(CreateEventStatusCodes.END_DATE_ERROR, "Velg et tidspunkt etter startdato");
            }
        } else if (event.getEndTime() != null && event.getEndDate() == null) {
            return new CreateEventValidationContainer(CreateEventStatusCodes.END_DATE_ERROR, "Veld en sluttdato");
        }

        return new CreateEventValidationContainer(CreateEventStatusCodes.OK);

    }

}
