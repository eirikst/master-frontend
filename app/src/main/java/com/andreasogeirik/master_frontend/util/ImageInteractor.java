package com.andreasogeirik.master_frontend.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;

import org.apache.commons.io.FilenameUtils;

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
            instance =  new ImageInteractor();
        return instance;
    }

    public interface OnImageFoundListener {
        void foundImage(String imageUri, Bitmap bitmap);
        void onProgressChange(int percent);
        void imageNotFound(String imageUri);
    }

    /*
     * Tries to find image locally. If not found locally, fetches from backend and stores locally
     */
    public void findImage(@NonNull final String imageUri, final File storagePath,
                          final OnImageFoundListener listener) {

        //find locally
        new LocalImageLoader(imageUri, storagePath, new OnLocalImageFoundListener() {
            @Override
            public void imageSuccess(String imageName, Bitmap bitmap) {
                listener.foundImage(imageName, bitmap);
                System.out.println("Found image " + imageName + " locally");
            }

            @Override
            public void imageFailure(int status) {
                //not found locally, try server
                loadImageFromServer(imageUri, storagePath, listener);
                System.out.println("Image not found locally for image " + imageUri + " Trying server.");

            }
        }).execute();
    }

    private void loadImageFromServer(@NonNull final String imageUri, final File storagePath,
                                     final OnImageFoundListener listener) {
        //can't find image locally, fetch from server
        imgDownloader.download(imageUri, false, new BasicImageDownloader.OnImageLoaderListener() {
            @Override
            public void onComplete(Bitmap result) {
                listener.foundImage(imageUri, result);
                //Write image to disk
                imgDownloader.writeToDisk(new File(storagePath, FilenameUtils.getName(imageUri) + ".jpg"), result,
                        Bitmap.CompressFormat.JPEG, true, new BasicImageDownloader.
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







    private interface OnLocalImageFoundListener {
        void imageSuccess(String imageName, Bitmap bitmap);
        void imageFailure(int status);
    }

    private class LocalImageLoader extends AsyncTask<Void, Void, Bitmap> {
        private static final int OK = 0;
        private static final int EXTERNAL_STORAGE_NOT_READABLE = 1;
        private static final int FILE_DOES_NOT_EXIST = 2;
        private static final int ERROR = 3;

        private OnLocalImageFoundListener listener;
        private String imageName;
        private File storagePath;
        private int status;

        public LocalImageLoader(String imageName, File storagePath,
                                OnLocalImageFoundListener listener) {
            if(imageName == null) {
                throw new IllegalArgumentException("Image name cannot be empty");
            }

            this.listener = listener;
            this.imageName = imageName;
            this.storagePath = storagePath;
        }


        private boolean isExternalStorageReadable() {
            String state = Environment.getExternalStorageState();
            if (Environment.MEDIA_MOUNTED.equals(state) ||
                    Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
                return true;
            }
            return false;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            /* Checks if external storage is available to at least read */
            if(isExternalStorageReadable()) {
                //Check if present locally
                File imgFile = new File(storagePath, FilenameUtils.getName(imageName) + ".jpg");
                if (imgFile.exists()) {
                    System.out.println("Image found locally");
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();

                    Bitmap bitmap = null;
                    try {
                        bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath(), bmOptions);
                    }
                    catch(Throwable e) {
                        Log.w(getClass().getSimpleName(), e);
                    }
                    if(bitmap != null) {
                        return bitmap;
                    }
                    else {
                        status = ERROR;
                        System.out.println(getClass().getName() + ": Error while fetching image. Image name: " + imageName);
                    }
                }
                else {
                    status = FILE_DOES_NOT_EXIST;
                    System.out.println(getClass().getName() + ": File does not exist. Image name: " + imageName);
                }
            }
            status = EXTERNAL_STORAGE_NOT_READABLE;
            System.out.println(getClass().getName() + ": External storage not readable. Image name: " + imageName);
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null) {
                listener.imageSuccess(imageName, bitmap);
            }
            else {
                listener.imageFailure(status);
            }
        }
    }
}