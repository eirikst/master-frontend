package com.andreasogeirik.master_frontend.application.event.create;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Pair;

import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventInteractor;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;
import com.andreasogeirik.master_frontend.listener.OnEncodeImageFinishedListener;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONObject;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Andreas on 10.02.2016.
 */
public class CreateEventPresenterImpl implements CreateEventPresenter, OnEncodeImageFinishedListener {
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
    public void encodeImage(InputStream inputStream) {
        createEventView.showProgress();
        new EncodeImageTask(this, inputStream).execute();
    }

    @Override
    public void create(Event event, String encodedImage) {
        validateInput(event, encodedImage);
    }

    private void validateInput(Event event, String encodedImage){
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
        }
        else {
            createEventView.showProgress();
            interactor.create(event, encodedImage);
        }
    }

    private Date convertToDate(Calendar eventDate, Pair<Integer, Integer> timePair) {
        return new GregorianCalendar(eventDate.get(Calendar.YEAR), eventDate.get(Calendar.MONTH), eventDate.get(Calendar.DAY_OF_MONTH), timePair.first, timePair.second).getTime();
    }

    @Override
    public void onSuccess(Bitmap bitmap, String encodedImage) {
        createEventView.hideProgress();
        createEventView.setImage(bitmap, encodedImage);
    }

    @Override
    public void onError(ImageStatusCode statusCode) {
        createEventView.hideProgress();
        if (statusCode == ImageStatusCode.FILE_NOT_FOUND){
            createEventView.setImageError("Finner ikke filen");
        }
        else if (statusCode == ImageStatusCode.NOT_AN_IMAGE){
            createEventView.setImageError("Den valgte bildefilen støttes ikke");
        }
    }
}
