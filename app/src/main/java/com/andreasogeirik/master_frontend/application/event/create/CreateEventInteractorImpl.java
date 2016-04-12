package com.andreasogeirik.master_frontend.application.event.create;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventInteractor;
import com.andreasogeirik.master_frontend.application.event.create.interfaces.CreateEventPresenter;
import com.andreasogeirik.master_frontend.communication.CreateEventTask;
import com.andreasogeirik.master_frontend.communication.UploadImageTask;
import com.andreasogeirik.master_frontend.listener.OnCreateEventFinishedListener;
import com.andreasogeirik.master_frontend.listener.OnImageUploadFinishedListener;
import com.andreasogeirik.master_frontend.listener.OnSampleImageFinishedListener;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;
import com.andreasogeirik.master_frontend.util.image.SampleImageTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Andreas on 10.02.2016.
 */
public class CreateEventInteractorImpl implements CreateEventInteractor, OnCreateEventFinishedListener, OnSampleImageFinishedListener, OnImageUploadFinishedListener {

    private CreateEventPresenter presenter;
    private Event event;
    private byte[] image;

    public CreateEventInteractorImpl(CreateEventPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void create(Event event) {
        // Saves current event in case of image upload
        this.event = event;
        if (this.image != null) {
            // Execute image upload
            new UploadImageTask(image, this).execute();
        } else {
            // No image selected, create event without image
            new CreateEventTask(eventToJson(event), this).execute();
        }
    }

    @Override
    public void sampleImage(InputStream inputStream) {
        new SampleImageTask(this, inputStream, false).execute();
    }

    @Override
    public void onCreateEventSuccess(JSONObject event) {
        try {
            presenter.createEventSuccess(new Event(event));
        }
        catch(JSONException e) {
            presenter.createEventError(Constants.JSON_PARSE_ERROR);
        }
    }

    @Override
    public void onCreateEventError(int error) {
        presenter.createEventError(error);
    }

    @Override
    public void onImageUploadSuccess(JSONObject jsonImageUris) {

        try {
            event.setImageUri(jsonImageUris.getString("imageUri"));
            event.setThumbUri(jsonImageUris.getString("thumbUri"));
            new CreateEventTask(eventToJson(event), this).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageUploadError(int error) {
        presenter.uploadImageError(error);
    }

    private JSONObject eventToJson(Event event) {

        JSONObject jsonEvent = new JSONObject();
        try {
            jsonEvent.put("name", event.getName());
            jsonEvent.put("location", event.getLocation());
            jsonEvent.put("description", event.getDescription());
            jsonEvent.put("timeStart", event.getStartDate().getTimeInMillis());
            jsonEvent.put("difficulty", event.getDifficulty());
            if (event.getEndDate() != null) {
                jsonEvent.put("timeEnd", event.getEndDate().getTimeInMillis());
            }
            if (event.getImageUri() != null) {
                jsonEvent.put("imageUri", event.getImageUri());
                jsonEvent.put("thumbUri", event.getThumbUri());
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
    public void onSampleSuccess(Bitmap bitmap, byte[] byteImage) {
        this.image = byteImage;
        this.presenter.sampleImageSuccess(bitmap);
    }

    @Override
    public void onSampleError(ImageStatusCode statusCode) {
        this.presenter.sampleImageError(statusCode);
    }
}
