package com.andreasogeirik.master_frontend.application.event.create;

import android.graphics.Bitmap;

/**
 * Created by Andreas on 20.02.2016.
 */
public class EncodedImageContainer {
    private ImageStatusCode status;
    private Bitmap bitmap;
    private String encodedImage;

    public EncodedImageContainer(ImageStatusCode status, Bitmap bitmap, String encodedImage) {
        this.status = status;
        this.bitmap = bitmap;
        this.encodedImage = encodedImage;
    }

    public ImageStatusCode getStatus() {
        return status;
    }

    public void setStatus(ImageStatusCode status) {
        this.status = status;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }
}
