package com.andreasogeirik.master_frontend.application.auth.register.camera;

import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.io.IOException;

/**
 * Created by Andreas on 07.03.2016.
 */
public class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private final String TAG = "PIC-FRAME";

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private Display display;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        mCamera = camera;
        display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();


        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        setKeepScreenOn(true);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
        } catch (IOException e) {
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPreviewAndFreeCamera();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

//        mCamera.setDisplayOrientation(90);
        if (mHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        try {
            // stop preview before making changes
            mCamera.stopPreview();

            Camera.Parameters parameters = mCamera.getParameters();
            Camera.Size previewSize = parameters.getSupportedPreviewSizes().get(4);
            parameters.setPreviewSize(previewSize.width, previewSize.height);


            mCamera.setParameters(parameters);

            // Set the holder size based on the aspect ratio
            int size = Math.min(display.getWidth(), display.getHeight());
            double ratio = (double) previewSize.width / previewSize.height;

            mHolder.setFixedSize((int)(size * ratio), size);
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            // ignore: tried to stop a non-existent preview
        }
        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.startPreview();

        } catch (Exception e){
        }
    }

    private void stopPreviewAndFreeCamera() {
        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera.stopPreview();

            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            mCamera.release();

            mCamera = null;
        }
    }




}
