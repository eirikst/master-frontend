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

    private SurfaceHolder surfaceHolder;
    private Camera camera;
    private Display display;
    private final Activity activity;

    public CameraPreview(Context context, Camera camera) {
        super(context);
        this.activity = (Activity) context;
        this.camera = camera;
        display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();


        // Install a SurfaceHolder.Callback so we get notified when the
        // underlying surface is created and destroyed.
        surfaceHolder = getHolder();
        surfaceHolder.addCallback(this);

        setKeepScreenOn(true);
    }

    public void surfaceCreated(SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        } catch (IOException e) {
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        stopPreviewAndFreeCamera();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (surfaceHolder.getSurface() == null){
            // preview surface does not exist
            return;
        }

        try {
            // stop preview before making changes
            camera.stopPreview();

            int rotation = getCameraDisplayOrientation(this.activity);

            Camera.Parameters parameters = camera.getParameters();
            Camera.Size previewSize = parameters.getSupportedPreviewSizes().get(4);
            parameters.setPreviewSize(previewSize.width, previewSize.height);

            //set rotation to save the picture
            parameters.setRotation(270);

            //set the rotation for preview camera
            camera.setDisplayOrientation(rotation);
            camera.setParameters(parameters);

            // Set the holder size based on the aspect ratio
            int size = Math.min(display.getWidth(), display.getHeight());
            double ratio = (double) previewSize.width / previewSize.height;
//            surfaceHolder.setFixedSize(size, size);

            surfaceHolder.setFixedSize((int) (size * ratio), size);
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();

        } catch (Exception e){
            Log.d(TAG, "Error starting camera preview: " + e.getMessage());
            // ignore: tried to stop a non-existent preview
        }
        try {
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();

        } catch (Exception e){
        }
    }

    private void stopPreviewAndFreeCamera() {
        if (camera != null) {
            // Call stopPreview() to stop updating the preview surface.
            camera.stopPreview();

            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            camera.release();

            camera = null;
        }
    }

    private static int getCameraDisplayOrientation(Activity activity) {
//        android.hardware.Camera.CameraInfo info =
//                new android.hardware.Camera.CameraInfo();
//        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0: degrees = 0; break;
            case Surface.ROTATION_90: degrees = 90; break;
            case Surface.ROTATION_180: degrees = 180; break;
            case Surface.ROTATION_270: degrees = 270; break;
        }

//        int result;
//        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
//            result = (info.orientation + degrees) % 360;
//            result = (360 - result) % 360;  // compensate the mirror
//        } else {  // back-facing
//            result = (info.orientation - degrees + 360) % 360;
//        }

        return (90 + 360 - degrees) % 360;
    }
}
