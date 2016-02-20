package com.andreasogeirik.master_frontend.application.event.create;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import com.andreasogeirik.master_frontend.listener.OnEncodeImageFinishedListener;
import com.andreasogeirik.master_frontend.util.ImageHandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * Created by Andreas on 19.02.2016.
 */

public class EncodeImageTask extends AsyncTask<Void, Void, EncodedImageContainer> {

    private OnEncodeImageFinishedListener listener;
    private InputStream inputStream;

    public EncodeImageTask(OnEncodeImageFinishedListener listener, InputStream inputStream) {
        this.listener = listener;
        this.inputStream = inputStream;
    }

    protected EncodedImageContainer doInBackground(Void... params) {
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
                String encodedImage = Base64.encodeToString(stream.toByteArray(), Base64.DEFAULT);
                return new EncodedImageContainer(ImageStatusCode.FILE_ENCODED, bitmap, encodedImage);

            } else {
                return new EncodedImageContainer(ImageStatusCode.NOT_AN_IMAGE, null, null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new EncodedImageContainer(ImageStatusCode.FILE_NOT_FOUND, null, null);
        }
    }

    protected void onPostExecute(EncodedImageContainer encodedImageContainer) {
        if (encodedImageContainer.getStatus() == ImageStatusCode.FILE_ENCODED) {
            listener.onSuccess(encodedImageContainer.getBitmap(), encodedImageContainer.getEncodedImage());
        } else {
            listener.onError(encodedImageContainer.getStatus());
        }
    }
}
