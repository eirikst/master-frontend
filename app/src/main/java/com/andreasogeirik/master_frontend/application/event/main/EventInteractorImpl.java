package com.andreasogeirik.master_frontend.application.event.main;

import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventInteractor;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.communication.GetFriendRequestsTask;
import com.andreasogeirik.master_frontend.communication.GetFriendsTask;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingFriendsListener;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingFriendshipsListener;
import com.andreasogeirik.master_frontend.model.FriendRequest;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public class EventInteractorImpl implements EventInteractor, OnFinishedLoadingFriendsListener ,
        OnFinishedLoadingFriendshipsListener {
    private EventPresenter presenter;

    public EventInteractorImpl(EventPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void findFriends(int userId) {
        new GetFriendsTask(this, userId).execute();
    }

    @Override
    public void onSuccessFriendsLoad(JSONArray jsonFriends) {
        Set<User> friends = new HashSet<>();

        try {
            for (int i = 0; i < jsonFriends.length(); i++) {
                friends.add(new User(jsonFriends.getJSONObject(i)));
            }
        }
        catch (JSONException e) {
            presenter.errorFriendsLoad(Constants.CLIENT_ERROR);
        }
        presenter.successFriendsLoad(friends);
    }

    @Override
    public void onFailedFriendsLoad(int code) {
        presenter.errorFriendsLoad(code);
    }

    @Override
    public void findFriendRequests() {
        new GetFriendRequestsTask(this).execute();
    }

    @Override
    public void onSuccessFriendRequestLoad(JSONArray requestsJson) {
        Set<FriendRequest> requests = new HashSet<>();

        //parse JSON array
        try {
            for(int i = 0; i < requestsJson.length(); i++) {

                JSONObject jsonFriend = requestsJson.getJSONObject(i).getJSONObject("friend");

                User friend = new User(jsonFriend.getInt("id"), jsonFriend.getString("email"),
                        jsonFriend.getBoolean("enabled"), jsonFriend.getString("firstname"),
                        jsonFriend.getString("lastname"), jsonFriend.getString("location"),
                        jsonFriend.getString("imageUri"));

                requests.add(new FriendRequest(new Date(requestsJson.getJSONObject(i).getLong
                        ("friendsSince")), requestsJson.getJSONObject(i).getBoolean("myRequest"),
                        friend));
            }
        }
        catch (JSONException e) {
            System.out.println("JSON error: " + e);
            presenter.errorFriendsLoad(Constants.CLIENT_ERROR);
        }

        presenter.successFriendRequestLoad(requests);
    }

    @Override
    public void onFailedFriendRequestLoad(int code) {
        presenter.errorFriendRequestsLoad(code);
    }
}
