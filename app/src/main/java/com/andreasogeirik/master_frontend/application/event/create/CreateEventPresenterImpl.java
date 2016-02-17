package com.andreasogeirik.master_frontend.application.event.create;

import android.text.TextUtils;
import android.util.Pair;

import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventInteractor;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Andreas on 10.02.2016.
 */
public class CreateEventPresenterImpl implements CreateEventPresenter {
    CreateEventView createEventView;
    private CreateEventInteractor interactor;


    public CreateEventPresenterImpl(CreateEventView createEventView) {
        this.createEventView = createEventView;
        this.interactor = new CreateEventInteractorImpl(this);
    }


    @Override
    public void createEventSuccess(JSONObject event) {
        createEventView.navigateToEventView();
    }

    @Override
    public void createEventError(int error) {
        createEventView.hideProgress();

        if (error == Constants.CLIENT_ERROR){
            createEventView.createEventFailed("En uventet feil oppstod. Prøv igjen.");
        }
        else if(error == Constants.RESOURCE_ACCESS_ERROR) {
            createEventView.createEventFailed("Fant ikke ressurs. Prøv igjen.");
        }
    }

    @Override
    public void create(Event event) {
        validateInput(event);
    }

    private void validateInput(Event event){
        if (TextUtils.isEmpty(event.getName())) {
            createEventView.setNameError("Sett et navn");
        } else if (TextUtils.isEmpty(event.getLocation())) {
            createEventView.setLocationError("Velg et sted");
        } else if (TextUtils.isEmpty(event.getDescription())) {
            createEventView.setDescriptionError("Skriv en kort beskrivelse");
        } else if (event.getStartDate() == null) {
            createEventView.setStartDateError("Velg en dato");
        } else if (event.getTimeStart() == null) {
            createEventView.setStartTimeError("Velg et starttidspunkt");
        } else if (convertToDate(event.getStartDate(), event.getTimeStart()).before(new Date())) {
            createEventView.setStartDateError("Velg en dato etter dagens dato");
        } else if (event.getEndDate() != null) {
            if (event.getTimeEnd() == null) {
                createEventView.setEndTimeError("Sett tidspunkt slutt");
            } else if (convertToDate(event.getStartDate(), event.getTimeStart()).before(new Date())) {
                createEventView.setEndDateError("Velg et tidspunkt etter dagens dagens dato");

            } else if (convertToDate(event.getEndDate(), event.getTimeEnd()).before(convertToDate(event.getStartDate(), event.getTimeStart()))) {
                createEventView.setEndDateError("Velg et tidspunkt etter startdato");
            }
        } else if (event.getTimeEnd() != null && event.getEndDate() == null) {
            createEventView.setEndDateError("Veld en dato slutt");
        } else {
            createEventView.showProgress();
            interactor.create(event);
        }
    }

    private Date convertToDate(Calendar eventDate, Pair<Integer, Integer> timePair) {
        return new GregorianCalendar(eventDate.get(Calendar.YEAR), eventDate.get(Calendar.MONTH), eventDate.get(Calendar.DAY_OF_MONTH), timePair.first, timePair.second).getTime();
    }
}
