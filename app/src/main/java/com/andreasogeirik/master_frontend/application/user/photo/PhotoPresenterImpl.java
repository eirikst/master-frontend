package com.andreasogeirik.master_frontend.application.user.photo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.main.MainPageActivity;
import com.andreasogeirik.master_frontend.application.user.photo.interfaces.PhotoInteractor;
import com.andreasogeirik.master_frontend.application.user.photo.interfaces.PhotoPresenter;
import com.andreasogeirik.master_frontend.application.user.photo.interfaces.PhotoView;
import com.andreasogeirik.master_frontend.communication.UploadImageTask;
import com.andreasogeirik.master_frontend.listener.OnImageUploadFinishedListener;
import com.andreasogeirik.master_frontend.listener.OnSampleImageFinishedListener;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;
import com.andreasogeirik.master_frontend.util.image.SampleImageTask;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Andreas on 09.03.2016.
 */
public class PhotoPresenterImpl extends GeneralPresenter implements PhotoPresenter,
        OnSampleImageFinishedListener, OnImageUploadFinishedListener {

    private PhotoView photoView;
    private PhotoInteractor interactor;

    private byte[] image;

    public PhotoPresenterImpl(PhotoView photoView) {
        super((Activity) photoView, GeneralPresenter.NO_CHECK);
        this.photoView = photoView;
        this.interactor = new PhotoInteractorImpl(this);
    }

    @Override
    public void submit() {
        if (image != null) {
            uploadPhoto(image);
        }
        this.photoView.navigateToMainView();
    }

    public void samplePhoto(InputStream inputStream) {
        new SampleImageTask(this, inputStream, true).execute();
    }

    @Override
    public void uploadPhoto(byte[] byteImage) {
        new UploadImageTask(byteImage, this).execute();
    }

    @Override
    public void userUpdatedSuccess() {
        photoView.sendMessage("Profil oppdatert!");
//        photoView.navigateToMainView();
    }

    @Override
    public void userUpdatedError(int error) {
        photoView.sendMessage("Noe gikk galt under oppdatering av bruker");
    }


    @Override
    public void onSampleSuccess(Bitmap bitmap, byte[] byteImage) {
        this.image = byteImage;
        this.photoView.setImage(bitmap);
    }

    @Override
    public void onSampleError(ImageStatusCode statusCode) {
        switch (statusCode) {
            case NOT_AN_IMAGE:
                photoView.imageError("Den valgte filen var ikke et bilde");
                break;
            case FILE_NOT_FOUND:
                photoView.imageError("Fant ikke den valgte filen");
                break;
        }
    }

    @Override
    public void onImageUploadSuccess(JSONObject jsonImageUris) {
        this.interactor.updateUser(jsonImageUris);
    }

    @Override
    public void onImageUploadError(int error) {
        photoView.sendMessage("Noe gikk galt under opplasting av bilde");

    }
}
