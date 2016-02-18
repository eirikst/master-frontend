package com.andreasogeirik.master_frontend.application.event.main;

import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventInteractor;
import com.andreasogeirik.master_frontend.application.event.main.interfaces.EventPresenter;
import com.andreasogeirik.master_frontend.communication.GetMyFriendsTask;
import com.andreasogeirik.master_frontend.communication.GetMeTask;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingFriendshipsListener;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingUserListener;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by eirikstadheim on 16/02/16.
 */
public class EventInteractorImpl implements EventInteractor, OnFinishedLoadingFriendshipsListener ,
        OnFinishedLoadingUserListener {
    private EventPresenter presenter;

    public EventInteractorImpl(EventPresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void findFriendships() {
        new GetMyFriendsTask(this).execute();
    }

    @Override
    public void onSuccessFriendshipsLoad(JSONArray friendshipsJson) {
        Set<Friendship> friendships = new HashSet<>();

        //parse JSON array
        try {
            for(int i = 0; i < friendshipsJson.length(); i++) {
                JSONObject friendship = friendshipsJson.getJSONObject(i);

                int id = friendship.getInt("id");
                User friend = new User(friendship.getJSONObject("friend"));
                int status = friendship.getInt("status");
                Date friendsSince = new Date(friendship.getLong("friendsSince"));

                friendships.add(new Friendship(id, friend, status, friendsSince));
            }
        }
        catch (JSONException e) {
            System.out.println("JSON error: " + e);
            presenter.errorFriendshipsLoad(Constants.CLIENT_ERROR);
        }

        Set<Friendship> actualFriendships = new HashSet<>();
        Set<Friendship> friendRequests = new HashSet<>();

        Iterator<Friendship> it = friendships.iterator();
        while(it.hasNext()) {
            Friendship friendship = it.next();
            if(friendship.getStatus() == Friendship.FRIENDS) {
                actualFriendships.add(friendship);
            }
            else {
                friendRequests.add(friendship);
            }
        }

        System.out.println("actual" + actualFriendships);
        System.out.println("req" + friendRequests);
        presenter.successFriendshipsLoad(actualFriendships, friendRequests);
    }

    @Override
    public void onFailedFriendshipsLoad(int code) {
        presenter.errorFriendshipsLoad(code);
    }




    @Override
    public void findUser() {
        new GetMeTask(this).execute();
    }

    @Override
    public void onLoadingUserSuccess(JSONObject jsonUser) {
        try {
            User user = new User(jsonUser);
            presenter.findUserSuccess(user);
        }
        catch (JSONException e) {
            presenter.findUserFailure(Constants.JSON_PARSE_ERROR);
        }
    }

    @Override
    public void onLoadingUserFailure(int code) {

    }
}