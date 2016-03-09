package com.andreasogeirik.master_frontend.application.auth.register.camera.interfaces;

import android.net.Uri;

/**
 * Created by Andreas on 07.03.2016.
 */
public interface CameraView {
    void navigateToRegisterUserView(Uri imageUri);
    Uri saveImage(byte[] byteImage);
}
