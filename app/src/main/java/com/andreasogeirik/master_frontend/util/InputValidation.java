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

    public static void validateEvent(Event event, String encodedImage, CreateEventView createEventView, CreateEventInteractor interactor) {

        // Mandatory inputs
        if (TextUtils.isEmpty(event.getName())) {
            createEventView.setNameError("Sett et navn");
            return;
        } else if (TextUtils.isEmpty(event.getLocation())) {
            createEventView.setLocationError("Velg et sted");
            return;
        } else if (TextUtils.isEmpty(event.getDescription())) {
            createEventView.setDescriptionError("Skriv en kort beskrivelse");
            return;
        } else if (event.getStartDate() == null) {
            createEventView.setStartDateError("Velg en dato");
            return;
        } else if (event.getStartTime() == null) {
            createEventView.setStartTimeError("Velg et starttidspunkt");
            return;
        } else if (convertToDate(event.getStartDate(), event.getStartTime()).before(new Date())) {
            createEventView.setStartDateError("Velg et starttidspunkt etter nåværende tidspunkt");
            return;
        }

        // Optional inputs
        if (event.getEndDate() != null) {
            if (event.getEndTime() == null) {
                createEventView.setEndTimeError("Sett et slutttidspunkt");
                return;
            } else if (convertToDate(event.getEndDate(), event.getEndTime()).before(new Date())) {
                createEventView.setEndDateError("Velg et slutttidspunkt etter nåværende tidspunkt");
                return;

            } else if (convertToDate(event.getEndDate(), event.getEndTime()).before(convertToDate(event.getStartDate(), event.getStartTime()))) {
                createEventView.setEndDateError("Velg et tidspunkt etter startdato");
                return;
            }
        } else if (event.getEndTime() != null && event.getEndDate() == null) {
            createEventView.setEndDateError("Veld en sluttdato");
            return;
        }

        createEventView.showProgress();
        interactor.create(event, encodedImage);
    }

    private static Date convertToDate(Calendar eventDate, Pair<Integer, Integer> timePair) {
        return new GregorianCalendar(eventDate.get(Calendar.YEAR), eventDate.get(Calendar.MONTH), eventDate.get(Calendar.DAY_OF_MONTH), timePair.first, timePair.second).getTime();
    }

}
