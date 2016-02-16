package com.andreasogeirik.master_frontend.application.user.my_profile;

import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfileInteractor;
import com.andreasogeirik.master_frontend.application.user.my_profile.interfaces.MyProfilePresenter;
import com.andreasogeirik.master_frontend.communication.GetFriendsTask;
import com.andreasogeirik.master_frontend.communication.GetPostsTask;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingFriendsListener;
import com.andreasogeirik.master_frontend.listener.OnFinishedLoadingPostsListener;
import com.andreasogeirik.master_frontend.model.Post;
import com.andreasogeirik.master_frontend.model.User;
import com.andreasogeirik.master_frontend.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by eirikstadheim on 06/02/16.
 */
public class MyProfileInteractorImpl implements MyProfileInteractor, OnFinishedLoadingPostsListener,
        OnFinishedLoadingFriendsListener
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