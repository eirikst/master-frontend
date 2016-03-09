package com.andreasogeirik.master_frontend.application.auth.register.camera;

import android.graphics.Bitmap;
import android.net.Uri;

import com.andreasogeirik.master_frontend.application.auth.register.camera.interfaces.CameraPresenter;
import com.andreasogeirik.master_frontend.application.auth.register.camera.interfaces.CameraView;
import com.andreasogeirik.master_frontend.listener.OnSampleImageFinishedListener;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;
import com.andreasogeirik.master_frontend.util.image.SampleImageTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        new SampleImageTask(this, is, true).execute();
    }

    @Override
    public void onSuccess(Bitmap bitmap, byte[] byteImage) {

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, 700, 700, false);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] scaledByteImage = stream.toByteArray();

        Uri uri = cameraView.saveImage(scaledByteImage);
        cameraView.navigateToRegisterUserView(uri);
    }

    @Override
    public void onError(ImageStatusCode statusCode) {

    }
}
