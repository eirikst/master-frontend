package com.andreasogeirik.master_frontend.application.auth.register.camera;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.application.auth.register.camera.interfaces.CameraPresenter;
import com.andreasogeirik.master_frontend.application.auth.register.camera.interfaces.CameraView;
import com.andreasogeirik.master_frontend.listener.OnSampleImageFinishedListener;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;
import com.andreasogeirik.master_frontend.util.image.SampleImageTask;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by Andreas on 07.03.2016.
 */
public class CameraPresenterImpl implements CameraPresenter, OnSampleImageFinishedListener {


    CameraView cameraView;

    public CameraPresenterImpl(CameraView cameraView){
        this.cameraView = cameraView;
    }


    @Override
    public void sampleImage(byte[] byteImage) {

        InputStream is = new ByteArrayInputStream(byteImage);
        new SampleImageTask(this, is).execute();
    }

    @Override
    public void onSuccess(Bitmap bitmap, byte[] byteImage) {
        cameraView.navigateToRegisterUserView(byteImage);
    }

    @Override
    public void onError(ImageStatusCode statusCode) {

    }
}
