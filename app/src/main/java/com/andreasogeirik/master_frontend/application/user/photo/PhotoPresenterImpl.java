package com.andreasogeirik.master_frontend.application.user.photo;

import android.app.Activity;
import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.application.general.GeneralPresenter;
import com.andreasogeirik.master_frontend.application.user.photo.interfaces.PhotoInteractor;
import com.andreasogeirik.master_frontend.application.user.photo.interfaces.PhotoPresenter;
import com.andreasogeirik.master_frontend.application.user.photo.interfaces.PhotoView;
import com.andreasogeirik.master_frontend.communication.UploadImageTask;
import com.andreasogeirik.master_frontend.listener.OnImageUploadFinishedListener;
import com.andreasogeirik.master_frontend.listener.OnSampleImageFinishedListener;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;
import com.andreasogeirik.master_frontend.util.image.SampleImageTask;

import org.json.JSONException;
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

    public PhotoPresenterImpl(PhotoView photoView) {
        super((Activity)photoView, GeneralPresenter.NO_CHECK);
        this.photoView = photoView;
        this.interactor = new PhotoInteractorImpl(this);
    }

    public void samplePhoto(InputStream inputStream){
        new SampleImageTask(this, inputStream, true).execute();
    }

    @Override
    public void uploadPhoto(byte[] byteImage) {
        new UploadImageTask(byteImage, this).execute();
    }

    @Override
    public void userUpdatedSuccess() {
        photoView.sendMessage();
    }

    @Override
    public void userUpdatedError(int error) {
        checkAuth();
    }


    @Override
    public void onSampleSuccess(Bitmap bitmap, byte[] byteImage) {
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 300, 300, false);

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] scaledByteImage = stream.toByteArray();

        photoView.setImage(scaledByteImage, scaledBitmap);
    }

    @Override
    public void onSampleError(ImageStatusCode statusCode) {

    }

    @Override
    public void onImageUploadSuccess(JSONObject jsonImageUris) {
        this.interactor.updateUser(jsonImageUris);
    }

    @Override
    public void onImageUploadError(int error) {
        checkAuth();
    }
}
