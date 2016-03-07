package com.andreasogeirik.master_frontend.util.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import com.andreasogeirik.master_frontend.listener.OnSampleImageFinishedListener;
import com.andreasogeirik.master_frontend.util.image.ImageContainer;
import com.andreasogeirik.master_frontend.util.image.ImageHandler;
import com.andreasogeirik.master_frontend.util.image.ImageStatusCode;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Andreas on 19.02.2016.
 */

public class SampleImageTask extends AsyncTask<Void, Void, ImageContainer> {

    private OnSampleImageFinishedListener listener;
    private InputStream inputStream;

    public SampleImageTask(OnSampleImageFinishedListener listener, InputStream inputStream) {
        this.listener = listener;
        this.inputStream = inputStream;
    }

    protected ImageContainer doInBackground(Void... params) {
        InputStream inputStreamWrapper = new BufferedInputStream(inputStream);
        try {
            inputStreamWrapper.mark(inputStreamWrapper.available());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStreamWrapper, null, options);

            if (options.outHeight != -1 && options.outWidth != 1) {
                options.inSampleSize = ImageHandler.calculateInSampleSize(options, 540, 540);
                inputStreamWrapper.reset();
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeStream(inputStreamWrapper, null, options);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);

                return new ImageContainer(ImageStatusCode.FILE_ENCODED, bitmap, stream.toByteArray());

            } else {
                return new ImageContainer(ImageStatusCode.NOT_AN_IMAGE, null, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new ImageContainer(ImageStatusCode.FILE_NOT_FOUND, null, null);
        }
    }

    protected void onPostExecute(ImageContainer imageContainer) {
        if (imageContainer.getStatus() == ImageStatusCode.FILE_ENCODED) {
            listener.onSuccess(imageContainer.getBitmap(), imageContainer.getByteImage());
        } else {
            listener.onError(imageContainer.getStatus());
        }
    }
}
