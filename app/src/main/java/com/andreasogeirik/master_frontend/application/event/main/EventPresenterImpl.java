package com.andreasogeirik.master_frontend.application.event.main;

import android.app.Activity;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventInteractor;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.DateUtility;


import java.util.GregorianCalendar;

import static com.andreasogeirik.master_frontend.util.Constants.CLIENT_ERROR;
import static com.andreasogeirik.master_frontend.util.Constants.RESOURCE_ACCESS_ERROR;
import static com.andreasogeirik.master_frontend.util.Constants.SOME_ERROR;
import static com.andreasogeirik.master_frontend.util.Constants.UNAUTHORIZED;


/**
 * Created by Andreas on 10.02.2016.
 */
public class EventPresenterImpl extends GeneralPresenter implements EventPresenter {
    private EventView eventView;
    private EventInteractor interactor;
    private Event event;


    public EventPresenterImpl(EventView eventView, Event event) {
        super((Activity) eventView, CHECK_USER_AVAILABLE);
        this.eventView = eventView;
        this.interactor = new EventInteractorImpl(this);
        this.event = event;
    }

    @Override
    public void attendEvent() {
        interactor.attendEvent(this.event.getId());
    }


    @Override
    public void unAttendEvent() {
        interactor.unAttendEvent(this.event.getId());
    }

    @Override
    public void attendSuccess(Event event) {
        this.event = event;
        updateView();
    }

    @Override
    public void attendError(int error) {
        this.eventView.hideProgress();
        switch (error) {
            case CLIENT_ERROR:
                eventView.showErrorMessage(getActivity().getResources().getString(R.string.some_error));
                break;
            case RESOURCE_ACCESS_ERROR:
                eventView.showErrorMessage("Fant ikke ressurs. Prøv igjen");
                break;
            case UNAUTHORIZED:
                checkAuth();
                break;
            case Constants.SOME_ERROR:
                this.eventView.showErrorMessage(getActivity().getResources().getString(R.string.some_error));
                break;
        }
    }

    @Override
    public void initGui() {
        this.eventView.initGui();
    }

    @Override
    public void setEventAttributes() {

        String participants = "";

        int NoOfParticipants = this.event.getUsers().size();
        if (NoOfParticipants == 1){
            participants = "1 DELTAKER";
        }
        else{
            participants = NoOfParticipants + " DELTAKERE";
        }


        this.eventView.setEventAttributes(event.getName(), event.getLocation(), event.getDescription(), DateUtility.formatFull(this.event.getStartDate().getTime()),
                participants);
        if (this.event.getEndDate() != null) {
            this.eventView.updateEndTime(DateUtility.formatFull(this.event.getEndDate().getTime()));
        }

        if (!this.event.getImageUri().isEmpty()) {
            eventView.setImage(event.getImageUri());
        }

        User currentUser = CurrentUser.getInstance().getUser();
        boolean userInEvent = false;

        if (this.event.getStartDate().after(new GregorianCalendar())){
            for (User user : this.event.getUsers()) {
                if (currentUser.getId() == user.getId()) {
                    userInEvent = true;
                    this.eventView.setUnAttendButton();
                    break;
                }
            }
            if (!userInEvent) {
                this.eventView.setAttendButton();
            }
        }

        if (currentUser.getId() == this.event.getAdmin().getId() && this.event.getStartDate().after(new GregorianCalendar())){
            this.eventView.setEditButton();
            this.eventView.setDeleteButton();
        }

        eventView.setDifficultyView(event.getDifficulty());

    }

    @Override
    public void updateView() {
        User currentUser = CurrentUser.getInstance().getUser();
        boolean userInEvent = false;

        for (User user : this.event.getUsers()) {
            if (currentUser.getId() == user.getId()) {
                userInEvent = true;
                this.eventView.setUnAttendButton();
                break;
            }
        }
        if (!userInEvent) {
            this.eventView.setAttendButton();
        }

        String participants = "";

        int NoOfParticipants = this.event.getUsers().size();
        if (NoOfParticipants == 1){
            participants = "1 DELTAKER";
        }
        else{
            participants = NoOfParticipants + " DELTAKERE";
        }

        this.eventView.setParticipants(participants);
    }

    @Override
    public void navigateToParticipants() {
        this.eventView.navigateToParticipants(this.event.getUsers());
    }

    @Override
    public void navigateToEditEvent() {
        this.eventView.navigateToEditEvent(this.event);
    }

    @Override
    public void deleteEvent() {
        this.eventView.showProgress();
        this.interactor.deleteEvent(this.event.getId());
    }

    @Override
    public void deleteSuccess() {
        this.eventView.hideProgress();
        this.eventView.navigateToMain();
    }

    @Override
    public void deleteError(int error) {
        this.eventView.hideProgress();
        switch (error) {
            case CLIENT_ERROR:
                eventView.showErrorMessage(getActivity().getResources().getString(R.string.some_error));
                break;
            case RESOURCE_ACCESS_ERROR:
                eventView.showErrorMessage(getActivity().getResources().getString(R.string.resource_access_error));
                break;
            case UNAUTHORIZED:
                checkAuth();
                break;
            case Constants.SOME_ERROR:
                this.eventView.showErrorMessage(getActivity().getResources().getString(R.string.some_error));
                break;
        }
    }
}
