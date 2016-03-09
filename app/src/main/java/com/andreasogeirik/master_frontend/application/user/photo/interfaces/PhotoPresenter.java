package com.andreasogeirik.master_frontend.application.user.photo.interfaces;

import java.io.InputStream;

/**
 * Created by Andreas on 09.03.2016.
 */
public interface PhotoPresenter {
    void samplePhoto(InputStream inputStream);
    void uploadPhoto(byte[] byteImage);
    void userUpdatedSuccess();
    void userUpdatedError(int error);
}
