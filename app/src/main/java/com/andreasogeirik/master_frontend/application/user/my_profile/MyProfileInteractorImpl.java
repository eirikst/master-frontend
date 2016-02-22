package com.andreasogeirik.master_frontend.application.user.my_profile;

import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfileInteractor;
import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfilePresenter;
import com.andreasogeirik.master_frontend.communication.GetFriendsTask;
import com.andreasogeirik.master_frontend.communication.GetPostsTask;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingFriendshipsListener;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingPostsListener;
import com.andreasogeirik.master_frontend.model.Friendship;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class MyProfileInteractorImpl implements MyProfileInteractor, OnFinishedLoadingPostsListener,
        OnFinishedLoadingFriendshipsListener
{
    private MyProfilePresenter presenter;

    public MyProfileInteractorImpl(MyProfilePresenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void findPosts(User user, int start) {
        new GetPostsTask(this, user, start).execute();
    }

    @Override
    public void findFriends(int userId) {
        new GetFriendsTask(this, userId).execute();
    }

    @Override
    public void onSuccessPostsLoad(JSONArray jsonPosts) {
        List<Post> posts = new ArrayList<>();

        try {
            for (int i = 0; i < jsonPosts.length(); i++) {
                posts.add(new Post(jsonPosts.getJSONObject(i)));
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

    @Override
    public void onSuccessFriendshipsLoad(JSONArray friendshipsJson) {
        Set<Friendship> friendships = new HashSet<>();

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
            presenter.errorFriendsLoad(Constants.CLIENT_ERROR);
        }

        presenter.successFriendsLoad(friendships);

    }

    @Override
    public void onFailedFriendshipsLoad(int code) {
        presenter.errorFriendsLoad(code);

    }
}