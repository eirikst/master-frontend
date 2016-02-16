package com.andreasogeirik.master_frontend.application.event.create;

import android.text.TextUtils;
import android.util.Pair;

import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventInteractor;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventView;
import com.andreasogeirik.master_frontend.listener.OnCreateEventFinishedListener;
import com.andreasogeirik.master_frontend.model.Event;

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
        createEventView.createEventFailed("");
    }

    @Override
    public void create(Event event) {
        if (TextUtils.isEmpty(event.getName())){
            createEventView.setNameError("Sett et navn");
        }
        else if (TextUtils.isEmpty(event.getLocation())){
            createEventView.setLocationError("Velg et sted");
        }
        else if (TextUtils.isEmpty(event.getDescription())){
            createEventView.setDescriptionError("Skriv en kort beskrivelse");
        }
        else if (event.getEventDate() == null){
            createEventView.setDateError("Velg en dato");
        }
        if (event.getTimeStart() == null){
            createEventView.setTimeStartError("Velg et starttidspunkt");
        }
        else{
            createEventView.showProgress();
            interactor.create(event);
        }
//        else{
//            Date startTime = convertToDate(event.getEventDate(), event.getTimeStart());
//            if (startTime.before(new Date())){
//                createEventView.setTimeStartError("Tidspunktet må settes etter nåverende tidspunkt");
//            }
//            if (event.getTimeEnd() != null){
//                Date endTime = convertToDate(event.getEventDate(), event.getTimeStart());
//
//            }
//        }

//        else if (event.getTimeEnd() != null){
//
//            if (event.getTimeEnd().first < event.getTimeStart().first || event.getTimeEnd().second < event.getTimeStart().second)
//        }

    }

    private Date convertToDate(Calendar eventDate, Pair<Integer, Integer> timePair){
        return new GregorianCalendar(eventDate.get(Calendar.YEAR), eventDate.get(Calendar.MONTH), eventDate.get(Calendar.DAY_OF_MONTH), timePair.first, timePair.second).getTime();
    }
}
