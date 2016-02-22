package com.andreasogeirik.master_frontend.application.user.friend;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.application.user.friend.interfaces.FriendListPresenter;
import com.andreasogeirik.master_frontend.application.user.friend.interfaces.FriendListView;
import com.andreasogeirik.master_frontend.util.ImageInteractor;

import java.io.File;

/**
 * Created by eirikstadheim on 20/02/16.
 */
public class FriendListPresenterImpl implements FriendListPresenter,
        ImageInteractor.OnImageFoundListener {
    private FriendListView view;

    public FriendListPresenterImpl(FriendListView view) {
        this.view = view;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Image handling
     */

    @Override
    public void findImage(String imageUri, File storagePath) {
        if(imageUri == null || imageUri.equals("")) {
            view.findProfileImageFailure(imageUri);
        }
        ImageInteractor.getInstance().findImage(imageUri, storagePath, this);
    }


    @Override
    public void foundImage(String imageUri, Bitmap bitmap) {
        view.setProfileImage(imageUri, bitmap);
    }

    @Override
    public void onProgressChange(int percent) {
        //TODO:This gonna do anything?
    }

    @Override
    public void imageNotFound(String imageUri) {
        view.findProfileImageFailure(imageUri);
    }
}
