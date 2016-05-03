package com.andreasogeirik.master_frontend.application.user.edit;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.application.user.edit.interfaces.EditUserInteractor;
import com.andreasogeirik.master_frontend.application.user.edit.interfaces.EditUserPresenter;
import com.andreasogeirik.master_frontend.communication.UpdateUserTask;
import com.andreasogeirik.master_frontend.communication.UploadImageTask;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.listener.OnImageUploadFinishedListener;
import com.andreasogeirik.master_frontend.listener.OnSampleImageFinishedListener;
import com.andreasogeirik.master_frontend.listener.OnUpdateUserFinishedListener;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;
import com.andreasogeirik.master_frontend.util.image.SampleImageTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;

/**
 * Created by Andreas on 07.04.2016.
 */
public class EditUserInteractorImpl implements EditUserInteractor, OnUpdateUserFinishedListener, OnSampleImageFinishedListener, OnImageUploadFinishedListener {
    private EditUserPresenter presenter;
    private byte[] image;
    private User user;

    public EditUserInteractorImpl(EditUserPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void updateUser(User user) {
        this.user = user;
        if (image == null){
            JSONObject jsonUser = new JSONObject();

            try {
                jsonUser.put("firstname", user.getFirstname());
                jsonUser.put("lastname", user.getLastname());
                jsonUser.put("location", user.getLocation());
                jsonUser.put("imageUri", user.getImageUri());
                jsonUser.put("thumbUri", user.getThumbUri());
                new UpdateUserTask(jsonUser, this).execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else{
            new UploadImageTask(image, this).execute();
        }
    }

    @Override
    public void sampleImage(InputStream inputStream) {
        new SampleImageTask(this, inputStream, true).execute();
    }

    @Override
    public void onUpdateUserSuccess(JSONObject user) {
        try {
            CurrentUser.getInstance().setUser(new User(user));
            this.presenter.updateSuccess();
        }
        catch (JSONException e) {
            this.presenter.updateError(Constants.JSON_PARSE_ERROR);
        }
    }

    @Override
    public void onUserUpdateError(int error) {
        this.presenter.updateError(error);
    }

    @Override
    public void onSampleSuccess(Bitmap bitmap, byte[] byteImage) {
        this.image = byteImage;
        this.presenter.sampleSuccess(bitmap);
    }

    @Override
    public void onSampleError(ImageStatusCode statusCode) {
        this.presenter.sampleError(statusCode);
    }

    @Override
    public void onImageUploadSuccess(JSONObject jsonImageUris) {

        String imageUri = "";
        String thumbUri = "";

        try {
            imageUri = jsonImageUris.getString("imageUri");
            thumbUri = jsonImageUris.getString("thumbUri");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject jsonUser = new JSONObject();

        try {
            jsonUser.put("firstname", user.getFirstname());
            jsonUser.put("lastname", user.getLastname());
            jsonUser.put("location", user.getLocation());
            if (!imageUri.isEmpty() && !thumbUri.isEmpty()){
                jsonUser.put("imageUri", imageUri);
                jsonUser.put("thumbUri", thumbUri);
            }
            new UpdateUserTask(jsonUser, this).execute();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onImageUploadError(int error) {
        this.presenter.uploadImageError(error);
    }
}
