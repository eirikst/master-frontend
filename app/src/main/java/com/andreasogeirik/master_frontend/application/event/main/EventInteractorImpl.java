package com.andreasogeirik.master_frontend.application.event.main;

import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventInteractor;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.communication.GetFriendsTask;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingFriendsListener;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public class EventInteractorImpl implements EventInteractor, OnFinishedLoadingFriendsListener {
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
}
