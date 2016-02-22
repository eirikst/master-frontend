package com.andreasogeirik.master_frontend.application.user.profile_not_friend;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.application.user.profile_not_friend.interfaces.ProfileOthersInteractor;
import com.andreasogeirik.master_frontend.application.user.profile_not_friend.interfaces.ProfileOthersPresenter;
import com.andreasogeirik.master_frontend.communication.AcceptRequestTask;
import com.andreasogeirik.master_frontend.communication.FriendRequestTask;
import com.andreasogeirik.master_frontend.communication.UnFriendTask;
import com.andreasogeirik.master_frontend.listener.OnAcceptRequestListener;
import com.andreasogeirik.master_frontend.listener.OnFriendRequestedListener;
import com.andreasogeirik.master_frontend.listener.OnUnfriendedListener;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.ImageInteractor;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * Created by eirikstadheim on 17/02/16.
 */
public class ProfileOthersInteractorImpl implements ProfileOthersInteractor,
        OnFriendRequestedListener, OnUnfriendedListener, OnAcceptRequestListener,
        ImageInteractor.OnImageFoundListener{
    private ProfileOthersPresenter presenter;

    public ProfileOthersInteractorImpl(ProfileOthersPresenter presenter) {
        this.presenter = presenter;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Request friendship methods
     */
    @Override
    public void requestFriendship(User user) {
        new FriendRequestTask(this, user.getId()).execute();
    }

    @Override
    public void onFriendRequestSuccess(JSONObject friendship) {
        try {
            presenter.friendRequestSuccess(new Friendship(friendship));
        }
        catch(JSONException e) {
            System.out.println("JSON parse error. " + e);
            presenter.friendRequestFailure(Constants.JSON_PARSE_ERROR);
        }
    }

    @Override
    public void onFriendRequestFailure(int code) {
        presenter.friendRequestFailure(code);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Accept friendship methods
     */
    @Override
    public void acceptFriendship(int friendshipId) {
        new AcceptRequestTask(this, friendshipId).execute();
    }


    @Override
    public void onAcceptRequestSuccess(int friendshipId) {
        presenter.acceptRequestSuccess(friendshipId);
    }

    @Override
    public void onAcceptRequestFailure(int code) {
        presenter.acceptRequestFailure(code);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Reject friendship methods
     */
    @Override
    public void rejectFriendship(int friendshipId) {
        new UnFriendTask(this, friendshipId).execute();
    }

    @Override
    public void onRejectFriendSuccess(int friendshipId) {
        presenter.rejectRequestSuccess(friendshipId);
    }

    @Override
    public void onRejectFriendFailure(int code) {
        presenter.rejectRequestFailure(code);
    }



    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Image handling
     */
    @Override
    public void findImage(String imageUri, File storagePath) {
        ImageInteractor.getInstance().findImage(imageUri, storagePath, this);
    }

    @Override
    public void foundImage(String imageUrl, Bitmap bitmap) {
        presenter.imageFound(imageUrl, bitmap);
    }

    @Override
    public void onProgressChange(int percent) {
        //TODO:handle this maybe maybe not
    }

    @Override
    public void imageNotFound(String imageUri) {
        //TODO: imageUri?
        presenter.imageNotFound();
    }
}
