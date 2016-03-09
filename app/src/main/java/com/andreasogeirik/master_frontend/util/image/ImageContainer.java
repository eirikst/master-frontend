package com.andreasogeirik.master_frontend.util.image;

import android.graphics.Bitmap;

/**
 * Created by Andreas on 20.02.2016.
 */
public class ImageContainer {
    private ImageStatusCode status;
    private Bitmap bitmap;
    private byte[] byteImage;

    public ImageContainer(ImageStatusCode status, Bitmap bitmap, byte[] byteImage) {
        this.status = status;
        this.bitmap = bitmap;
        this.byteImage = byteImage;
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

    public byte[] getByteImage() {
        return byteImage;
    }

    public void setByteImage(byte[] byteImage) {
        this.byteImage = byteImage;
    }
}
