package com.andreasogeirik.master_frontend.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;

/**
 * Created by eirikstadheim on 19/02/16.
 */
public class ImageInteractor {
    private static ImageInteractor instance;
    private BasicImageDownloader imgDownloader;

    protected ImageInteractor() {
        this.imgDownloader = new BasicImageDownloader();
    }

    public static ImageInteractor getInstance() {
        if(instance != null) {
            return instance;
        }
        return new ImageInteractor();
    }

    public interface OnImageFoundListener {
        void foundImage(String imageUri, Bitmap bitmap);
        void onProgressChange(int percent);
        void imageNotFound(String imageUri);
    }

    public void findImage(@NonNull final String imageUri, final File storagePath,
                          final OnImageFoundListener listener) {
        if(imageUri == null) {
            throw new IllegalArgumentException("Image URI cannot be empty");
        }
        /* Checks if external storage is available to at least read */
        if(isExternalStorageReadable()) {
            //Check if present locally
            File imgFile = new File(storagePath, imageUri);
            if (imgFile.exists()) {
                System.out.println("Image found locally");
                BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(),bmOptions);
                if(bitmap != null) {
                    listener.foundImage(imageUri, bitmap);
                    return;
                }
            }
        }
        else {
            System.out.println("External storage not readable");
        }
        System.out.println("Image not found locally");


        //can't find image locally, fetch from server
        imgDownloader.download(imageUri, false, new BasicImageDownloader.OnImageLoaderListener() {
            @Override
            public void onComplete(Bitmap result) {
                listener.foundImage(imageUri, result);

                imgDownloader.writeToDisk(new File(storagePath, imageUri), result,
                        Bitmap.CompressFormat.PNG, true,  new BasicImageDownloader.
                                OnBitmapSaveListener() {
                    @Override
                    public void onBitmapSaved() {
                        System.out.println("Image " + imageUri + " saved successfully.");
                    }

                    @Override
                    public void onBitmapSaveError(BasicImageDownloader.ImageError error) {
                        System.out.println("Image " + imageUri + " save not successful.");
                        System.out.println(error);
                    }
                });
            }

            @Override
            public void onError(BasicImageDownloader.ImageError error) {
                System.out.println("Did not find the image with URI " + imageUri);
                listener.imageNotFound(imageUri);//maybe return something
            }

            @Override
            public void onProgressChange(int percent) {
                listener.onProgressChange(percent);
            }
        });
    }

    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}