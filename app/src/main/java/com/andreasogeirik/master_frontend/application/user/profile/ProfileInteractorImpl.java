package com.andreasogeirik.master_frontend.application.user.profile;

import android.util.Log;

import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfileInteractor;
import com.andreasogeirik.master_frontend.application.user.profile.interfaces.ProfilePresenter;
import com.andreasogeirik.master_frontend.communication.GetAttendingEventsTask;
import com.andreasogeirik.master_frontend.communication.GetFriendsTask;
import com.andreasogeirik.master_frontend.communication.GetPostsTask;
import com.andreasogeirik.master_frontend.communication.GetUserTask;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingFriendshipsListener;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingPostsListener;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingUserListener;
import com.andreasogeirik.master_frontend.model.Event;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class ProfileInteractorImpl implements ProfileInteractor, OnFinishedLoadingPostsListener,
        OnFinishedLoadingFriendshipsListener, GetAttendingEventsTask.OnFinishedLoadingAttendingEventsListener,
        OnFinishedLoadingUserListener

{
    private String tag = getClass().getSimpleName();

    private ProfilePresenter presenter;

    public ProfileInteractorImpl(ProfilePresenter presenter) {
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
     * Handling post list
     */
    @Override
    public void findPosts(User user, int start) {
        new GetPostsTask(this, user, start).execute();
    }

    @Override
    public void onSuccessPostsLoad(JSONArray jsonPosts) {
        Set<Post> posts = new HashSet<>();

        try {
            for (int i = 0; i < jsonPosts.length(); i++) {
                posts.add(new Post(jsonPosts.getJSONObject(i)));
            }
        }
        catch (JSONException e) {
            Log.w(tag, "JSON error: " + e);
            presenter.errorPostsLoad(Constants.JSON_PARSE_ERROR);
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
            Log.w(tag, "JSON error: " + e);
            presenter.errorFriendsLoad(Constants.JSON_PARSE_ERROR);
        }

        presenter.successFriendsLoad(friendships);
    }

    @Override
    public void onFailedFriendshipsLoad(int code) {
        presenter.errorFriendsLoad(code);
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
            Log.w(tag, "JSON error: " + e);
            presenter.failureAttendingEvents(Constants.CLIENT_ERROR);
        }
    }

    @Override
    public void onFailureAttendingEvents(int code) {
        presenter.failureAttendingEvents(code);
    }
}
