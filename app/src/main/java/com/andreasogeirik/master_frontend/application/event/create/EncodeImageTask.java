package com.andreasogeirik.master_frontend.application.event.create;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.andreasogeirik.master_frontend.communication.UploadImageTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by Andreas on 19.02.2016.
 */

public class EncodeImageTask extends AsyncTask<Void, Void, String> {

    private String imagePath;
    private String fileName;

    public EncodeImageTask(String imagePath, String fileName) {
        this.imagePath = imagePath;
        this.fileName = fileName;
    }

    protected String doInBackground(Void... params) {
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        Bitmap bitmap = BitmapFactory.decodeFile(this.imagePath,
                options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        return Base64.encodeToString(byte_arr, Base64.DEFAULT);
    }

    protected void onPostExecute(String encodedImage) {
        new UploadImageTask(encodedImage, fileName).execute();
    }
}
