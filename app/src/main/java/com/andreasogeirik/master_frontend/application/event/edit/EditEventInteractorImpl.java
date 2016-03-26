package com.andreasogeirik.master_frontend.application.event.edit;

import com.andreasogeirik.master_frontend.application.event.edit.interfaces.EditEventInteractor;
import com.andreasogeirik.master_frontend.application.event.edit.interfaces.EditEventPresenter;
import com.andreasogeirik.master_frontend.communication.UploadImageTask;
import com.andreasogeirik.master_frontend.listener.OnEditEventFinishedListener;
import com.andreasogeirik.master_frontend.listener.OnImageUploadFinishedListener;
import com.andreasogeirik.master_frontend.model.Event;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Andreas on 10.02.2016.
 */
public class EditEventInteractorImpl implements EditEventInteractor, OnEditEventFinishedListener, OnImageUploadFinishedListener {

    private EditEventPresenter presenter;
    private Event event;

    public EditEventInteractorImpl(EditEventPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onImageUploadSuccess(String imageUrl) {
        event.setImageURI(imageUrl);
//        new EditEventTask(eventToJson(event), this).execute();
    }

    @Override
    public void onImageUploadError(int error) {
        presenter.editEventError(error);
    }

    private JSONObject eventToJson(Event event) {

        JSONObject jsonEvent = new JSONObject();
        try {
            jsonEvent.put("name", event.getName());
            jsonEvent.put("location", event.getLocation());
            jsonEvent.put("description", event.getDescription());
            jsonEvent.put("timeStart", event.getStartDate().getTimeInMillis());
            if (event.getEndDate() != null) {
                jsonEvent.put("timeEnd", event.getEndDate().getTimeInMillis());
            }
            if (event.getImageURI() != null) {
                jsonEvent.put("imageUri", event.getImageURI());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonEvent;
    }

    private long dateToLong(Calendar eventDate, int hour, int minute) {
        Calendar calendar = new GregorianCalendar(eventDate.get(Calendar.YEAR), eventDate.get(Calendar.MONTH), eventDate.get(Calendar.DAY_OF_MONTH), hour, minute);
        return calendar.getTimeInMillis();
    }

    @Override
    public void editEvent(Event event, byte[] byteImage) {
        // Saves current event in case of image upload
        this.event = event;
        if (byteImage != null) {
            // Execute image upload
            new UploadImageTask(byteImage, this).execute();
        } else {
            // No image selected, create event without image
//            new EditEventTask(eventToJson(event), this).execute();
        }
    }

    @Override
    public void onEditEventSuccess(JSONObject event) {
        presenter.editEventSuccess(event);
    }

    @Override
    public void onEditEventError(int error) {
        presenter.editEventError(error);
    }
}
