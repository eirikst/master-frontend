package com.andreasogeirik.master_frontend.application.event.main;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Environment;

import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventInteractor;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventView;
import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.DateUtility;
import com.andreasogeirik.master_frontend.util.ImageInteractor;



import static com.andreasogeirik.master_frontend.util.Constants.CLIENT_ERROR;
import static com.andreasogeirik.master_frontend.util.Constants.RESOURCE_ACCESS_ERROR;


/**
 * Created by Andreas on 10.02.2016.
 */
public class EventPresenterImpl extends GeneralPresenter implements EventPresenter, ImageInteractor.OnImageFoundListener {
    private EventView eventView;
    private EventInteractor interactor;
    private Event event;


    public EventPresenterImpl(EventView eventView, Event event) {
        super((Activity) eventView);
        this.eventView = eventView;
        this.interactor = new EventInteractorImpl(this);
        this.event = event;
        //check that current user singleton is set, if not redirection
//        userAvailable();
    }

    @Override
    public void attendEvent() {
//        this.eventView.showProgress();
        interactor.attendEvent(this.event.getId());
    }


    @Override
    public void unAttendEvent() {
//        this.eventView.showProgress();
        interactor.unAttendEvent(this.event.getId());
    }

    @Override
    public void attendSuccess(Event event) {
        this.eventView.hideProgress();
        this.event = event;
        updateView();
    }

    @Override
    public void attendError(int error) {
        this.eventView.hideProgress();
        switch (error) {
            case CLIENT_ERROR:
                eventView.showErrorMessage("Du deltar allerede i denne aktiviteten");
                break;
            case RESOURCE_ACCESS_ERROR:
                eventView.showErrorMessage("Fant ikke ressurs. Prøv igjen");
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

        if (!this.event.getImageURI().isEmpty()) {
            ImageInteractor.getInstance().findImage(this.event.getImageURI(), getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES), this);
        }

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

        if (currentUser.getId() == this.event.getAdmin().getId()){
            this.eventView.setEditButton();
        }

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
    public void foundImage(String imageUri, Bitmap bitmap) {
//        eventView.hideProgress();
        this.eventView.setImage(bitmap);
    }

    @Override
    public void onProgressChange(int percent) {

    }

    @Override
    public void imageNotFound(String imageUri) {
        eventView.hideProgress();
    }


}
