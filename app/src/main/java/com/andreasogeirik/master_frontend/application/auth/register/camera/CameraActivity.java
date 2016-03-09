package com.andreasogeirik.master_frontend.application.auth.register.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.andreasogeirik.master_frontend.R;
import com.andreasogeirik.master_frontend.application.auth.register.camera.interfaces.CameraPresenter;
import com.andreasogeirik.master_frontend.application.auth.register.camera.interfaces.CameraView;

import org.apache.commons.lang3.RandomStringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Andreas on 07.03.2016.
 */
public class CameraActivity extends AppCompatActivity implements CameraView {

    @Bind(R.id.capture_photo)
    Button capturePhoto;

    private Camera mCamera;
    private CameraPreview mPreview;
    private CameraPresenter presenter;
    private static int FRONT_CAMERA_ID = 1;

    private Camera.PictureCallback mPicture = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            releaseCamera();

            if (!isExternalStorageWritable()){
                Intent i = new Intent();
                setResult(Activity.RESULT_CANCELED, i);
                finish();
            }

            presenter.sampleImage(data);
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_activity);
        ButterKnife.bind(this);

        this.presenter = new CameraPresenterImpl(this);


        // Create an instance of Camera
        try{
            mCamera = getCameraInstance();
            CameraOrientation.setCameraDisplayOrientation(this, FRONT_CAMERA_ID, mCamera);
        }
        catch (RuntimeException e){
            Toast.makeText(this, "Kan ikke åpne kamera", Toast.LENGTH_LONG).show();
        }

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(this, mCamera);

        RelativeLayout preview = (RelativeLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview, 0);

    }

    public static Camera getCameraInstance() throws RuntimeException{
        Camera c = Camera.open(FRONT_CAMERA_ID);
        return c;
    }

    @Override
    protected void onPause() {
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera == null){
            mCamera = getCameraInstance();
            mPreview = new CameraPreview(this, mCamera);

            RelativeLayout preview = (RelativeLayout) findViewById(R.id.camera_preview);

            preview.addView(mPreview, 0);
        }
    }

    @OnClick(R.id.capture_photo)
    void capturePhoto(){
        mCamera.takePicture(null, null, mPicture);
    }

    void releaseCamera(){
        if (mCamera != null) {
            // Call stopPreview() to stop updating the preview surface.
            mCamera.stopPreview();
            mCamera.setPreviewCallback(null);
            mPreview.getHolder().removeCallback(mPreview);
            // Important: Call release() to release the camera for use by other
            // applications. Applications should release the camera immediately
            // during onPause() and re-open() it during onResume()).
            mCamera.release();

            mCamera = null;
        }
    }

    private boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    private File getAlbumStorageDir(Context context, String albumName) {
        // Get the directory for the app's private pictures directory.
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("MAKE DIR", "Directory not created");
        }
        return file;
    }


    @Override
    public void navigateToRegisterUserView(Uri imageUri) {
        Intent i = new Intent();
        i.putExtra("image", imageUri);
        setResult(Activity.RESULT_OK, i);
        finish();
    }

    @Override
    public Uri cacheImage(byte[] byteImage) {
        File imageFile = new File(getCacheDir(), "profilePic.jpg");
        try {
            FileOutputStream stream = new FileOutputStream(imageFile);
            stream.write(byteImage);
            stream.close();
            return Uri.parse(imageFile.toURI().toString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // TODO HANDLE COULD NOT SAVE IMAGE ERROR
        return null;
    }
}
