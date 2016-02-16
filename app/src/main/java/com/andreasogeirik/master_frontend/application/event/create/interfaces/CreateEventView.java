package com.andreasogeirik.master_frontend.application.event.create.interfaces;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface CreateEventView {
    void navigateToEventView();
    void createEventFailed(String errorMessage);
    void showProgress();
    void hideProgress();
    void setNameError(String error);
    void setLocationError(String error);
    void setDescriptionError(String error);
    void setDateError(String error);
    void setTimeStartError(String error);
}
