package com.andreasogeirik.master_frontend.application.event.edit;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.application.event.edit.interfaces.EditEventInteractor;
import com.andreasogeirik.master_frontend.application.event.edit.interfaces.EditEventPresenter;
import com.andreasogeirik.master_frontend.communication.EditEventTask;
import com.andreasogeirik.master_frontend.communication.UploadImageTask;
import com.andreasogeirik.master_frontend.listener.OnEditEventFinishedListener;
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
public class EditEventInteractorImpl implements EditEventInteractor, OnSampleImageFinishedListener, OnEditEventFinishedListener, OnImageUploadFinishedListener {

    private EditEventPresenter presenter;
    private Event event;
    private byte[] image;
    private long activityTypeId;

    public EditEventInteractorImpl(EditEventPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onImageUploadSuccess(JSONObject jsonImageUris) {
        try {
            event.setImageUri(jsonImageUris.getString("imageUri"));
            event.setThumbUri(jsonImageUris.getString("thumbUri"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new EditEventTask(this.event.getId(), eventToJson(event), this).execute();
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
            jsonEvent.put("activityTypeId", activityTypeId);

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
    public void editEvent(Event event, long activityTypeId) {
        // Saves current event in case of image upload
        this.event = event;
        this.activityTypeId = activityTypeId;
        if (image != null) {
            // Execute image upload
            new UploadImageTask(image, this).execute();
        } else {
            // No image selected, create event without image
            new EditEventTask(this.event.getId(), eventToJson(event), this).execute();
        }
    }

    @Override
    public void sampleImage(InputStream inputStream) {
        new SampleImageTask(this, inputStream, false).execute();
    }

    @Override
    public void onEditEventSuccess(JSONObject event) {
        try {
            presenter.editEventSuccess(new Event(event));
        }
        catch(JSONException e) {
            presenter.editEventError(Constants.JSON_PARSE_ERROR);
        }
    }

    @Override
    public void onEditEventError(int error) {
        presenter.editEventError(error);
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
