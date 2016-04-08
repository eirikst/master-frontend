package com.andreasogeirik.master_frontend.application.event.main.interfaces;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.User;

import java.util.Set;

/**
 * Created by Andreas on 10.02.2016.
 */
public interface EventView {
    void setEventAttributes(String name, String location, String description, String startTime, String participants);
    void updateEndTime(String endTime);
    void setAttendButton();
    void setParticipants(String participants);
    void setUnAttendButton();
    void setEditButton();
    void setDeleteButton();
    void showErrorMessage(String error);
    void showProgress();
    void hideProgress();
    void setDifficultyView(int difficulty);
    void setImage(String imageUri);
    void initGui();
    void navigateToParticipants(Set<User> users);
    void navigateToEditEvent(Event event);
    void navigateToMain();
}
