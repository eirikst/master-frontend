package com.andreasogeirik.master_frontend.application.user.profile;

import android.graphics.Bitmap;

import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfileInteractor;
import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfilePresenter;
import com.andreasogeirik.master_frontend.communication.GetAttendedEventsTask;
import com.andreasogeirik.master_frontend.communication.GetAttendingEventsTask;
import com.andreasogeirik.master_frontend.communication.GetFriendsTask;
import com.andreasogeirik.master_frontend.communication.GetPostsTask;
import com.andreasogeirik.master_frontend.data.CurrentUser;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingFriendshipsListener;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingPostsListener;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.UserPost;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;
import com.andreasogeirik.master_frontend.util.ImageInteractor;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class ProfileInteractorImpl implements ProfileInteractor, OnFinishedLoadingPostsListener,
        OnFinishedLoadingFriendshipsListener, ImageInteractor.OnImageFoundListener,
        GetAttendingEventsTask.OnFinishedLoadingAttendingEventsListener

{
    private ProfilePresenter presenter;

    public ProfileInteractorImpl(ProfilePresenter presenter) {
        this.presenter = presenter;
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Handling post list
     */
    @Override
    public void findPosts(User user, int start) {
        new GetPostsTask(this, user, start).execute();
    }

    @Override
    public void onSuccessPostsLoad(JSONArray jsonPosts) {
        Set<UserPost> posts = new HashSet<>();

        try {
            for (int i = 0; i < jsonPosts.length(); i++) {
                posts.add(new UserPost(jsonPosts.getJSONObject(i)));
            }
        }
        catch (JSONException e) {
            presenter.errorPostsLoad(Constants.CLIENT_ERROR);
        }
        presenter.successPostsLoad(posts);
    }

    @Override
    public void onFailedPostsLoad(int error) {
        presenter.errorPostsLoad(error);
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    /*
     * Handling friend list
     */
    @Override
    public void findFriends(int userId) {
        new GetFriendsTask(this, userId).execute();
    }

    @Override
    public void onSuccessFriendshipsLoad(JSONArray friendshipsJson) {
        Set<Friendship> friendships = new HashSet<>();

        try {
            for(int i = 0; i < friendshipsJson.length(); i++) {
                friendships.add(new Friendship(friendshipsJson.getJSONObject(i)));
            }
        }
        catch (JSONException e) {
            System.out.println("JSON error: " + e);
            presenter.errorFriendsLoad(Constants.CLIENT_ERROR);
        }

        presenter.successFriendsLoad(friendships);
    }

    @Override
    public void onFailedFriendshipsLoad(int code) {
        presenter.errorFriendsLoad(code);
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
        presenter.imageNotFound(imageUri);
    }

    @Override
    public void findAttendingEvents(User user) {
        new GetAttendingEventsTask(this, user).execute();
    }

    @Override
    public void onSuccessAttendingEvents(JSONArray eventsJson) {
        Set<Event> events = new HashSet<>();
        try {
            for(int i = 0; i < eventsJson.length(); i++) {
                events.add(new Event(eventsJson.getJSONObject(i)));
            }
            presenter.successAttendingEvents(events);
        }
        catch (JSONException e) {
            System.out.println("JSON error: " + e);
            presenter.failureAttendingEvents(Constants.CLIENT_ERROR);
        }
    }

    @Override
    public void onFailureAttendingEvents(int code) {
        presenter.failureAttendingEvents(code);
    }


}
