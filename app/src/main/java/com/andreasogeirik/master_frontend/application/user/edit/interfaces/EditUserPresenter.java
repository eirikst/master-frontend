package com.andreasogeirik.master_frontend.application.user.edit.interfaces;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;

import java.io.InputStream;

/**
 * Created by Andreas on 07.04.2016.
 */
public interface EditUserPresenter {
    void setUserAttributes();
    void updateUser(String firstname, String lastname, String location);
    void updateSuccess();
    void updateError(int error);
    void sampleImage(InputStream inputStream);
    void sampleSuccess(Bitmap image);
    void sampleError(ImageStatusCode statusCode);
    void uploadImageError(int error);
}
