package com.andreasogeirik.master_frontend.application.event.main.interfaces;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface EventView {
    void updateMandatoryFields(String name, String location, String description, String startTime, String participants);
    void updateEndTime(String endTime);
    void setAttendButton();
    void setUnAttendButton();
    void showErrorMessage(String error);
    void showProgress();
    void hideProgress();
    void setImage(Bitmap image);
    void initGui();
    void navigateToParticipants(Set<User> users);
}
