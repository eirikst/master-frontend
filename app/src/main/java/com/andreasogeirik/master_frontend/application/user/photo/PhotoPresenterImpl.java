package com.andreasogeirik.master_frontend.application.user.photo;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.application.user.photo.interfaces.PhotoInteractor;
import com.andreasogeirik.master_frontend.application.user.photo.interfaces.PhotoPresenter;
import com.andreasogeirik.master_frontend.application.user.photo.interfaces.PhotoView;
import com.andreasogeirik.master_frontend.communication.UploadImageTask;
import com.andreasogeirik.master_frontend.listener.OnImageUploadFinishedListener;
import com.andreasogeirik.master_frontend.listener.OnSampleImageFinishedListener;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;
import com.andreasogeirik.master_frontend.util.image.SampleImageTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

/**
 * Created by Andreas on 09.03.2016.
 */
public class PhotoPresenterImpl implements PhotoPresenter, OnSampleImageFinishedListener, OnImageUploadFinishedListener {

    private PhotoView photoView;
    private PhotoInteractor interactor;

    public PhotoPresenterImpl(PhotoView photoView) {
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
//        photoView.navigateToMainView();
    }

    @Override
    public void userUpdatedError(int error) {
        photoView.setImageError("");
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
    public void onImageUploadSuccess(String imageUrl) {
        this.interactor.updateUser(imageUrl);
    }

    @Override
    public void onImageUploadError(int error) {

    }
}
