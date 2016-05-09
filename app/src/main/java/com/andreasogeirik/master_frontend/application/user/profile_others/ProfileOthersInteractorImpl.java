package com.andreasogeirik.master_frontend.application.user.profile_others;

import android.util.Log;

import com.andreasogeirik.master_frontend.application.user.profile_others.interfaces.ProfileOthersInteractor;
import com.andreasogeirik.master_frontend.application.user.profile_others.interfaces.ProfileOthersPresenter;
import com.andreasogeirik.master_frontend.communication.AcceptRequestTask;
import com.andreasogeirik.master_frontend.communication.FriendRequestTask;
import com.andreasogeirik.master_frontend.communication.GetUserTask;
import com.andreasogeirik.master_frontend.communication.UnFriendTask;
import com.andreasogeirik.master_frontend.listener.OnAcceptRequestListener;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingUserListener;
import com.andreasogeirik.master_frontend.listener.OnFriendRequestedListener;
import com.andreasogeirik.master_frontend.listener.OnUnfriendedListener;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * Created by eirikstadheim on 17/02/16.
 */
public class ProfileOthersInteractorImpl implements ProfileOthersInteractor,
        OnFriendRequestedListener, OnUnfriendedListener, OnAcceptRequestListener,
        OnFinishedLoadingUserListener {
    private String tag = getClass().getSimpleName();

    private ProfileOthersPresenter presenter;

    public ProfileOthersInteractorImpl(ProfileOthersPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void findUser(int userId) {
        new GetUserTask(this, userId).execute();
    }

    @Override
    public void onLoadingUserSuccess(JSONObject user) {
        try {
            presenter.onSuccessUserLoad(new User(user));
        }
        catch (JSONException e) {
            presenter.onFailedUserLoad(Constants.JSON_PARSE_ERROR);
        }
    }

    @Override
    public void onLoadingUserFailure(int code) {
        presenter.onFailedUserLoad(Constants.CLIENT_ERROR);
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
            Log.w(tag, "JSON parse error. " + e);
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
}
